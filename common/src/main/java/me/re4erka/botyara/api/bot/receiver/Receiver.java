package me.re4erka.botyara.api.bot.receiver;

import me.re4erka.botyara.api.bot.friendship.FriendshipType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Receiver {
    long getId();

    FriendshipType getFriendshipType();

    void changeName(@NotNull String name);
    @Nullable String getName();

    Receiver reply(@NotNull String respondMessage);

    boolean reputation(int delta);
    int getReputation();
}
