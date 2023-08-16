package yating_asr;

import java.net.URI;

import yating_asr.asr.StreamingClient;
import yating_asr.asr.TokenClient;
import yating_asr.asr.Language;
import yating_asr.asr.Pipeline;

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

        try {
            // Initialize token client, get auth token
            TokenClient tokenClient = new TokenClient(asrApiUrl, asrApiKey);
            String authToken = tokenClient.GetToken(pipeline, language);
            System.out.println("authToken: " + authToken);

            // Initialize streaming client, ready for asr recognization
            StreamingClient streamingClient = new StreamingClient(new URI(asrWebSocketUrl + "?token=" +
                    authToken));
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

        try {
            // Initialize token client, get auth token
            TokenClient tokenClient = new TokenClient(asrApiUrl, asrApiKey);
            String authToken = tokenClient.GetToken(pipeline, language);
            System.out.println("authToken: " + authToken);

            // Initialize streaming client, ready for asr recognization
            StreamingClient streamingClient = new StreamingClient(new URI(asrWebSocketUrl + "?token=" +
                    authToken));
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
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}
