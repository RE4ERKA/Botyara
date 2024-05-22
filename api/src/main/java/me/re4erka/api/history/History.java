package me.re4erka.api.history;

import java.util.concurrent.CompletableFuture;

public abstract class History {
    protected final String name;

    protected History(String name) {
        this.name = name;
    }

    protected void logAwait(String message) {
        CompletableFuture.runAsync(() -> onLog(message)).join();
    }

    protected void log(String message) {
        CompletableFuture.runAsync(() -> onLog(message));
    }

    private void onLog(String message) {
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
