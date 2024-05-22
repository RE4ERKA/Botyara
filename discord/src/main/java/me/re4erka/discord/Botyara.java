package me.re4erka.discord;

import com.google.common.base.Stopwatch;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.re4erka.api.history.HistoryManager;
import me.re4erka.api.history.type.SimpleHistory;
import me.re4erka.api.util.file.JarDirectory;
import me.re4erka.api.util.key.Key;
import me.re4erka.discord.bot.BotManager;
import me.re4erka.discord.command.CommandManager;
import me.re4erka.discord.database.DatabaseManager;
import me.re4erka.discord.executor.ScheduledExecutor;
import me.re4erka.discord.file.FileManager;
import sun.misc.Signal;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Getter @Log4j2
public enum Botyara {
    INSTANCE;

    private final FileManager fileManager = new FileManager();
    private final DatabaseManager databaseManager = new DatabaseManager();

    private final BotManager botManager = new BotManager();

    private final CommandManager commandManager = new CommandManager();

    private final JarDirectory jarDirectory = new JarDirectory();

    private static final SimpleHistory history = HistoryManager.newSimple("MainClass");

    public static void main(String... args) {
        Botyara.INSTANCE.onEnable();

        Signal.handle(new Signal("INT"), sig -> {
            System.out.println("It is not recommended to terminate the process via the 'CTRL + C' combination!");
            System.out.println("Instead, it is better to use the command - /stop");

            Botyara.INSTANCE.onDisable();
        });
    }

    public void onEnable() {
        HistoryManager.init(jarDirectory);

        log.info("Enabling...");

        final Stopwatch stopwatch = Stopwatch.createStarted();

        fileManager.start();

        databaseManager.start();
        commandManager.start();

        if (botManager.start()) {
            stopwatch.stop();

            log.info("The bot successfully launched in {}ms.", stopwatch.elapsed(TimeUnit.MILLISECONDS));
            history.logAwait("Бот был включен.");
        } else {
            onDisable();
        }
    }

    public void onDisable() {
        log.info("Shutdown...");

        ScheduledExecutor.shutdown();

        botManager.stop();
        databaseManager.stop();
        commandManager.stop();

        log.info("Successfully shut down.");
        history.logAwait("Бот был выключен.");
    }
}
