package yating_asr;

import java.net.URI;
import java.util.List;

import yating_asr.asr.AsrResult;
import yating_asr.asr.Sentence;
import yating_asr.constants.Language;
import yating_asr.constants.Pipeline;

import yating_asr.service.MessageListener;
import yating_asr.service.StreamingClient;
import yating_asr.service.TokenClient;

public class App {
    public static void main(String[] args) {
        microphone();
    }

    public static void microphone() {
        String asrWebSocketUrl = "ASR_WEBSOCKET_ENDPOINT";
        String asrApiUrl = "ASR_ENDPOINT";
        String asrApiKey = "PUT_YOUR_API_KEY_HERE";

        String pipeline = Pipeline.General;
        String language = Language.ZHEN;

        AsrResult asrResult = new AsrResult();

        try {
            // Initialize token client, get auth token
            TokenClient tokenClient = new TokenClient(asrApiUrl, asrApiKey);
            String authToken = tokenClient.GetToken(pipeline, language);
            System.out.println("authToken: " + authToken);

            MessageListener messageListener = new MessageListener();
            messageListener.addAsrResult(asrResult);

            // Initialize streaming client, ready for asr recognization
            StreamingClient streamingClient = new StreamingClient(new URI(asrWebSocketUrl + "?token=" +
                    authToken), messageListener);
            streamingClient.connect();

            boolean scanning = true;
            while (scanning) {
                if (streamingClient.isOpen()) {
                    scanning = false;
                }
                Thread.sleep(100);
            }

            // Microphone audio recognization
            MicrophoneHandler microphoneHandler = new MicrophoneHandler();
            microphoneHandler.recognization(streamingClient);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    public static void file() {
        String asrWebSocketUrl = "ASR_WEBSOCKET_ENDPOINT";
        String asrApiUrl = "ASR_ENDPOINT";
        String asrApiKey = "PUT_YOUR_API_KEY_HERE";

        String filePath = "PUT_YOUR_FILE_PATH_HERE";
        String pipeline = Pipeline.General;
        String language = Language.ZHEN;

        AsrResult asrResult = new AsrResult();

        try {
            // Initialize token client, get auth token
            TokenClient tokenClient = new TokenClient(asrApiUrl, asrApiKey);
            String authToken = tokenClient.GetToken(pipeline, language);
            System.out.println("authToken: " + authToken);

            MessageListener messageListener = new MessageListener();
            messageListener.addAsrResult(asrResult);

            // Initialize streaming client, ready for asr recognization
            StreamingClient streamingClient = new StreamingClient(new URI(asrWebSocketUrl + "?token=" +
                    authToken), messageListener);
            streamingClient.connect();

            boolean scanning = true;
            while (scanning) {
                if (streamingClient.isOpen()) {
                    scanning = false;
                }
                Thread.sleep(100);
            }

            // File recognization
            FileHandler fileHandler = new FileHandler(filePath);
            fileHandler.recognization(streamingClient);

            // Print final result
            List<Sentence> sentenceList = asrResult.getSentences();
            sentenceList.forEach((s) -> System.out.println(s.getSentence()));
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}
