package me.re4erka.botyara.api.bot.receiver;

import me.re4erka.botyara.api.bot.friendship.FriendshipType;

import javax.annotation.Nullable;

public interface Receiver {
    long getId();

    FriendshipType getFriendshipType();

    void changeName(final String p0);
    @Nullable
    String getName();

    Receiver reply(final String p0);

    boolean reputation(final int p0);
    int getReputation();
}
