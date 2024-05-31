package me.re4erka.botyara.api.bot;

import lombok.Getter;
import me.re4erka.botyara.api.bot.mood.MoodType;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.Words;

@Getter
public abstract class Bot {
    protected boolean isSleep;
    protected MoodType mood = MoodType.NORMAL;

    public abstract void onListen(Receiver receiver, Words words);

    public abstract void setMood(MoodType type);

    public abstract void sleep(boolean isSleep);
    public abstract void watch(String title);
    public abstract void listen(String song);
    public abstract void play(String game);

    public abstract int getCurrentTime();

    public abstract Bot cleanUp();
}
