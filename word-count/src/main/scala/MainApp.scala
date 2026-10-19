import org.apache.spark.sql.SparkSession

object MainApp {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder
      .appName("Word Count")
      .getOrCreate() // create a Spark session

    spark.sparkContext.setLogLevel("ERROR")  // set the log level to ERROR (possible values: ALL, DEBUG, ERROR, FATAL, INFO, OFF, TRACE, WARN)


    spark.stop() // stop the Spark session
  }

}
