import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

object StreamrSparkDirect {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("StreamrSparkDirect")
    val ssc = new StreamingContext(conf, Seconds(10))
    val streamrReceiver: DStream[String] = ssc.receiverStream(new StreamrCustomReceiver("YOUR_STREAMR_API_KEY", "YOUR_STREAM_ID"))

    val filtered = streamrReceiver.filter(str => str.contains("6T"))

    filtered.print()
    ssc.start()
    ssc.awaitTermination()
  }

}
