package me.re4erka.botyara.command;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.log4j.Log4j2;
import me.re4erka.botyara.api.command.Command;
import me.re4erka.botyara.api.manager.Manager;
import me.re4erka.botyara.api.util.key.Key;
import me.re4erka.botyara.command.types.*;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

// TODO: Переписать CommandManager для работы через библиотеку JLine
@Log4j2
public class CommandManager extends Manager {
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

    private final ImmutableMap<Key, Command> commands = new ImmutableMap.Builder<Key, Command>()
            .put(Key.of("STOP"), new StopCommand())
            .put(Key.of("HELP"), new HelpCommand())
            .put(Key.of("USERS"), new UsersCommand())
            .put(Key.of("LISTENERS"), new ListenersCommand())
            .build();

    @Override
    public boolean start() {
        if (running.get()) {
            log.warn("The console thread has already started!");
            return false;
        }

        service.execute(() -> {
            running.set(true);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
                /*
                * Вероятнее всего метод reader.readLine() будет блокировать выполнение цикличное выполнение
                * цикла из-за того, что условие (running.get()) цикла не будет проверяться постоянно.
                *  */
                while (running.get()) {
                    final String[] message = StringUtils.split(reader.readLine(), ' ');
                    final Command command = commands.getOrDefault(
                            Key.create(message[0], true, true),
                            args -> System.out.println("Неизвестная команда!")
                    );

                    if (command != null) {
                        command.execute(
                                Arrays.copyOfRange(message, 1, message.length)
                        );
                    }
                }
            } catch (IOException exception) {
                log.error("Error when reading the console!", exception);
                Thread.currentThread().interrupt();
            }
        });

        return true;
    }

    @Override
    public void stop() {
        if (running.compareAndSet(true, false)) {
            service.shutdownNow();

            // Так как цикл не проверяется постоянно, то код ниже не будет работать корректно.
//          service.shutdown();
//
//            try {
//                if (!service.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
//                    log.warn("The console thread could not be terminated in time.");
//                    service.shutdownNow();
//                }
//            } catch (InterruptedException exception) {
//                log.error("Failed to terminate the console thread!", exception);
//                Thread.currentThread().interrupt();  // Восстановление флага прерывания
//            }
        }
    }

    public ImmutableSet<Key> getCommands() {
        return commands.keySet();
    }
}
