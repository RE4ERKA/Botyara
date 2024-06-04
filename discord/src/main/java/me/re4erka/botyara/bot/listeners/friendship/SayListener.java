package me.re4erka.botyara.bot.listeners.friendship;

import me.re4erka.botyara.api.bot.friendship.FriendshipType;
import me.re4erka.botyara.api.bot.listener.common.Listener;
import me.re4erka.botyara.api.bot.listener.common.PostOrder;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.api.util.key.Key;
import me.re4erka.botyara.bot.receiver.DiscordReceiver;
import me.re4erka.botyara.executor.ScheduledExecutor;
import net.fellbaum.jemoji.Emojis;
import org.apache.commons.lang3.StringUtils;

import java.util.OptionalInt;
import java.util.StringJoiner;

@SuppressWarnings("unused")
public class SayListener extends Listener {
    private final String[] searchWordArray = new String[] {
            "скажи", "повтори"
    };

    public SayListener() {
        super(Key.of("USER_ASKS_TO_PASS_ON_A_MESSAGE"), PostOrder.NORMAL);
    }

    @Override
    public boolean onListen(Receiver receiver, Words words) {
        final OptionalInt index = words.find(searchWordArray);

        if (index.isPresent()) {
            if (receiver.getFriendshipType() == FriendshipType.BEST_FRIEND) {
                final StringJoiner joiner = new StringJoiner(StringUtils.SPACE);

                for (int i = index.getAsInt() + 1; i < words.sizeRaw(); i++) {
                    joiner.add(
                            words.getRaw(i)
                    );
                }

                if (joiner.length() == 0) {
                    receiver.reply("Что сказать? :thinking: Я слушаю.");
                    return true;
                }

                if (receiver instanceof DiscordReceiver discordReceiver) {
                    discordReceiver.emojiThenRun(Emojis.THUMBS_UP.getEmoji(), userMessage -> {
                        final String message = StringUtils.capitalize(joiner.toString());

                        discordReceiver.botTyping().thenRun(() -> ScheduledExecutor.executeLater(
                                () -> userMessage.getChannel().sendMessage(message),
                                discordReceiver.calculateDelay(message.length())
                        ));
                    }).reputation(-50);
                }
            } else if (receiver.getFriendshipType() == FriendshipType.FRIEND) {
                receiver.reply("Хоть мы с тобой и друзья, но я еще не готов настолько доверять тебе... :disappointed:");
            } else {
                receiver.reply("Извини, но я еще недостаточно тебе доверяю... :pensive:");
            }

            return true;
        }

        return false;
    }
}
