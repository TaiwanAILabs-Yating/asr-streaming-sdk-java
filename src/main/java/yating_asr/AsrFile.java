package yating_asr;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class AsrFile {
    String filePath;

    public AsrFile(String filePath, String pipeline, String language) {
        this.filePath = filePath;
    }

    public void recognization(AsrStreamingClient client) throws Exception {
        try {
            final Path audioFile = Paths.get(filePath);
            System.out.println("reading from: " + audioFile);
            byte[] audioData = Files.readAllBytes(audioFile);

            System.out.println("read " + audioData.length + " bytes");

            int rate = 16000;
            int bytesPerSample = 2;
            int chunckSize = 2048;

            final int bytesPerMsec = (bytesPerSample * rate) / 1000;
            System.out.println("bytesPerMsec: " + bytesPerMsec);

            long count = 0;
            long timeStart = System.currentTimeMillis();

            // Send audio over websocket at approximately real-time rate
            int i = 0;
            for (; i < audioData.length; i += chunckSize) {
                int start = i;
                int end = i + chunckSize;

                if (i + chunckSize > audioData.length) {
                    end = audioData.length;
                }

                client.send(Arrays.copyOfRange(audioData, start, end));
                count += chunckSize;
                long expectedElapsed = count / bytesPerMsec;
                long elapsed = System.currentTimeMillis() - timeStart;
                long diff = expectedElapsed - elapsed;
                if (diff > 0) {
                    Thread.sleep(diff);
                }
            }

            Thread.sleep(5000);
            client.close();
        } catch (Exception e) {
            throw e;
        }
    }
}
