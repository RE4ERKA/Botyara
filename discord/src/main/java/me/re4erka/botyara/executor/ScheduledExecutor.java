package me.re4erka.botyara.executor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Log4j2
public class ScheduledExecutor {
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(
            new ThreadFactoryBuilder()
                    .setNameFormat("Schedule-Executor-Thread")
                    .setPriority(Thread.NORM_PRIORITY)
                    .setUncaughtExceptionHandler(
                            (t, e) -> log.error("An error occurred in thread {}: {}", t.getName(), e.getMessage(), e)
                    )
                    .setDaemon(false)
                    .build()
    );

    public static void executeLater(Runnable runnable, long milliseconds) {
        try {
            executor.schedule(runnable, milliseconds, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("Failed to schedule task: {}", e.getMessage(), e);
        }
    }

    public static void shutdown() {
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
