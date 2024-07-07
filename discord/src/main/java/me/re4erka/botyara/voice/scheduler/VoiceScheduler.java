package me.re4erka.botyara.voice.scheduler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.log4j.Log4j2;
import me.re4erka.botyara.voice.VoiceManager;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Log4j2
public class VoiceScheduler {
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(
            new ThreadFactoryBuilder()
                    .setNameFormat("Voice-Scheduler-Thread")
                    .setPriority(Thread.NORM_PRIORITY)
                    .setDaemon(false)
                    .build()
    );
    private final VoiceManager voiceManager;

    public VoiceScheduler(@NotNull VoiceManager voiceManager) {
        this.voiceManager = voiceManager;
    }

    public void start() {
        executor.scheduleAtFixedRate(() -> {
            try {
                if (voiceManager.shouldDisconnect()) {
                    voiceManager.disconnect();
                }
            } catch (Exception e) {
                log.error("Error in scheduler task", e);
                stop();
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    public void stop() {
        executor.shutdown();
    }
}
