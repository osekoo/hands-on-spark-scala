import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.functions.col

import scala.io.Source

/**
 * Implements Word Count using Spark SQL
 */
object WordCount {
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  @transient lazy val logger: Logger = Logger.getLogger("$")

  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder()
      .appName(s"WordCount")
      .master("local[*]")
      .getOrCreate()

    val filePath = "https://raw.githubusercontent.com/osekoo/hands-on-spark-scala/develop/data/ulysses.txt"

    logger.info(s"loading text from $filePath ...")
    val content = Source.fromURL(filePath).mkString.toUpperCase // downloading URL's content
    val pattern = "([A-Z]+)".r // Set up words filtering regex

    logger.info("tokenizing the input text...")
    val tokens = pattern.findAllIn(content) // extracting words from the above text
      .matchData.map(_.group(1)).toSeq
      .zipWithIndex

    logger.info("converting the tokens into dataframe...")
    val tokensDf = spark.createDataFrame(tokens) // creating DataFrame from above Seq
      .toDF("token", "id")
    tokensDf.show(truncate = false)

    logger.info("counting the occurrence of each word...")
    val wordCountDf = tokensDf.where(length(col("token")) > 2) // we keep only words with length greater than 2
      .groupBy("token") // computing the word count using groupBy() instruction
      .count()
      .orderBy(desc("count"))

    logger.info("displaying the dataframe...")
    wordCountDf.show(truncate = false)

    logger.info("saving the dataframe in csv format...")
    wordCountDf.repartition(1) // grouping the data into 1 partition
      .write.mode(SaveMode.Overwrite)
      .csv("tokens.csv")

    logger.info("saving the dataframe in parquet format...")
    wordCountDf.write.mode(SaveMode.Overwrite)
      .parquet("tokens.parquet")
  }
}
