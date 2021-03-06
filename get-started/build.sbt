name := "get-started" // le nom de votre projet
version := "0.1" // la version de votre application
scalaVersion := "2.12.13" // la version de Scala (l'information la plus importante!)

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.0.2",
  "org.apache.spark" %% "spark-sql" % "3.0.2",
  "org.apache.spark" %% "spark-ml" % "3.0.2",
"org.apache.spark" %% "spark-mllib" % "3.0.2" % "provided"
)
