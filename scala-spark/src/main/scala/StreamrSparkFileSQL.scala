import org.apache.spark
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.apache.spark.sql
import org.apache.spark.sql.types.StructType

object StreamrSparkFileSQL {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .appName("StreamrSparkFileSQL")
      .getOrCreate()

    val userSchema = new StructType().add("deviceUuid", "string").add("humidity", "double").add("temperature", "double").add("pressure", "integer").add("accelerationX", "integer").add("accelerationY", "integer").add("accelerationZ", "integer").add("battery", "integer").add("timestamp", "integer").add("totalAcceleration", "double").add("meanTotalAcceleration", "double").add("zscoreTotalAcceleration", "double")
    val jsonDF: DataFrame = spark
      .readStream
      .schema(userSchema)
      .json("/Users/user/streamr-file-spark-streaming/tmp").as("input")
    val anomalies = jsonDF.select("input", "zscoreTotalAcceleration").where("zscoreTotalAcceleration < (-0.5) OR zscoreTotalAcceleration > 0.5")
    val query = anomalies.writeStream
      .format("json")
      .outputMode("append")
      .option("checkpointLocation", "/Users/user/streamr-file-spark-streaming/checkpoint")
      .option("path", "/Users/user/streamr-file-spark-streaming/res")
      .start()

    query.awaitTermination()
  }
}
