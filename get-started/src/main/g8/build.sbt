name := sys.env.get("APP_NAME").getOrElse("get-started") // the project's name
version := sys.env.get("APP_VERSION").getOrElse("0.1") // the application version
scalaVersion := sys.env.get("SCALA_FULL_VERSION").getOrElse("2.12.18") // version of Scala we want to use (this should be in line with the version of Spark framework)
organization := "com.osekoo.dev"

val sparkVersion = sys.env.get("SPARK_VERSION").getOrElse("3.5.0")

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion % "provided"
)
