# Apache spark integrations from Streamr

This repository contains different ways to integrate Streamr to Apache Spark.

Direct integrations from Streamr to Spark are done with Streamr's Java client for Java and Scala versions of Spark. Non-direct example integrations for Streamr-NiFi-Kafka-Spark-Kafka-NiFi-Streamr and a Streamr integration to Spark file streaming are also provided. The NiFi + Kafka integration might be useful for people who wish to either subscribe or publish data via NiFi and/or Kafka. The file streaming JS integration tool is useful for people who wish to do operations on historical data from Streamr and possibly store it for later use.

[Java Direct Integration](./java-spark)

[Scala Integrations](./scala-spark)

[JS file integration tool](./streamr-file-spark-streaming)
