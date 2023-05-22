import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.{RegexTokenizer, StopWordsRemover}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

import scala.io.Source

/**
 * Implements Word Count using Spark SQL
 */
object WordCount {
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  @transient lazy val logger: Logger = Logger.getLogger("$")

  def main(args: Array[String]): Unit = {
    logger.info("Initializing spark context...")
    val spark: SparkSession = SparkSession.builder()
      .appName(s"WordCount")
      //.master("spark://localhost:7077")
      .master("local[*]")
      .getOrCreate()

    val filePath = "ulysses.txt"
    logger.info(s"loading text from $filePath ...")
    val content = Source.fromFile(filePath) // reading file content

    val data = spark.createDataFrame(Seq((0, content.mkString)))
      .toDF("id", "sentence")

    logger.info("tokenizing the input text...")
    val tokenizer = new RegexTokenizer().setInputCol("sentence")
      .setOutputCol("words")
      .setPattern("[a-z]+").setGaps(false) // Set up words filtering regex. We only keep words

    logger.info("removing stop words from the dataframe...")
    val remover = new StopWordsRemover().setInputCol("words").setOutputCol("word")

    // transformer pipeline
    val pipeline = new Pipeline().setStages(Array(tokenizer, remover))
    logger.info("transforming the input text...")
    val model = pipeline.fit(data)
    val words = model.transform(data)
    words.show(100, truncate = true)

    logger.info("exploding words into rows...")
    val tokens = words.select(explode(col("word")).as("token"))
      .where(length(col("token")) > 1)
    logger.info("Nb words: " + tokens.count())
    tokens.show(false)

    logger.info("counting the occurrence of each word...")
    val wordCountDf = tokens
      .groupBy("token") // counting the words using groupBy() instruction
      .count()
      .orderBy(desc("count"))
    wordCountDf.count();

    logger.info("displaying the dataframe...")
    wordCountDf.show(100, truncate = false)



    //    logger.info("saving the dataframe in csv format...")
    //    wordCountDf.repartition(1) // grouping the data into 1 partition
    //      .write.mode(SaveMode.Overwrite)
    //      .csv("tokens.csv")
    //
    //    logger.info("saving the dataframe in parquet format...")
    //    wordCountDf.write.mode(SaveMode.Overwrite)
    //      .parquet("tokens.parquet")
  }
}
