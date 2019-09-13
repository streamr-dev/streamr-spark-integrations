# Apache spark integrations from Streamr

You can find the maven library for the integration here [here](https://github.com/streamr-dev/streamr-spark)
You can import it to your maven project easily with:

```
<dependency>
  <groupId>com.streamr.labs</groupId>
  <artifactId>streamr_spark</artifactId>
  <version>0.2</version>
</dependency>
```

This repository contains different ways to integrate Streamr to Apache Spark.

Direct integrations from Streamr to Spark are done with Streamr's Java client for Java and Scala versions of Spark. Non-direct example integrations for Streamr-NiFi-Kafka-Spark-Kafka-NiFi-Streamr and a Streamr integration to Spark file streaming are also provided. The NiFi + Kafka integration might be useful for people who wish to either subscribe or publish data via NiFi and/or Kafka. The file streaming JS integration tool is useful for people who wish to do operations on historical data from Streamr and possibly store it for later use.

The direct integrations use the [Helsinki Tram Stream](https://www.streamr.com/marketplace/products/31e8df5243ce49cfa250002f60b73e475f39b44723ca4fbcbf695198d19c6b08) as their data input.

[Java Direct Integration](./java-spark)

[Scala Integrations](./scala-spark)

[JS file integration tool](./streamr-file-spark-streaming)
