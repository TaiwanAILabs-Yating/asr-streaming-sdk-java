package yating_asr;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import yating_asr.service.StreamingClient;

public class MicrophoneHandler {
    static float sampleRate = 16000;
    static int sampleSizeInBits = 16;
    static int channels = 1;

    static AudioFormat getAudioFormat() {
        boolean signed = true;
        boolean bigEndian = false;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                channels, signed, bigEndian);
        return format;
    }

    public MicrophoneHandler() {

    }

    public void recognization(StreamingClient asrStreamingClient) throws Exception {
        AudioFormat format = getAudioFormat();
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        // checks if system supports the data line
        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Line not supported");
            System.exit(0);
        }
        TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);

        line.open(format);
        line.start(); // start capturing

        System.out.println("Start capturing...");

        AudioInputStream ais = new AudioInputStream(line);

        System.out.println("Start recording...");

        System.out.println("sampleRate: " + sampleRate);
        System.out.println("sampleSizeInBits: " + sampleSizeInBits);
        System.out.println("channels: " + channels);

        final int bytesPerMsec = (int) sampleRate * sampleSizeInBits / 8 / 1000;
        System.out.println("bytesPerMsec: " + bytesPerMsec);

        while (true) {
            Thread.sleep(100);
            byte[] data = new byte[ais.available()];
            ais.read(data, 0, data.length);
            asrStreamingClient.send(data);
        }
    }
}
