package me.re4erka.botyara.api.bot;

import lombok.Getter;
import me.re4erka.botyara.api.bot.activity.Activity;
import me.re4erka.botyara.api.bot.mood.MoodType;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.Words;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class Bot {
    protected boolean isSleep;
    protected MoodType mood = MoodType.NORMAL;

    public abstract void onListen(@NotNull Receiver receiver, @NotNull Words words);

    public abstract void setMood(@NotNull MoodType type);

    public abstract void sleep(boolean isSleep);
    public abstract void watch(@NotNull String title);
    public abstract void listen(@NotNull String song);
    public abstract void play(@NotNull String game);

    public abstract Activity.Type getActivityType();
    public abstract String getActivityContent();
    public abstract int getCurrentHours();

    public abstract Bot cleanUp();
}
