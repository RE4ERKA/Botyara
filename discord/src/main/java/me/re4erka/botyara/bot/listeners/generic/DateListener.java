package me.re4erka.botyara.bot.listeners.generic;

import me.re4erka.botyara.api.bot.listener.common.Listener;
import me.re4erka.botyara.api.bot.listener.common.PostOrder;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.api.bot.word.search.SearchWords;
import me.re4erka.botyara.api.util.key.Key;
import me.re4erka.botyara.api.util.time.Pluralizer;
import me.re4erka.botyara.bot.ActiveBot;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("unused")
public class DateListener extends Listener {
    private final SearchWords timeWords = SearchWords.builder()
            .words("какое время сегодня")
            .words("какое время сейчас")
            .words("какое время прямо сейчас")
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

    private final SearchWords dateWords = SearchWords.builder()
            .words("какое число")
            .words("какое сейчас число")
            .words("какое прямо сейчас число")
            .words("какое сегодня число")
            .words("какое сейчас число")
            .words("какое прямо сейчас число")
            .words("какая сегодня дата")
            .words("какая сейчас дата")
            .words("какая прямо сейчас дата")
            .build();

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public DateListener() {
        super(Key.of("USER_ASKS_WHAT_DATE_TODAY_IS"), PostOrder.NORMAL);
    }

    @Override
    public boolean onListen(Receiver receiver, Words words) {
        if (words.containsAny(timeWords)) {
            final ZonedDateTime date = ZonedDateTime.now(ActiveBot.ZONE_ID);

            receiver.reply(
                    String.format(
                            "Судя по моим часам сейчас %d %s %d %s %d %s. :alarm_clock:",
                            date.getHour(),
                            Pluralizer.formatHours(date.getHour()),
                            date.getMinute(),
                            Pluralizer.formatMinutes(date.getMinute()),
                            date.getSecond(),
                            Pluralizer.formatSeconds(date.getSecond())
                    )
            ).reputation(1);

            return true;
        }

        if (words.containsAny(dateWords)) {
            final ZonedDateTime date = ZonedDateTime.now(ActiveBot.ZONE_ID);

            receiver.reply(
                    String.format(
                            "Судя по моему календарю сегодня %s. :calendar_spiral:",
                            formatter.format(date)
                    )
            ).reputation(1);

            return true;
        }

        return false;
    }
}
