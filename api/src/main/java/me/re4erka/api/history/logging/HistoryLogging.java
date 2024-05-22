package me.re4erka.api.history.logging;

@FunctionalInterface
public interface HistoryLogging<HistoryMessage, U> {
    void accept(HistoryMessage message, U u);
}
