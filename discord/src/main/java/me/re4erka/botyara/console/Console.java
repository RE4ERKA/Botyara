package me.re4erka.botyara.console;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.log4j.Log4j2;
import me.re4erka.botyara.api.command.Command;
import me.re4erka.botyara.api.command.logger.Logger;
import me.re4erka.botyara.api.util.key.Key;
import me.re4erka.botyara.command.CommandManager;
import me.re4erka.botyara.console.logger.TerminalLogger;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jline.reader.*;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Log4j2
public class Console {
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final ExecutorService service = Executors.newSingleThreadExecutor(
            new ThreadFactoryBuilder()
                    .setNameFormat("Console-Input-Thread")
                    .setPriority(Thread.NORM_PRIORITY)
                    .setUncaughtExceptionHandler(
                            (t, e) -> log.error("There was an error while running the console thread!", e)
                    )
                    .setDaemon(false)
                    .build()
    );

    private final CommandManager commandManager;

    private final Terminal terminal;
    private final Logger logger;

    private final LineReader reader;

    private static final String READ_LINE_PREFIX = "> ";

    public Console(@NotNull CommandManager manager) throws IOException {
        this.commandManager = manager;

        this.terminal = TerminalBuilder.builder()
                .name("Botyara terminal")
                .build();
        this.logger = new TerminalLogger(terminal);

        final Set<String> commands = manager.getCommands()
                .stream()
                .map(Key::toLowerCase)
                .collect(Collectors.toUnmodifiableSet());

        final Completer completer = new StringsCompleter(commands);
        reader = LineReaderBuilder.builder()
                .appName("Botyara")
                .terminal(terminal)
                .completer(completer)
                .build();
    }

    public void run() {
        if (running.get()) {
            log.warn("The console thread has already started!");
            return;
        }

        service.execute(() -> {
            running.set(true);

            while (running.get()) {
                try {
                    final String line = reader.readLine(READ_LINE_PREFIX);
                    final String[] args = StringUtils.split(line, StringUtils.SPACE);

                    if (args.length == 0 || args[0].isEmpty()) {
                        continue;
                    }

                    final Key name = Key.create(args[0], true, true);
                    final Command command = commandManager.getCommand(name);
                    if (command != null) {
                        command.execute(
                                logger,
                                Arrays.copyOfRange(args, 1, args.length)
                        );
                    } else {
                        logger.warn("Неизвестная команда: " + args[0]);
                    }
                } catch (UserInterruptException | EndOfFileException e) {
                    break; // Прерывание или окончание ввода, выходим из цикла
                } catch (Exception e) {
                    log.error("Error when reading the console!", e);
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    public void shutdown() {
        if (running.compareAndSet(true, false)) {
            try {
                terminal.close();
            } catch (IOException e) {
                log.error("Error closing the terminal", e);
            }

            service.shutdown();

            try {
                if (!service.awaitTermination(5, TimeUnit.SECONDS)) {
                    log.warn("The console thread could not be terminated in time.");
                    service.shutdownNow();
                }
            } catch (InterruptedException exception) {
                log.error("Failed to terminate the console thread!", exception);
                Thread.currentThread().interrupt();
            }
        }
    }
}
