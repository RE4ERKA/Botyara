package me.re4erka.botyara.bot.activities;

import me.re4erka.botyara.api.bot.Bot;
import me.re4erka.botyara.api.bot.activity.Activity;
import me.re4erka.botyara.api.util.random.Random;
import me.re4erka.botyara.file.type.Properties;
import org.jetbrains.annotations.NotNull;

public class PlayingActivity implements Activity {

    @Override
    public boolean update(@NotNull Bot bot) {
        if (Random.chance(20)) {
            bot.updateActivity(
                    Type.PLAYING,
                    Random.nextElement(
                            Properties.ACTIVITIES_PLAYING_GAMES.asStringList()
                    )
            );

            return true;
        }

        return false;
    }
}
