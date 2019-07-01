# Direct integration from Streamr to Spark with Java

Example integration directly from Streamr's Java client to Apache Spark Streaming.

Streamr's first version of the Java client requires some extra configuration with the dependencies before you can run the integration. You can use Maven Shade to get all the dependencies in a single .jar file that is easy to run but might lower your run time performance. Alternatively you can specify the required imports for Spark in your submit script.

```
spark-submit --master local[4] --class Streamrspark --packages com.streamr:client:1.1.0,org.apache.logging.log4j:log4j-core:2.9.0,org.apache.logging.log4j:log4j-api:2.9.0,org.apache.logging.log4j:log4j-slf4j-impl:2.9.0 target/Streamrspark-1.0-SNAPSHOT.jar
```

You might also need to add some of these dependencies to your pom.xml file for your IDE and Maven before packaging the .jar file.


If you shade the .jar file as is done in this example you can leave the --packages configuration out. You can also leave out most dependencies reuired by Streamr's client. See the pom.xml file of this project to see the required dependencies and plugins for the shaded .jar.

The package the .jar file with: 
```
mvn package
```
And then run Spark with:
```
spark-submit --master local[4] --class Streamrspark Streamrspark-1.0-SNAPSHOT.jar
```

If you do not shade the .jar you also need to specify some imports required by [Streamr's Java client](https://github.com/streamr-dev/streamr-client-java).

# Usage

This is how you integrate the StreamrCustomReceiver to Spark's executor. Streamr API key and Stream ID can be found in [Streamr's editor](https://www.streamr.com/core) after you have created an account and a stream.

``` java
 SparkConf conf = new SparkConf();
    conf.setAppName("Streamrspark");
    JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1));
    JavaDStream<String> streamrReceiverStream = 
    jssc.receiverStream(new StreamrCustomReceiver("YOUR_STREAMR_API_KEY","YOUR_STREAM_ID"));
```

The data is ingested in a String JSON format. In the example code you can see how to do some filtering of the data. However you propably should set up an SQLContext to process the data in JSON format instead of raw text. 

I would recommend using [Scala](../scala-spark) when playing around with the JSON data, and using Scala over Java with Apache Spark in general.
