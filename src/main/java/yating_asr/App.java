package yating_asr;

import java.net.URI;

public class App {
    public static void main(String[] args) {
        file();
    }

    public static void file() {
        String asrWebSocketUrl = "ASR_WEBSOCKET_ENDPOINT";
        String asrApiUrl = "ASR_ENDPOINT";
        String asrApiKey = "PUT_YOUR_API_KEY_HERE";

        String filePath = "PUT_YOUR_FILE_PATH_HERE";
        String pipeline = Pipeline.General;
        String language = Language.ZHEN_OFFLINE;

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
            AsrFile asrFile = new AsrFile(filePath, pipeline, language);
            asrFile.recognization(asrStreamingClient);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}
