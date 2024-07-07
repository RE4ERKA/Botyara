package me.re4erka.botyara.api.history.logging;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class HistoryMessage {
    private String message;

    private HistoryMessage(@NotNull String message) {
        this.message = message;
    }

    public static HistoryMessage create(@NotNull String message) {
        return new HistoryMessage(message);
    }

    public HistoryMessage replace(@NotNull String search, @Nullable String replace) {
        this.message = StringUtils.replaceOnce(message,
                '%' + search + '%',
                replace == null ? "NULL" : replace
        );

        return this;
    }
}
