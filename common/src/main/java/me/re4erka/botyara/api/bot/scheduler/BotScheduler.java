package me.re4erka.botyara.api.bot.scheduler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.log4j.Log4j2;
import me.re4erka.botyara.api.bot.Bot;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Log4j2
public abstract class BotScheduler {
    protected final ScheduledExecutorService executor;
    protected final Bot bot;

    protected BotScheduler(@NotNull String name, @NotNull Bot bot) {
        this.executor = Executors.newSingleThreadScheduledExecutor(
                new ThreadFactoryBuilder()
                        .setNameFormat(String.format("%s-Scheduler-Thread", name))
                        .setPriority(Thread.NORM_PRIORITY)
                        .setDaemon(false)
                        .build()
        );
        this.bot = bot;
    }

    public abstract CompletableFuture<Void> update();

    public void shutdown() {
        try {
            executor.shutdown();

            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                log.warn("Executor did not terminate in the specified time.");

                executor.shutdownNow();
                if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                    log.error("Executor did not terminate after shutdownNow.");
                }
            }
        } catch (InterruptedException e) {
            log.error("Shutdown interrupted: {}", e.getMessage(), e);

            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
