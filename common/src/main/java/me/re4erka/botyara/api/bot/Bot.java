package me.re4erka.botyara.api.bot;

import lombok.Getter;
import me.re4erka.botyara.api.bot.activity.Activity;
import me.re4erka.botyara.api.bot.mood.MoodType;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.sleep.SleepQuality;
import me.re4erka.botyara.api.bot.word.Words;
import org.jetbrains.annotations.NotNull;

import java.time.ZonedDateTime;

@Getter
public abstract class Bot {
    protected boolean isSleep;
    protected MoodType mood = MoodType.NORMAL;

    public abstract void listen(@NotNull Receiver receiver, @NotNull Words words);

    public abstract void setMood(@NotNull MoodType type);

    public abstract void sleep();
    public abstract void wakeUp();

    public abstract SleepQuality getSleepQuality();

    public abstract int getCurrentHours();
    public abstract ZonedDateTime getCurrentDateTime();

    public abstract void updateActivities();

    public abstract void updateActivity(@NotNull Activity.Type type, @NotNull String content);
    public abstract Activity.Type getActivityType();
    public abstract String getActivityContent();

    public abstract void doesNothing();

    public abstract Bot cleanUp();
}
