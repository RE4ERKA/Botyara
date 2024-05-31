package me.re4erka.botyara.bot.activities;

import me.re4erka.botyara.api.bot.Bot;
import me.re4erka.botyara.api.bot.activity.Activity;
import me.re4erka.botyara.api.bot.mood.MoodType;
import me.re4erka.botyara.api.util.random.Random;

public class MoodActivity implements Activity {
    private final Bot bot;

    public MoodActivity(Bot bot) {
        this.bot = bot;
    }

    @Override
    public boolean update() {
        bot.setMood(Random.nextEnum(MoodType.class));

        return false;
    }
}
