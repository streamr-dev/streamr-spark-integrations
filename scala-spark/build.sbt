import org.apache.logging.log4j.core.config.composite.MergeStrategy

name := "scala-spark"

version := "0.1"

scalaVersion := "2.11.11"


libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.4.1",
  "org.apache.spark" %% "spark-sql" % "2.4.1",
  "org.apache.spark" %% "spark-streaming" % "2.4.1",
  "org.apache.spark" %% "spark-sql-kafka-0-10" % "2.4.1",

  // For Streamr's direct integrations
//  "org.java-websocket" % "Java-WebSocket" % "1.4.0",
//  "com.squareup.moshi" % "moshi" % "1.8.0",
//  "com.squareup.okhttp3" % "okhttp" % "3.14.2",
//  "org.apache.logging.log4j" % "log4j-core" % "2.9.0",
//  "org.apache.logging.log4j" % "log4j-api" % "2.9.0",
//  "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.9.0",
//  "org.cache2k" % "cache2k-core" % "1.2.0.Final",
//  "org.cache2k" % "cache2k-api" % "1.2.0.Final",
//  "commons-codec" % "commons-codec" % "1.12",
//  "org.apache.commons" % "commons-lang3" % "3.9"
)
