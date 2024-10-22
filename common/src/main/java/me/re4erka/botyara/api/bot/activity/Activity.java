package me.re4erka.botyara.api.bot.activity;

import me.re4erka.botyara.api.bot.Bot;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Activity {
    boolean update(@NotNull Bot bot);

    enum Type {
        PLAYING,
        WATCHING,
        LISTENING,
        EATING,
        NOTHING
    }
}
