package yating_asr.service;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import yating_asr.asr.AsrResult;
import yating_asr.asr.Sentence;

public class MessageListener {
    AsrResult asrResult;

    public void addAsrResult(AsrResult asrResult) {
        this.asrResult = asrResult;
    }

    public void onMessage(String message) throws Exception {
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
            throw new Exception("recieve unknown messaage");
        }

        // Handle asr result
        if (messageResponse.containsKey("pipe")) {
            JSONObject pipeObject = (JSONObject) messageResponse.get("pipe");

            if (pipeObject.containsKey("asr_final")) {
                Boolean asrFinal = (Boolean) pipeObject.get("asr_final");

                if (asrFinal) {
                    asrResult.addSentence(new Sentence(pipeObject));
                }
            }
            return;
        }

        throw new Exception("recieve unknown messaage");
    }
}
