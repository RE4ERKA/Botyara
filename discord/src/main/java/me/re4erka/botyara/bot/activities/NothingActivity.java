package me.re4erka.botyara.bot.activities;

import me.re4erka.botyara.api.bot.Bot;
import me.re4erka.botyara.api.bot.activity.Activity;
import org.jetbrains.annotations.NotNull;

public class NothingActivity implements Activity {
    @Override
    public boolean update(@NotNull Bot bot) {
        bot.doesNothing();
        return true;
    }
}
