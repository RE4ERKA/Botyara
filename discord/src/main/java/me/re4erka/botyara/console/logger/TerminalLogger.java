package me.re4erka.botyara.console.logger;

import lombok.extern.log4j.Log4j2;
import me.re4erka.botyara.api.command.logger.Logger;
import org.jetbrains.annotations.NotNull;
import org.jline.terminal.Terminal;

@Log4j2
public class TerminalLogger implements Logger {
    private final Terminal terminal;

    public TerminalLogger(@NotNull Terminal terminal) {
        this.terminal = terminal;
    }

    @Override
    public void info(@NotNull String message) {
        synchronized (terminal) {
            log.info(message);
            terminal.flush();
        }
    }

    @Override
    public void info(@NotNull String message, Object... params) {
        synchronized (terminal) {
            log.info(message, params);
            terminal.flush();
        }
    }

    @Override
    public void warn(@NotNull String message) {
        synchronized (terminal) {
            log.warn(message);
            terminal.flush();
        }
    }

    @Override
    public void warn(@NotNull String message, Object... params) {
        synchronized (terminal) {
            log.warn(message, params);
            terminal.flush();
        }
    }

    @Override
    public void error(@NotNull String message) {
        synchronized (terminal) {
            log.error(message);
            terminal.flush();
        }
    }

    @Override
    public void error(@NotNull String message, Object... params) {
        synchronized (terminal) {
            log.error(message, params);
            terminal.flush();
        }
    }
}
