package me.re4erka.botyara.bot.receiver.type;

import me.re4erka.botyara.api.bot.friendship.FriendshipType;
import me.re4erka.botyara.api.bot.user.UserData;
import me.re4erka.botyara.bot.receiver.DiscordReceiver;
import org.javacord.api.entity.message.Message;
import org.jetbrains.annotations.Nullable;

public class EmptyReceiver extends DiscordReceiver {
    protected EmptyReceiver(Message message, boolean hasMessageBeenChanged) {
        super(message, UserData.newStranger(), hasMessageBeenChanged);
    }

    public EmptyReceiver(Message message) {
        this(message, false);
    }

    @Override
    public long getId() {
        return message.getAuthor().getId();
    }

    @Override
    public FriendshipType getFriendshipType() {
        return FriendshipType.STRANGER;
    }

    @Override
    public void changeName(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable String getName() {
        return null;
    }

    @Override
    public boolean reputation(int delta) {
        return false;
    }

    @Override
    public int getReputation() {
        return 0;
    }
}
