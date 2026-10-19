import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.storage.StorageLevel

object MainApp {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder
      .appName("Word Count")
      .getOrCreate() // create a Spark session

    spark.sparkContext.setLogLevel("ERROR") // set the log level to ERROR (possible values: ALL, DEBUG, ERROR, FATAL, INFO, OFF, TRACE, WARN)


    val root = sys.env.getOrElse("COURSE_DATA_PATH", "./spark-course-data")
    val orders = spark.read.parquet(s"$root/orders_partitioned")
    val fragmentedOrders = spark.read.parquet(s"$root/orders_small_files")
    val customers = spark.read.parquet(s"$root/customers")
    val countries = spark.read.parquet(s"$root/countries")
    val events = spark.read.parquet(s"$root/events")

    spark.stop() // stop the Spark session
  }

}
