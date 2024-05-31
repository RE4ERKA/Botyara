package me.re4erka.botyara.api.command;

public interface Command {
    boolean execute(String[] args);

    default void info(String message) {
        System.out.println(message);
    }

    default void info(String message, Object... args) {
        System.out.printf((message) + "%n", args);
    }
}
