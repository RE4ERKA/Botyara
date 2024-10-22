package me.re4erka.botyara.command;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.extern.log4j.Log4j2;
import me.re4erka.botyara.api.command.Command;
import me.re4erka.botyara.api.manager.Manager;
import me.re4erka.botyara.api.util.key.Key;
import me.re4erka.botyara.command.types.*;
import me.re4erka.botyara.console.Console;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

@Log4j2
public class CommandManager extends Manager {
    private final ImmutableMap<Key, Command> commands = new ImmutableMap.Builder<Key, Command>()
            .put(Key.of("STOP"), new StopCommand())
            .put(Key.of("HELP"), new HelpCommand())
            .put(Key.of("USERS"), new UsersCommand())
            .put(Key.of("ACTIVITY"), new ActivityCommand())
            .put(Key.of("LISTENERS"), new ListenersCommand())
            .build();

    private Console console;

    @Override
    public boolean start() {
        log.info("Console startup...");
        try {
            console = new Console(this);
        } catch (IOException e) {
            log.error("Error when initializing the console!", e);
            return false;
        }

        return true;
    }

    @Override
    public void stop() {
        if (console != null) {
            console.shutdown();
        }
    }

    public void runConsoleThread() {
        console.run();
    }

    public ImmutableSet<Key> getCommands() {
        return commands.keySet();
    }

    @Nullable
    public Command getCommand(@NotNull Key name) {
        return commands.get(name);
    }
}
