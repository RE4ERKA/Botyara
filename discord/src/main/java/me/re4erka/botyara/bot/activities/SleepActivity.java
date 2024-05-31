package me.re4erka.botyara.bot.activities;

import me.re4erka.botyara.api.bot.activity.Activity;
import me.re4erka.botyara.bot.ActiveBot;

import java.time.ZonedDateTime;

public class SleepActivity implements Activity {
    private final ActiveBot bot;

    public SleepActivity(ActiveBot bot) {
        this.bot = bot;
    }

    @Override
    public boolean update() {
        final int hours = ZonedDateTime.now(ActiveBot.ZONE_ID).getHour();

        if (hours == 23 || hours <= 5) {
            bot.sleep(true);

            return true;
        } else {
            bot.sleep(false);
        }

        return false;
    }
}
