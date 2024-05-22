package me.re4erka.discord.executor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutor {
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public static void executeLater(Runnable runnable, long delay, TimeUnit unit) {
        executor.schedule(runnable, delay, unit);
    }

    public static void shutdown() {
        executor.shutdownNow();
    }
}
