package me.re4erka.botyara.api.history.logging;

@FunctionalInterface
public interface HistoryLogging<HistoryMessage, U> {
    void accept(HistoryMessage message, U u);
}
