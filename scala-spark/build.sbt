import org.apache.logging.log4j.core.config.composite.MergeStrategy

name := "scala-spark"

version := "0.1"

scalaVersion := "2.11.11"


libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.4.1",
  "org.apache.spark" %% "spark-sql" % "2.4.1",
  "org.apache.spark" %% "spark-streaming" % "2.4.1",
  "org.apache.spark" %% "spark-sql-kafka-0-10" % "2.4.1",

)
