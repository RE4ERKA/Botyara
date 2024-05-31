package me.re4erka.botyara.api.history.type;

import me.re4erka.botyara.api.history.History;

public class SimpleHistory extends History {
    public SimpleHistory(String name) {
        super(name);
    }

    public void log(String message) {
        super.log(message);
    }

    public void log(String message, Object... args) {
        this.log(
                String.format(message, args)
        );
    }

    public void logAwait(String message) {
        super.logAwait(message);
    }
}
