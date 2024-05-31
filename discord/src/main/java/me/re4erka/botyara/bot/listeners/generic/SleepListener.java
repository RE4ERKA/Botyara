package me.re4erka.botyara.bot.listeners.generic;

import me.re4erka.botyara.api.bot.listener.ListeningBot;
import me.re4erka.botyara.api.bot.listener.common.Listener;
import me.re4erka.botyara.api.bot.listener.common.PostOrder;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.api.bot.word.search.SearchWords;
import me.re4erka.botyara.api.util.key.Key;
import me.re4erka.botyara.bot.ActiveBot;
import me.re4erka.botyara.file.type.Properties;

public class SleepListener extends Listener {
    private final ActiveBot bot;

    private final SearchWords searchWords = SearchWords.builder()
            .words("ты скоро спать").words("ты будешь спать")
            .words("ты скоро пойдешь спать").words("ты скоро ложиться спать")
            .words("как скоро ты пойдешь спать").words("как скоро ты будешь ложиться спать")
            .words("как скоро ты будешь спать").words("как скоро ты спать")
            .words("во сколько ты пойдешь спать").words("во сколько ты ложишься спать")
            .words("во сколько ты будешь спать")
            .words("тебе скоро спать").words("тебе скоро идти спать")
            .words("тебе скоро ложиться спать").words("тебе скоро идти спать")
            .words("тебе во сколько идти спать").words("тебе во сколько ложиться спать")
            .words("тебе во сколько спать")
            .build();

    public SleepListener(ListeningBot bot) {
        super(Key.of("USER_ASKS_IF_YOULL_BE_ASLEEP_SOON"), PostOrder.NORMAL);
        this.bot = (ActiveBot) bot;
    }

    @Override
    public boolean onListen(Receiver receiver, Words words) {
        if (words.containsAny(searchWords)) {
            if (bot.getCurrentTime() >= Properties.ACTIVITIES_SLEEPING_PATTERN_BETWEEN.asInt() - 1) {
                receiver.reply("Уже скоро собираюсь идти спать!").reputation(1);
            } else {
                receiver.reply(
                        String.format(
                                "В ближайшее время не планирую идти спать! Обычно, я стараюсь, ложиться в %d:00",
                                Properties.ACTIVITIES_SLEEPING_PATTERN_BETWEEN.asInt()
                        )
                ).reputation(1);
            }

            return true;
        }

        return false;
    }
}
