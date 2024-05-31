package me.re4erka.botyara.history;

import me.re4erka.botyara.api.history.History;
import me.re4erka.botyara.api.history.logging.HistoryMessage;
import org.javacord.api.entity.activity.ActivityType;

public class ActivityHistory extends History {
    private final String message;

    public ActivityHistory(String message) {
        super("BotActivity");
        this.message = message;
    }

    public void log(ActivityType type, String text) {
        super.log(
                HistoryMessage.create(message)
                        .replace("activity_type", type.toString())
                        .replace("activity_text", text)
                        .get()
        );
    }
}
