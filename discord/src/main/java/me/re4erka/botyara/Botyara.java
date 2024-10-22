package me.re4erka.botyara;

import com.google.common.base.Stopwatch;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.re4erka.botyara.api.history.HistoryFactory;
import me.re4erka.botyara.api.history.type.SimpleHistory;
import me.re4erka.botyara.api.util.file.JarDirectory;
import me.re4erka.botyara.command.CommandManager;
import me.re4erka.botyara.database.DatabaseManager;
import me.re4erka.botyara.discord.DiscordManager;
import me.re4erka.botyara.executor.ScheduledExecutor;
import me.re4erka.botyara.file.FileManager;
import me.re4erka.botyara.file.type.Properties;
import org.javacord.api.util.logging.FallbackLoggerConfiguration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter @Log4j2
public enum Botyara {
    INSTANCE;

    private final FileManager fileManager = new FileManager();
    private final DatabaseManager databaseManager = new DatabaseManager();

    private final DiscordManager discordManager = new DiscordManager();

    private final CommandManager commandManager = new CommandManager();

    private final JarDirectory jarDirectory = new JarDirectory();

    private static final SimpleHistory history = HistoryFactory.createSimple("MainClass");

    /* Потокобезопастная переменная выключен ли бот.
    *
    * Для избежания повторных выключений.
    *  */
    private final AtomicBoolean shutdownInProgress = new AtomicBoolean(false);

    public static void main(String... args) {
        Botyara.INSTANCE.start();

        Runtime.getRuntime().addShutdownHook(
                new Thread(Botyara.INSTANCE::shutdown, "Shutdown-Thread")
        );
    }

    public void start() {
        log.info("Enabling...");

        final Stopwatch stopwatch = Stopwatch.createStarted();

        if (fileManager.start()) {
            final boolean debug = Properties.BOT_DEBUG.asBoolean();

            HistoryFactory.initialize(jarDirectory, debug);

            if (debug) {
                FallbackLoggerConfiguration.setDebug(true);
                log.info("Debug mode has been enabled!");
            }

            if (databaseManager.start()) {
                commandManager.start();

                if (discordManager.start()) {
                    stopwatch.stop();

                    log.info("The bot successfully launched in {}ms.", stopwatch.elapsed(TimeUnit.MILLISECONDS));
                    history.logAwait("Бот был включен.");

                    commandManager.runConsoleThread();
                } else {
                    shutdown();
                }
            }
        }
    }

    public void shutdown() {
        if (shutdownInProgress.compareAndSet(false, true)) {
            log.info("Shutdown...");

            ScheduledExecutor.shutdown();

            discordManager.stop();
            databaseManager.stop();
            commandManager.stop();

            log.info("Successfully shut down.");
            history.logAwait("Бот был выключен.");
        }
    }
}
