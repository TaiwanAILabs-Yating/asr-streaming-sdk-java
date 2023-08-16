package yating_asr;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import yating_asr.service.StreamingClient;

public class FileHandler {
    String filePath;

    public FileHandler(String filePath) {
        this.filePath = filePath;
    }

    public void recognization(StreamingClient client) throws Exception {
        try {
            final Path audioFile = Paths.get(filePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(audioFile.toString()));
            AudioFormat audioFormat = audioInputStream.getFormat();

            int sampleRate = (int) audioFormat.getSampleRate();
            int sampleSizeInBits = audioFormat.getSampleSizeInBits();
            int channels = audioFormat.getChannels();
            System.out.println("sampleRate: " + sampleRate);
            System.out.println("sampleSizeInBits: " + sampleSizeInBits);
            System.out.println("channels: " + channels);

            if (sampleRate != 16000) {
                throw new Exception("sampleRate should be 16000");
            }
            if (sampleSizeInBits != 16) {
                throw new Exception("sampleSizeInBits should be 16");
            }
            if (channels != 1) {
                throw new Exception("channels should be 1");
            }

            System.out.println("reading from: " + audioFile);
            byte[] audioData = Files.readAllBytes(audioFile);

            System.out.println("read " + audioData.length + " bytes");

            int chunckSize = 1024;

            final int bytesPerMsec = sampleRate * sampleSizeInBits / 8 / 1000;
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
