package me.re4erka.botyara.voice.source;

import org.javacord.api.DiscordApi;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.audio.AudioSourceBase;

public class SpeechAudioSource extends AudioSourceBase {
    private final byte[] audioData;
    private int readIndex = 0;

    private static final short CHUNK_SIZE = 3840; // Оптимальный размер кадра для Discord (20 мс аудио при 48 кГц, 16 бит, стерео)

    public SpeechAudioSource(DiscordApi api, byte[] audioData) {
        super(api);
        this.audioData = audioData;
    }

    @Override
    public byte[] getNextFrame() {
        if (readIndex >= audioData.length) {
            return null;
        }

        final byte[] chunk = new byte[Math.min(CHUNK_SIZE, audioData.length - readIndex)];

        System.arraycopy(audioData, readIndex, chunk, 0, chunk.length);
        readIndex += chunk.length;

        return chunk;
    }

    @Override
    public boolean hasNextFrame() {
        return audioData.length >= readIndex;
    }

    @Override
    public AudioSource copy() {
        return null;
    }
}
