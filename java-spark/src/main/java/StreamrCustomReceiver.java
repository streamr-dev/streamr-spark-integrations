import com.streamr.client.options.SigningOptions;
import com.streamr.client.options.StreamrClientOptions;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.receiver.Receiver;
import com.streamr.client.StreamrClient;
import com.streamr.client.Subscription;
import com.streamr.client.authentication.ApiKeyAuthenticationMethod;
import com.streamr.client.authentication.AuthenticationMethod;
import com.streamr.client.MessageHandler;
import com.streamr.client.protocol.message_layer.StreamMessage;
import com.streamr.client.rest.Stream;

import java.io.IOException;

public class StreamrCustomReceiver extends Receiver<String>{

    String apiKey;
    String streamId;
    
    public StreamrCustomReceiver(String apiKey, String streamId) {
        super(StorageLevel.MEMORY_AND_DISK_2());

        this.apiKey = apiKey;
        this.streamId = streamId;
    }

    @Override
    public void onStart() {
        // Start the thread that receives data over a connection
        new Thread(this::receive).start();
    }

    @Override
    public void onStop() {
        // There is nothing much to do as the thread calling receive()
        // is designed to stop by itself if isStopped() returns false
    }

    /** Create a socket connection and receive data until receiver is stopped */
    private void receive() {

        try {
            // connect to the server
            StreamrClient client = new StreamrClient(new StreamrClientOptions(
                    new ApiKeyAuthenticationMethod(apiKey)
            ));
            try {
                Stream stream = client.getStream(streamId);
                Subscription sub = client.subscribe(stream, new MessageHandler() {
                    @Override
                    public void onMessage(Subscription s, StreamMessage message) {
                        // Here you can react to the latest messager
                        store(message.getSerializedContent());
                    }
                });
            } catch (IOException e) {
                restart("Trying to connect again");
            }

            // Restart in an attempt to connect again when server is active again
//            restart("Trying to connect again");
        } catch(Exception ce) {
            // restart if could not connect to server
            restart("Could not connect", ce);
        } catch(Throwable t) {
            // restart if there is any other error
            restart("Error receiving data", t);
        }
    }
}