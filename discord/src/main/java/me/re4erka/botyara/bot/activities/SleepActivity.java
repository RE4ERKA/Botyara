package me.re4erka.botyara.bot.activities;

import me.re4erka.botyara.api.bot.activity.Activity;
import me.re4erka.botyara.bot.ActiveBot;
import me.re4erka.botyara.file.type.Properties;
import org.jetbrains.annotations.NotNull;

public class SleepActivity implements Activity {
    private final ActiveBot bot;

    private final int between;
    private final int to;

    public SleepActivity(@NotNull ActiveBot bot) {
        this.bot = bot;

        this.between = Properties.ACTIVITIES_SLEEPING_PATTERN_BETWEEN.asInt();
        this.to = Properties.ACTIVITIES_SLEEPING_PATTERN_TO.asInt();
    }

    @Override
    public boolean update() {
        final int hours = bot.getCurrentHours();

        if (hours == between || hours <= to) {
            bot.sleep(true);

            return true;
        } else {
            bot.sleep(false);
        }

        return false;
    }
}
