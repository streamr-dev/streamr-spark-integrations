import org.apache.spark.SparkConf
import org.apache.spark.sql.functions.{col, from_json}
import org.apache.spark.sql.{DataFrame, SQLContext, SparkSession}
import org.apache.spark.sql.types.{DoubleType, IntegerType, LongType, StringType, StructType, TimestampType}
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

object StreamrSparkStreamingSQL {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("StreamrSparkStreamingSQL")
    val ssc = new StreamingContext(conf, Seconds(5))
    val streamrReceiver: DStream[String] = ssc.receiverStream(new StreamrCustomReceiver("Hb35dKItSxeJI9VSOPBLLAiF6EHACZQqGZo8mwKf3gJw", "7wa7APtlTq6EC5iTCBy6dw"))
    // Schema for the Streamr marketplace Helsinki Tram Stream
    val schema = new StructType()
        .add("desi", StringType)
        .add("dir", StringType)
        .add("oper", IntegerType)
        .add("veh", IntegerType)
        .add("tst", StringType)
        .add("tsi", LongType)
        .add("spd", DoubleType)
        .add("hdg", IntegerType)
        .add("lat", DoubleType)
        .add("long", DoubleType)
        .add("acc", DoubleType)
        .add("dl", IntegerType)
        .add("odo", IntegerType)
        .add("drst", IntegerType)
        .add("jrn", IntegerType)
        .add("line", IntegerType)
        .add("start", StringType)

    // Do through all RDDs in the DStream and filter all trams on path 6, 6T, 6H and print them to console
    streamrReceiver.foreachRDD{rdd =>
      val spark = SparkSession.builder.config(rdd.sparkContext.getConf).getOrCreate()
      import spark.implicits._
      val tramsDF = rdd.toDF("tram")
        .selectExpr("CAST(tram AS STRING) as tram")
        .select(from_json(col("tram"), schema).as("trams"))
        .select("trams.*").filter(col("desi").contains("6"))
        .show()
    }

    ssc.start()
    ssc.awaitTermination()
  }
}
//{"desi":"3","dir":"1","oper":40,"veh":79,"tst":"2019-05-27T14:18:19Z","tsi":1558966699,"spd":2.69,"hdg":350,"lat":60.179306,"long":24.949983,"acc":-0.8,"dl":-76,"odo":4194,"drst":0,"oday":"2019-05-27","jrn":825,"line":31,"start":"16:57"}