package me.re4erka.botyara.bot.activities;

import me.re4erka.botyara.api.bot.Bot;
import me.re4erka.botyara.api.bot.activity.Activity;
import me.re4erka.botyara.api.util.random.Random;
import me.re4erka.botyara.file.type.Properties;
import org.jetbrains.annotations.NotNull;

public class EatingActivity implements Activity {
    @Override
    public boolean update(@NotNull Bot bot) {
        if (Random.chance(20)) {
            bot.updateActivity(
                    Type.EATING,
                    "Кушает " + Random.nextElement(
                            Properties.ACTIVITIES_EATING_FOODS.asStringList()
                    )
            );
            return true;
        }

        return false;
    }
}
