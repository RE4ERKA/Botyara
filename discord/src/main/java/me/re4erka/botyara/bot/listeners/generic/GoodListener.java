package me.re4erka.botyara.bot.listeners.generic;

import me.re4erka.botyara.api.bot.listener.ListeningBot;
import me.re4erka.botyara.api.bot.listener.ask.AskListener;
import me.re4erka.botyara.api.bot.listener.ask.AskType;
import me.re4erka.botyara.api.bot.listener.common.PostOrder;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.search.SearchWords;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.api.util.key.Key;
import me.re4erka.botyara.bot.ActiveBot;

@SuppressWarnings("unused")
public class GoodListener extends AskListener {
    private final SearchWords morning = SearchWords.builder().words("доброе утро").words("утречко").build();
    private final SearchWords day = SearchWords.builder().words("добрый день").build();
    private final SearchWords evening = SearchWords.builder().words("доброго вечера").build();
    private final SearchWords night = SearchWords.builder().words("доброй ночи").build();

    private final ActiveBot bot;

    public GoodListener(ListeningBot bot) {
        super(Key.of("GOOD_MORNING_AND_OTHER"), PostOrder.LAST, bot);
        this.bot = (ActiveBot) bot;
    }

    @Override
    public boolean onListen(Receiver receiver, Words words) {
        if (words.containsAny(morning)) {
            final int hours = bot.getCurrentHours();

            if (hours >= 4 && hours <= 12) {
                receiver.reply("Доброе!");
            } else {
                this.addAskListener(receiver.getId());

                receiver.reply("Я бы сказал доброе, но сейчас не утро...").reputation(1);
            }

            return true;
        }

        if (words.containsAny(day)) {
            final int hours = bot.getCurrentHours();

            if (hours >= 12 && hours <= 16) {
                receiver.reply("Добрый!");
            } else {
                this.addAskListener(receiver.getId());

                receiver.reply("Я бы сказал добрый, но сейчас не день...").reputation(1);
            }

            return true;
        }

        if (words.containsAny(evening)) {
            final int hours = bot.getCurrentHours();

            if (hours >= 15) {
                receiver.reply("Доброго!");
            } else {
                this.addAskListener(receiver.getId());

                receiver.reply("Я бы сказал доброго, но сейчас не вечер..").reputation(1);
            }

            return true;
        }

        if (words.containsAny(night)) {
            final int hours = bot.getCurrentHours();

            if (hours <= 6 || hours == 23) {
                receiver.reply("Доброй!");
            } else {
                this.addAskListener(receiver.getId());

                receiver.reply("Я бы сказал доброй, но сейчас не ночь..").reputation(1);
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean onAsked(Receiver receiver, Words words) {
        if (words.containsAny(AskType.WHY.getSearchWords())) {
            receiver.reply("Потому что посмотри на мое время по МСК!");

            return true;
        }

        if (words.containsAny(AskType.SURE.getSearchWords())) {
            receiver.reply("Я посмотрел на свое и говорю как есть!");

            return true;
        }

        return false;
    }
}
