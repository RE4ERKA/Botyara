package me.re4erka.discord.bot.activities;

import me.re4erka.api.bot.Bot;
import me.re4erka.api.bot.activity.Activity;
import me.re4erka.api.util.random.Random;
import me.re4erka.discord.file.type.Properties;

public class ListeningActivity implements Activity {
    private final Bot bot;

    public ListeningActivity(Bot bot) {
        this.bot = bot;
    }

    @Override
    public boolean update() {
        if (bot.isSleep()) {
            return true;
        }

        if (Random.chance(33)) {
            bot.listen(
                    Random.nextElement(
                            Properties.ACTIVITIES_LISTENING_SONGS.asStringList()
                    )
            );

            return true;
        }

        return false;
    }
}
