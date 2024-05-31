package me.re4erka.botyara.api.bot.receiver;

import me.re4erka.botyara.api.bot.friendship.FriendshipType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

public interface Receiver {
    /* Реализация получение сообщения в зависимости от платформы */
    void onReply(String message);

    long getId();

    FriendshipType getFriendshipType();
    void intoFamiliar(String name);

    void setName(String name);
    String getName();

    void reputation(int delta);
    int getReputation();

    boolean isStranger();

    default Receiver reply(String message) {
        this.onReply(message);
        return this;
    }

    default Receiver reply(String message, Pair<String, String> replace) {
        message = StringUtils.replace(message, replace.getKey(), replace.getValue());

        this.onReply(message);
        return this;
    }
}
