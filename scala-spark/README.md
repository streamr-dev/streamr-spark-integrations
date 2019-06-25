# Streamr integrations with Scala to Spark SQL / Structured streaming via kafka and file streaming

## Direct integrations
The current example is done with the first version of Streamr's client so we have to work around with some issues still present. Firstly, SBT is unable to download the dependency from maven so you have to make a /lib directory and put the Streamr client's .jar file inside it. The .jar file can be found inside this project's /lib directory. For some IDE's such as Intellij IDEA you also need to specify .jar dependency in the project settings.

You also have to do imports for some of the dependencies required by Streamr. you can find the required dependencies in the build.sbt file.

Before running spark you first need to create a .jar file of the project with:

```
sbt package
```

Even though Apache Spark is now able to resolve most of the dependencies, you still need to tell Spark to load a few dependencies seen in the start up script:

```
spark-submit --master local[4] --class StreamrSparkStreamingSQL --packages com.streamr:client:1.1.0,org.apache.logging.log4j:log4j-core:2.9.0,org.apache.logging.log4j:log4j-api:2.9.0,org.apache.logging.log4j:log4j-slf4j-impl:2.9.0 target/scala-2.11/scala-spark_2.11-0.1.jar
```

Alternatively you could set up assembly for your project to create a large .jar file of the project. However the large .jar file might reduce your run time performance, where as the manual imports only slow down your start up time.

## Non-direct integrations

This repository also includes some esample integrations for setting up non-direct integrations to Streamr.

You can use the [Node.js integration tool](../streamr-file-spark-streaming) to pull historical data from a stream. For kafka you can set up an integration script from Streamr to Kafka or use NiFi to create a Streamr - NiFi - Kafka - Spark - and/or back pipeline.

## Node-js file streaming integration

Check out the [Node.js integration tool](../streamr-file-spark-streaming) before using this integration. In the following snippet you can see what the /tmp, /checkpoint and /res directories are used for in the node.js script. You should point the directories to the integration scripts directory.

``` scala
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
```

The tmp directory is where the integration script pushes new json files after a specified amount of inputs is received. The checkpoint directory is used by Spark as a checkpoint before outputting. Finally the res directory contains the final outputs of Spark.

### Running
First start up the node.js integration script. After this you should spark-submit the `StreamrSparkFileStreaming` class as follows:

```
sbt package
```

```
spark-submit --master local[4] --class StreamrSparkFileStreaming target/scala-2.11/scala-spark_2.11-0.1.jar
````

If you are not getting json files in the integration scripts /tmp directory, but spark is running succesfully, you can go back to the integration script and lower the amount of json lines in a single file. 

If you pull historical data and wish to use it again after the integration script has been stopped, you can just clear the integration scripts `checkpoint` and `res` directories and the run spark again on the data stored in the `tmp` directory. Restarting the integration script will clear the json files in the tmp directory.

## Streamr - NiFi - Kafka - Spark

You can also use Apache NiFi and Kafka to set up a pipeline from streamr with Streamr's NiFi processors. With Kafka and NiFi you are able to subscribe and publish to data from Streamr. The NiFi processors and a guide on how to use them can be found [here](https://github.com/streamr-dev/streamr-nifi). 

After you have installed [NiFi](https://nifi.apache.org/) + Streamr processors you should install and set up [Apache Kafka](https://kafka.apache.org/) you should import the nififlow.xml file to Apache NiFi. From there you should configure the Streamr and Kafka processors. After the processors are configured you should configure the spark main class to subscribe and publish to the streams and topics you have set up. See the [KafkaStreamrSpark](./src/main/scala/KafkaStreamrSpark.scala) class for an example.

After everything is set up and you can try to start up spark with first:
```
sbt package
```

and then

```
spark-submit --master local[4] --class KafkaStreamrSpark --packages org.apache.spark:spark-sql-kafka-0-10:2.4.1 target/scala-2.11/scala-spark_2.11-0.1.jar
```

Now you can go to your NiFi flow to see if the data is flowing in and out from Streamr.