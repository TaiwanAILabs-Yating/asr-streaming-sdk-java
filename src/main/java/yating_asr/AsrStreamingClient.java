package yating_asr;

import java.net.URI;
import java.util.Map;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class AsrStreamingClient extends WebSocketClient {

  public AsrStreamingClient(URI serverUri, Draft draft) {
    super(serverUri, draft);
  }

  public AsrStreamingClient(URI serverURI) {
    super(serverURI);
  }

  public AsrStreamingClient(URI serverUri, Map<String, String> httpHeaders) {
    super(serverUri, httpHeaders);
  }

  @Override
  public void onOpen(ServerHandshake handshakedata) {
    System.out.println("### opened connection ###");
  }

  @Override
  public void onMessage(String message) {
    try {
      JSONParser parser = new JSONParser();
      JSONObject messageResponse = (JSONObject) parser.parse(message);

      // Handle open message
      String status = "";
      if (messageResponse.containsKey("status")) {
        status = (String) messageResponse.get("status");
      }
      if (status.equals("ok")) {
        System.out.println("### start stream ###");
        return;
      } else if (!status.equals("")) {
        System.out.println("### close stream ###");
        close();
        return;
      }

      // Handle asr result
      if (messageResponse.containsKey("pipe")) {
        JSONObject pipeObject = (JSONObject) messageResponse.get("pipe");

        if (pipeObject.containsKey("asr_final")) {
          Boolean asrFinal = (Boolean) pipeObject.get("asr_final");

          if (asrFinal) {
            Sentence sentence = new Sentence(pipeObject);
            System.out.println("[" + sentence.getStart() + ":" + sentence.getEnd() + "] " + sentence.getSentence());
          }
        }
        return;
      }

      throw new Error("recieve unknown messaage");
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