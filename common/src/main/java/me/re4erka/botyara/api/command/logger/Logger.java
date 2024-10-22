package me.re4erka.botyara.api.command.logger;

import org.jetbrains.annotations.NotNull;

public interface Logger {
    void info(@NotNull String message);
    void info(@NotNull String message, Object... params);

    void warn(@NotNull String message);
    void warn(@NotNull String message, Object... params);

    void error(@NotNull String message);
    void error(@NotNull String message, Object... params);
}
