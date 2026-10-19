import org.apache.spark.sql.functions._
import org.apache.spark.storage.StorageLevel

val root = sys.env.getOrElse("COURSE_DATA_PATH", "./spark-course-data")
val orders = spark.read.parquet(s"$root/orders_partitioned")
val fragmentedOrders = spark.read.parquet(s"$root/orders_small_files")
val customers = spark.read.parquet(s"$root/customers")
val countries = spark.read.parquet(s"$root/countries")
val events = spark.read.parquet(s"$root/events")

println(s"Orders partitions: ${orders.rdd.getNumPartitions}")
println(s"Fragmented orders partitions: ${fragmentedOrders.rdd.getNumPartitions}")

// LAB 1 — Plan reading and partition pruning
val januaryPaid = orders
  .filter(col("order_date").between(lit("2026-01-01"), lit("2026-01-31")))
  .filter(col("status") === "PAID")
  .select("customer_id", "amount", "country")
januaryPaid.explain("formatted")

// LAB 2 — Find the deliberate hot key
orders.groupBy("customer_id").count()
  .orderBy(desc("count"))
  .show(10, truncate = false)

// LAB 3 — Compare a normal join and an explicit broadcast
val activeCountries = countries.filter(col("active") === true)
val normalJoin = orders.join(activeCountries, col("country") === col("country_code"))
val broadcastJoin = orders.join(broadcast(activeCountries), col("country") === col("country_code"))
normalJoin.explain("formatted")
broadcastJoin.explain("formatted")

// LAB 4 — Cache only because the filtered base feeds two actions
val paidBase = orders
  .filter(col("status") === "PAID")
  .select("customer_id", "country", "amount")
  .persist(StorageLevel.MEMORY_AND_DISK)

paidBase.groupBy("country").agg(sum("amount").as("revenue")).count()
paidBase.groupBy("customer_id").agg(sum("amount").as("revenue")).count()
paidBase.unpersist()

// LAB 5 — Small-file comparison
val compactedPath = s"$root/orders_compacted"
fragmentedOrders.coalesce(32).write.mode("overwrite").parquet(compactedPath)
val compacted = spark.read.parquet(compactedPath)
println(s"Before compaction partitions: ${fragmentedOrders.rdd.getNumPartitions}")
println(s"After compaction partitions: ${compacted.rdd.getNumPartitions}")

// LAB 6 — RDD aggregation anti-pattern (run on a sample)
val pairs = orders.sample(0.01).select("customer_id", "amount").rdd
  .map(r => (r.getLong(0), r.getDouble(1)))
val inefficient = pairs.groupByKey().mapValues(_.sum)
val preferred = pairs.reduceByKey(_ + _)
println(s"groupByKey partitions: ${inefficient.getNumPartitions}")
println(s"reduceByKey partitions: ${preferred.getNumPartitions}")

