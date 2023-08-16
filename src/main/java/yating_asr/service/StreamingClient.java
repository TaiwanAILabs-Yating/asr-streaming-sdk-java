package yating_asr.service;

import java.net.URI;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class StreamingClient extends WebSocketClient {
  private MessageListener messageListener;

  public StreamingClient(URI serverURI, MessageListener messageListener) {
    super(serverURI);
    this.messageListener = messageListener;
  }

  @Override
  public void onOpen(ServerHandshake handshakedata) {
    System.out.println("### opened connection ###");
  }

  @Override
  public void onMessage(String message) {
    try {
      messageListener.onMessage(message);
    } catch (Exception e) {
      System.out.println("unknown message: " + e.getMessage());
      close();
    }
  }

  @Override
  public void onClose(int code, String reason, boolean remote) {
    System.out.println(
        "Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: "
            + reason);
  }

  @Override
  public void onError(Exception ex) {
    ex.printStackTrace();
  }
}