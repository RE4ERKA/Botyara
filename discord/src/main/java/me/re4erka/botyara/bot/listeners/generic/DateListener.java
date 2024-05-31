package me.re4erka.botyara.bot.listeners.generic;

import me.re4erka.botyara.api.bot.listener.common.Listener;
import me.re4erka.botyara.api.bot.listener.common.PostOrder;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.api.bot.word.search.SearchWords;
import me.re4erka.botyara.api.util.key.Key;
import me.re4erka.botyara.bot.ActiveBot;

import java.time.ZonedDateTime;

public class DateListener extends Listener {
    private final SearchWords searchWords = SearchWords.builder()
            .words("сколько времени")
            .words("сколько сейчас времени")
            .words("сколько прямо сейчас времени")
            .words("сколько время")
            .words("сколько сейчас время")
            .words("сколько прямо сейчас время")
            .words("какое сейчас время")
            .words("какое прямо сейчас время")
            .words("сколько по времени")
            .words("какой час")
            .words("какой сейчас час")
            .words("какой прямо сейчас час")
            .build();

    public DateListener() {
        super(Key.of("USER_ASKS_WHAT_DATE_TODAY_IS"), PostOrder.NORMAL);
    }

    @Override
    public boolean onListen(Receiver receiver, Words words) {
        if (words.containsAny(searchWords)) {
            final ZonedDateTime date = ZonedDateTime.now(ActiveBot.ZONE_ID);

            receiver.reply(
                    String.format("В данный момент %d:%d часов.", date.getHour(), date.getMinute())
            ).reputation(1);

            return true;
        }

        return false;
    }
}
