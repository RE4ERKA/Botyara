package me.re4erka.botyara.api.bot.receiver;

import me.re4erka.botyara.api.bot.friendship.FriendshipType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

public abstract class Receiver {
    public abstract long getId();

    public abstract FriendshipType getFriendshipType();
    public abstract void intoFamiliar(String name);

    public abstract void setName(String name);
    public abstract String getName();

    public abstract void reputation(int delta);
    public abstract int getReputation();

    public abstract boolean isStranger();

    public abstract Receiver reply(String message);

    public Receiver reply(String message, Pair<String, String> replace) {
        message = StringUtils.replace(message, replace.getKey(), replace.getValue());
        return reply(message);
    }
}
