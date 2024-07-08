package me.re4erka.botyara.api.bot.activity;

public interface Activity {
    boolean update();

    enum Type {
        PLAYING,
        WATCHING,
        LISTENING,
        SLEEPING
    }
}
