import com.streamr.client.options.SigningOptions
import com.streamr.client.options.StreamrClientOptions
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming._
import org.apache.spark.streaming.receiver.Receiver
import com.streamr.client.StreamrClient
import com.streamr.client.Subscription
import com.streamr.client.authentication.ApiKeyAuthenticationMethod
import com.streamr.client.MessageHandler
import com.streamr.client.protocol.message_layer.StreamMessage
import com.streamr.client.rest.Stream
import java.io.IOException

import org.apache.spark.internal.Logging


class StreamrCustomReceiver(apiKey: String, streamId: String)
  extends Receiver[String](StorageLevel.MEMORY_AND_DISK_2) with Logging {

  def onStart() {
    // Start the thread that receives data over a connection
    new Thread("Streamr Receiver") {
      override def run() { receive() }
    }.start()
  }

  def onStop() {
    // There is nothing much to do as the thread calling receive()
    // is designed to stop by itself if isStopped() returns false
  }

  /** Create a socket connection and receive data until receiver is stopped */
  private def receive() {
    val client = new StreamrClient(new StreamrClientOptions(new ApiKeyAuthenticationMethod(apiKey)))
    try {
      val stream = client.getStream(streamId)
      val sub = client.subscribe(stream, new MessageHandler() {
        override def onMessage(s: Subscription, message: StreamMessage): Unit = { // Here you can react to the latest messager
          store(message.getSerializedContent)
        }
      })
    } catch {
      case e: IOException =>
        restart("Trying to connect again")
      case f: Exception =>
        restart("Could not connect")
      case g: Throwable =>
        restart("Error receiving data")
    }

  }
}