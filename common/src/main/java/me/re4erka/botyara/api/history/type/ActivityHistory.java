package me.re4erka.botyara.api.history.type;

import me.re4erka.botyara.api.bot.activity.Activity;
import me.re4erka.botyara.api.history.History;
import me.re4erka.botyara.api.history.logging.HistoryMessage;
import org.jetbrains.annotations.NotNull;

public class ActivityHistory extends History {
    private final String message;

    public ActivityHistory(@NotNull String name, @NotNull String message) {
        super(name);
        this.message = message;
    }

    public void logPlaying(@NotNull String text) {
        log(Activity.Type.PLAYING, text);
    }

    public void logWatching(@NotNull String text) {
        log(Activity.Type.WATCHING, text);
    }

    public void logListening(@NotNull String text) {
        log(Activity.Type.LISTENING, text);
    }

    private void log(@NotNull Activity.Type type, @NotNull String text) {
        super.log(
                HistoryMessage.create(message)
                        .replace("activity_type", type.toString())
                        .replace("activity_text", text)
                        .getMessage()
        );
    }
}
