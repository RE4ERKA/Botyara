package me.re4erka.botyara.bot.activities;

import me.re4erka.botyara.api.bot.Bot;
import me.re4erka.botyara.api.bot.activity.Activity;
import me.re4erka.botyara.api.util.random.Random;
import me.re4erka.botyara.file.type.Properties;
import org.jetbrains.annotations.NotNull;

public class ListeningActivity implements Activity {

    @Override
    public boolean update(@NotNull Bot bot) {
        if (Random.chance(20)) {
            bot.updateActivity(
                    Type.LISTENING,
                    Random.nextElement(
                            Properties.ACTIVITIES_LISTENING_SONGS.asStringList()
                    )
            );

            return true;
        }

        return false;
    }
}
