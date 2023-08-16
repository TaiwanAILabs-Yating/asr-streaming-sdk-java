package yating_asr;

import java.net.URI;

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
            AsrTokenClient client = new AsrTokenClient(asrApiUrl, asrApiKey);
            String authToken = client.GetToken(pipeline, language);
            System.out.println("authToken: " + authToken);

            // Initialize streaming client, ready for asr recognization
            AsrStreamingClient asrStreamingClient = new AsrStreamingClient(new URI(asrWebSocketUrl + "?token=" +
                    authToken));
            asrStreamingClient.connect();

            boolean scanning = true;
            while (scanning) {
                if (asrStreamingClient.isOpen()) {
                    scanning = false;
                }
                Thread.sleep(100);
            }

            // Microphone audio recognization
            MicrophoneHandler microphoneHandler = new MicrophoneHandler();
            microphoneHandler.recognization(asrStreamingClient);
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
            AsrTokenClient client = new AsrTokenClient(asrApiUrl, asrApiKey);
            String authToken = client.GetToken(pipeline, language);
            System.out.println("authToken: " + authToken);

            // Initialize streaming client, ready for asr recognization
            AsrStreamingClient asrStreamingClient = new AsrStreamingClient(new URI(asrWebSocketUrl + "?token=" +
                    authToken));
            asrStreamingClient.connect();

            boolean scanning = true;
            while (scanning) {
                if (asrStreamingClient.isOpen()) {
                    scanning = false;
                }
                Thread.sleep(100);
            }

            // File recognization
            FileHandler fileHandler = new FileHandler(filePath);
            fileHandler.recognization(asrStreamingClient);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}
