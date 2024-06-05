package me.re4erka.botyara.bot.listeners.generic;

import lombok.RequiredArgsConstructor;
import me.re4erka.botyara.api.bot.listener.ListeningBot;
import me.re4erka.botyara.api.bot.listener.ask.AskListener;
import me.re4erka.botyara.api.bot.listener.ask.AskType;
import me.re4erka.botyara.api.bot.listener.common.PostOrder;
import me.re4erka.botyara.api.bot.memory.part.ExpiringMemoryPart;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.search.SearchWords;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.api.util.key.Key;
import me.re4erka.botyara.api.util.random.Random;

import java.util.concurrent.TimeUnit;

public class WhatsUpListener extends AskListener {
    private final SearchWords searchWords = SearchWords.builder()
            .words("как у тебя дела").words("как твои дела").words("как дела")
            .words("как у тебя делишки").words("как твои делишки").words("как делишки")
            .words("как у тебя настроение").words("как твое настроение").words("как настроение")
            .words("как у тебя жизнь").words("как твоя жизнь").words("как жизнь")
            .words("как себя чувствуешь").words("как самочувствие")
            .words("как ты чувствуешь").words("как ты себя чувствуешь")
            .words("как у тебя житуха").words("как житуха")
            .words("как сам").words("как твое ничего")
            .words("что по чем")
            .build();

    private final ExpiringMemoryPart<WhatsUp> memory = ExpiringMemoryPart.<WhatsUp>builder()
            .expireAfter(60, TimeUnit.MINUTES)
            .update(() -> Random.nextEnum(WhatsUp.class))
            .build();

    public WhatsUpListener(ListeningBot bot) {
        super(Key.of("WHATS_UP"), PostOrder.NORMAL, bot);
    }

    @Override
    public boolean onListen(Receiver receiver, Words words) {
        if (words.size() == 2) {
            if (words.equals(0, "как") && words.equals(1, "ты")
                    || words.equals(0, "ты") && words.equals(1, "как")) {
                this.addAskListener(receiver.getId());

                receiver.reply(
                        memory.updateAndGet().answer
                ).reputation(5);

                return true;
            }
        }

        if (words.containsAny(searchWords)) {
            this.addAskListener(receiver.getId());

            receiver.reply(
                    memory.updateAndGet().answer
            ).reputation(5);

            return true;
        }

        return false;
    }

    @Override
    public boolean onAsked(Receiver receiver, Words words) {
        if (words.containsAny(AskType.WHY.getSearchWords())) {
            switch (memory.getOrElse(WhatsUp.NORMAL)) {
                case GOOD -> receiver.reply("Потому что сейчас солнечный и хороший день!");
                case NORMAL -> receiver.reply("Ну сейчас я себя чувствую не плохо и не хорошо.");
                case BAD -> receiver.reply("Потому что меня обидели, я не буду говорить кто...");
            }

            return true;
        }

        if (words.containsAny(AskType.SURE.getSearchWords())) {
            receiver.reply("Я не буду врать по поводу своего настроения!");

            return true;
        }

        return false;
    }

    @RequiredArgsConstructor
    private enum WhatsUp {
        GOOD("У меня дела хорошо."),
        NORMAL("У меня дела нормально."),
        BAD("У меня дела плохо.");

        private final String answer;
    }
}
