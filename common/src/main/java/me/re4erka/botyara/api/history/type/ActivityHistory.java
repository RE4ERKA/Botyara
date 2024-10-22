package me.re4erka.botyara.api.history.type;

import me.re4erka.botyara.api.bot.activity.Activity;
import me.re4erka.botyara.api.history.History;
import me.re4erka.botyara.api.history.logging.HistoryMessage;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class ActivityHistory extends History {
    private final String message;

    public ActivityHistory(@NotNull String name, @NotNull String message) {
        super(name);
        this.message = message;
    }

    public void logNothing() {
        super.log(
                HistoryMessage.create(message)
                        .replace("activity_type", Activity.Type.NOTHING.toString())
                        .replace("activity_text", "empty")
                        .getMessage()
        );
    }

    public void log(@NotNull Activity.Type type, @NotNull String text) {
        super.log(
                HistoryMessage.create(message)
                        .replace("activity_type", type.toString())
                        .replace("activity_text", text)
                        .getMessage()
        );
    }
}
