import org.apache.spark._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{DoubleType, IntegerType, StringType, StructType, LongType}
import org.apache.spark.sql.functions._
import org.apache.spark.sql._
import org.apache.spark.sql.catalyst.expressions._
object KafkaStreamrSpark {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder.appName("KafkaStreamrSpark").getOrCreate()

      val schema = new StructType()
          .add("id", IntegerType)
          .add("altitude", IntegerType)
          .add("speed", DoubleType)
          .add("heading", DoubleType)
          .add("latitude", DoubleType)
          .add("longitude", DoubleType)
          .add("callsign", StringType)
          .add("count", IntegerType)
          .add("seen", LongType)


    val df = spark
      // Read kafka stream
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("subscribe", "streamrspark")
      .load()

      // Operations for stream inputs
      .selectExpr("CAST(value AS STRING) as json")
      .select(from_json(col("json"), schema).as("data"))
      .select("data.*")

      df.filter(col("altitude") > 10000).as("out")

      // Output operations back to Kafka

      .selectExpr("CAST(out.id AS STRING) AS key", "to_json(struct(*)) AS value")
      .writeStream
      .format("kafka")
      .outputMode("append")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("topic", "streamrsparkOut")
      .option("group.id", "streamrsparkOut")
      .option("checkpointLocation", "/Users/santeri/checkpoint/checkpoint/")
      .start()
      .awaitTermination()

      df.show()
  }
}
