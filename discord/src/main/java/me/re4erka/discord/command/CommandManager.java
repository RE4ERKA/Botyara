package me.re4erka.discord.command;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.extern.log4j.Log4j2;
import me.re4erka.api.command.Command;
import me.re4erka.api.manager.Manager;
import me.re4erka.discord.command.types.*;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Log4j2
public class CommandManager extends Manager {
    private final ExecutorService service = Executors.newFixedThreadPool(1, runnable -> {
        final Thread thread = new Thread(runnable);

        thread.setName("Botyara-Console-Thread");
        thread.setPriority(Thread.NORM_PRIORITY);
        thread.setDaemon(false);

        thread.setUncaughtExceptionHandler(
                (t, e) -> log.error("There was an error while running the console thread!", e)
        );

        return thread;
    });

    private final ImmutableMap<String, Command> commands = new ImmutableMap.Builder<String, Command>()
            .put("STOP", new StopCommand())
            .put("HELP", new HelpCommand())
            .put("USERS", new UsersCommand())
            .put("LISTENERS", new ListenersCommand())
            .build();

    @Override
    public boolean start() {
        service.execute(() -> {
            AtomicBoolean isStop = new AtomicBoolean(false);

            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
                while (!isStop.get()) {
                    final String[] message = StringUtils.split(reader.readLine(), ' ');
                    final Command command = commands.getOrDefault(message[0].toUpperCase(), args -> {
                        System.out.println("Неизвестная команда!");
                        return false;
                    });

                    isStop.set(
                        Objects.requireNonNull(command).execute(
                                Arrays.copyOfRange(message, 1, message.length)
                        )
                    );
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return true;
    }

    @Override
    public void stop() {
        service.shutdownNow();
    }

    public ImmutableSet<String> getCommands() {
        return commands.keySet();
    }
}
