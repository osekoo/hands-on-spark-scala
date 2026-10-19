import org.apache.spark.sql.{DataFrame, SaveMode}
import org.apache.spark.sql.functions._

// Run with spark-shell -i generate_course_data.scala
// Configuration is supplied through environment variables; see README.md.

val outputRoot = sys.env.getOrElse("COURSE_DATA_PATH", "./spark-course-data")
val customerCount = sys.env.get("CUSTOMER_COUNT").map(_.toLong).getOrElse(200000L)
val orderCount = sys.env.get("ORDER_COUNT").map(_.toLong).getOrElse(2000000L)
val eventCount = sys.env.get("EVENT_COUNT").map(_.toLong).getOrElse(3000000L)
val normalPartitions = sys.env.get("NORMAL_PARTITIONS").map(_.toInt).getOrElse(32)
val smallFilePartitions = sys.env.get("SMALL_FILE_PARTITIONS").map(_.toInt).getOrElse(240)
val overwrite = sys.env.getOrElse("OVERWRITE", "true").toBoolean
val mode = if (overwrite) SaveMode.Overwrite else SaveMode.ErrorIfExists

require(customerCount >= 100, "CUSTOMER_COUNT must be at least 100")
require(orderCount >= 1000, "ORDER_COUNT must be at least 1,000")
require(eventCount >= 1000, "EVENT_COUNT must be at least 1,000")
require(normalPartitions > 0 && smallFilePartitions > 0, "Partition counts must be positive")

val countryCodes = array(
  lit("TG"), lit("FR"), lit("IT"), lit("DE"), lit("BE"),
  lit("GH"), lit("CI"), lit("SN"), lit("US"), lit("GB")
)
val categories = array(lit("Books"), lit("Office"), lit("Electronics"), lit("Food"), lit("Home"))
val eventTypes = array(lit("view"), lit("search"), lit("cart"), lit("purchase"), lit("support"))

val countries = Seq(
  ("TG", "Togo", "West Africa", true),
  ("FR", "France", "Europe", true),
  ("IT", "Italy", "Europe", true),
  ("DE", "Germany", "Europe", true),
  ("BE", "Belgium", "Europe", true),
  ("GH", "Ghana", "West Africa", true),
  ("CI", "Côte d'Ivoire", "West Africa", true),
  ("SN", "Senegal", "West Africa", true),
  ("US", "United States", "North America", true),
  ("GB", "United Kingdom", "Europe", false)
).toDF("country_code", "country_name", "region", "active")

val customers = spark.range(1L, customerCount + 1L)
  .select(
    col("id").as("customer_id"),
    concat(lit("Customer "), col("id")).as("customer_name"),
    element_at(countryCodes, (pmod(col("id"), lit(10)) + 1).cast("int")).as("country_code"),
    when(pmod(col("id"), lit(20)) === 0, "VIP")
      .when(pmod(col("id"), lit(4)) === 0, "Business")
      .otherwise("Standard").as("segment"),
    (pmod(col("id"), lit(17)) =!= 0).as("active"),
    date_add(lit("2022-01-01").cast("date"), pmod(col("id"), lit(1460)).cast("int")).as("signup_date"),
    concat(lit("customer"), col("id"), lit("@example.org")).as("email")
  )

// About 22% of orders use customer_id = 42. This deliberate hot key creates join/aggregation skew.
val orders = spark.range(orderCount)
  .select(
    (col("id") + 1000000L).as("order_id"),
    when(rand(17L) < 0.22, lit(42L))
      .otherwise(pmod(xxhash64(col("id")), lit(customerCount)) + 1L).as("customer_id"),
    when(rand(29L) < 0.63, "PAID")
      .when(rand(31L) < 0.55, "PENDING")
      .otherwise("CANCELLED").as("status"),
    round(rand(41L) * 490.0 + 10.0, 2).as("amount"),
    element_at(categories, (pmod(col("id"), lit(5)) + 1).cast("int")).as("product_category"),
    element_at(countryCodes, (pmod(xxhash64(col("id"), lit("country")), lit(10)) + 1).cast("int")).as("country"),
    date_add(lit("2026-01-01").cast("date"), pmod(col("id"), lit(365)).cast("int")).as("order_date"),
    concat(lit("unused payload "), sha2(col("id").cast("string"), 256)).as("debug_payload")
  )

val events = spark.range(eventCount)
  .select(
    col("id").as("event_id"),
    when(rand(53L) < 0.18, lit(42L))
      .otherwise(pmod(xxhash64(col("id")), lit(customerCount)) + 1L).as("user_id"),
    element_at(eventTypes, (pmod(col("id"), lit(5)) + 1).cast("int")).as("event_type"),
    round(rand(61L) * 100.0, 2).as("value"),
    date_add(lit("2026-10-01").cast("date"), pmod(col("id"), lit(31)).cast("int")).as("event_date"),
    (unix_timestamp(lit("2026-10-01 00:00:00")) + pmod(col("id"), lit(2678400))).cast("timestamp").as("event_time")
  )

def writeParquet(df: DataFrame, path: String, partitions: Int): Unit =
  df.repartition(partitions).write.mode(mode).parquet(path)

println(s"Generating course data in: $outputRoot")
println(s"customers=$customerCount, orders=$orderCount, events=$eventCount")

countries.coalesce(1).write.mode(mode).parquet(s"$outputRoot/countries")
writeParquet(customers, s"$outputRoot/customers", normalPartitions)

// Good baseline for date pruning.
orders.repartition(normalPartitions, col("order_date"))
  .write.mode(mode).partitionBy("order_date").parquet(s"$outputRoot/orders_partitioned")

// Deliberately fragmented copy for the small-file lab.
writeParquet(orders, s"$outputRoot/orders_small_files", smallFilePartitions)

events.repartition(normalPartitions, col("event_date"))
  .write.mode(mode).partitionBy("event_date").parquet(s"$outputRoot/events")

println("Generation complete.")
Seq("countries", "customers", "orders_partitioned", "orders_small_files", "events")
  .foreach(name => println(s"  $outputRoot/$name"))
println("Hot key for skew exercises: customer_id/user_id = 42")

