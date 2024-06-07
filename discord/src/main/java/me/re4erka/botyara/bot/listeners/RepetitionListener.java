package me.re4erka.botyara.bot.listeners;

import com.google.common.collect.EvictingQueue;
import me.re4erka.botyara.api.bot.listener.common.IListener;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.cache.CacheWords;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.bot.receiver.DiscordReceiver;
import me.re4erka.botyara.file.type.Properties;

public class RepetitionListener implements IListener {
    private final EvictingQueue<CacheWords> previousWords = EvictingQueue.create(
            Properties.LISTENER_REPETITION_WORDS_SIZE.asInt()
    );

    @Override
    public boolean onListen(Receiver receiver, Words words) {
        if (words.sizeRaw() == 1) {
            return false;
        }

        for (final CacheWords cacheWords : previousWords) {
            if (cacheWords.similarity(words)) {
                if (receiver instanceof DiscordReceiver discordReceiver) {
                    if (discordReceiver.hasMessageBeenChanged()) {
                        return true;
                    }
                }

                if (cacheWords.getId() == receiver.getId()) {
                    receiver.reply("Эй! Я уже тебе отвечал на это предложение ранее!").reputation(-3);
                } else {
                    receiver.reply("Я уже ранее отвечал на это же предложение!").reputation(-1);
                }

                return true;
            }
        }

        previousWords.add(
                words.toCache(receiver.getId())
        );

        return false;
    }
}
