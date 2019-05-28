import org.apache.spark.*;

import org.apache.spark.api.java.function.*;

import org.apache.spark.streaming.*;
import org.apache.spark.streaming.api.java.*;

    public class Streamrspark {

        public static void main(String[] args) {
        SparkConf conf = new SparkConf();
        conf.setAppName("Streamrspark");
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1));
        JavaDStream<String> streamrReceiverStream = jssc.receiverStream(new StreamrCustomReceiver("YOUR_STREAMR_API_KEY","YOUR_STREAM_ID"));

        JavaDStream<String> filtered = streamrReceiverStream.filter(new Function<String, Boolean>() {
            @Override
            public Boolean call(String s) throws Exception {
                return s.contains("6T");
            }
        });
        filtered.count().print();

        jssc.start();
        try {
            jssc.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
