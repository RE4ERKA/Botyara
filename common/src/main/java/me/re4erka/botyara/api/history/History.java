package me.re4erka.botyara.api.history;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

public abstract class History {
    protected final String name;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");

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
        if (HistoryFactory.DEBUG) {
            return;
        }

        final String currentDate = FORMATTER.format(LocalDateTime.now());
        final String line = String.format("[%s] [%s] %s", currentDate, name, message);

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(HistoryFactory.HISTORY_FILE, StandardCharsets.UTF_8, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
