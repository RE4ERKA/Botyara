package me.re4erka.api.history;

import me.re4erka.api.bot.Bot;
import me.re4erka.api.history.type.BotHistory;
import me.re4erka.api.history.type.SimpleHistory;
import me.re4erka.api.history.type.UserHistory;
import me.re4erka.api.util.file.JarDirectory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistoryManager {
    private static final String HISTORY_FILE = "history/" + DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()) + ".log";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");

    public static void init(JarDirectory directory) {
        if (directory.notExists(HISTORY_FILE)) {
            try {
                directory.newFile(HISTORY_FILE).createNewCustomFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static SimpleHistory newSimple(String name) {
        return new SimpleHistory(name);
    }

    public static UserHistory newUser(String name) {
        return new UserHistory(name);
    }

    public static BotHistory newBot(String name, Bot bot) {
        return new BotHistory(name, bot);
    }

    public static void writeLine(String line) {
        try (final BufferedWriter writer = new BufferedWriter(
                new FileWriter(HISTORY_FILE, StandardCharsets.UTF_8, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getCurrentDate() {
        return FORMATTER.format(
                LocalDateTime.now()
        );
    }
}
