package me.re4erka.botyara.api.history.logging;

import org.apache.commons.lang3.StringUtils;

public class HistoryMessage {
    private String message;

    public HistoryMessage(String message) {
        this.message = message == null ? "" : message;
    }

    public static HistoryMessage create(String message) {
        return new HistoryMessage(message);
    }

    public HistoryMessage replace(String search, String replace) {
        this.message = StringUtils.replaceOnce(message,
                '%' + search + '%',
                replace == null ? "NULL" : replace
        );

        return this;
    }

    public String get() {
        return message;
    }
}
