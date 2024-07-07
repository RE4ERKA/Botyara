package me.re4erka.botyara.api.history.type;

import me.re4erka.botyara.api.history.History;
import org.jetbrains.annotations.NotNull;

public class SimpleHistory extends History {
    public SimpleHistory(@NotNull String name) {
        super(name);
    }

    public void log(@NotNull String message) {
        super.log(message);
    }

    public void log(@NotNull String message, Object... args) {
        this.log(
                String.format(message, args)
        );
    }

    public void logAwait(@NotNull String message) {
        super.logAwait(message);
    }
}
