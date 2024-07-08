package me.re4erka.botyara.api.history;

import me.re4erka.botyara.api.history.type.ActivityHistory;
import me.re4erka.botyara.api.history.type.SimpleHistory;
import me.re4erka.botyara.api.history.type.UserHistory;
import me.re4erka.botyara.api.util.file.JarDirectory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HistoryFactory {
    public static final String HISTORY_FILE =
            "history/" + DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()) + ".log";

    public static boolean DEBUG;

    public static void init(@NotNull JarDirectory directory, boolean debug) {
        if (!debug && directory.notExists(HISTORY_FILE)) {
            try {
                directory.newFile(HISTORY_FILE).createNewCustomFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        DEBUG = debug;
    }

    public static SimpleHistory createSimple(@NotNull String name) {
        return new SimpleHistory(name);
    }

    public static ActivityHistory createActivity(@NotNull String name, @NotNull String message) {
        return new ActivityHistory(name, message);
    }

    public static UserHistory createUser(@NotNull String name) {
        return new UserHistory(name);
    }
}
