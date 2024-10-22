package me.re4erka.botyara.api.command;

import me.re4erka.botyara.api.command.logger.Logger;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Command {
    void execute(@NotNull Logger logger, @NotNull String[] args);
}
