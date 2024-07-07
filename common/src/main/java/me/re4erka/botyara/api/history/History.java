package me.re4erka.botyara.api.history;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public abstract class History {
    protected final String name;

    protected History(@NotNull String name) {
        this.name = name;
    }

    protected void logAwait(@NotNull String message) {
        CompletableFuture.runAsync(() -> onLog(message)).join();
    }

    protected void log(@NotNull String message) {
        CompletableFuture.runAsync(() -> onLog(message));
    }

    private void onLog(@NotNull String message) {
        final String currentDate = HistoryManager.getCurrentDate();

        HistoryManager.writeLine(
                String.format("[%s] [%s] %s",
                        currentDate,
                        name,
                        message
                )
        );
    }
}
