package me.re4erka.botyara.bot.receiver.type;

import me.re4erka.botyara.api.bot.friendship.FriendshipType;
import me.re4erka.botyara.api.bot.user.UserData;
import me.re4erka.botyara.bot.DiscordBot;
import me.re4erka.botyara.bot.receiver.DiscordReceiver;
import org.javacord.api.entity.message.Message;
import org.jetbrains.annotations.NotNull;

public class DataReceiver extends DiscordReceiver {
    protected DataReceiver(@NotNull Message message,
                           @NotNull UserData data,
                           @NotNull DiscordBot bot,
                           boolean hasMessageBeenChanged) {
        super(message, data, bot, hasMessageBeenChanged);
    }

    public DataReceiver(@NotNull Message message, @NotNull UserData data, @NotNull DiscordBot bot) {
        this(message, data, bot, false);
    }

    @Override
    public long getId() {
        return data.getId();
    }

    @Override
    public FriendshipType getFriendshipType() {
        return data.getFriendshipType();
    }

    @Override
    public void changeName(@NotNull String name) {
        data.setName(name);
    }

    @Override
    public String getName() {
        return data.getName();
    }

    @Override
    public int getReputation() {
        return data.getReputation();
    }

    @Override
    public boolean reputation(int delta) {
        /* Проверяем изменилось ли значение репутации */
        if (data.setReputation(getReputation() + delta)) {
            /* Проверяем изменился ли статус дружбы. */
            return data.checkFriendshipStatus();
        }

        return false;
    }
}
