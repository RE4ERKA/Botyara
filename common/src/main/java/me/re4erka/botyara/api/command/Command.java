package me.re4erka.botyara.api.command;

import org.jetbrains.annotations.NotNull;

public interface Command {
    void execute(@NotNull String[] args);

    default void info(@NotNull String message) {
        System.out.println(message);
    }

    default void info(@NotNull String message, Object... args) {
        System.out.printf((message) + "%n", args);
    }
}
