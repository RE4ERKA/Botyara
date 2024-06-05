package me.re4erka.botyara.bot.listeners.generic;

import me.re4erka.botyara.api.bot.listener.ListeningBot;
import me.re4erka.botyara.api.bot.listener.ask.AskListener;
import me.re4erka.botyara.api.bot.listener.ask.AskType;
import me.re4erka.botyara.api.bot.listener.common.PostOrder;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.search.SearchWords;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.api.util.key.Key;
import me.re4erka.botyara.api.util.random.Random;

@SuppressWarnings("unused")
public class ClarifyListener extends AskListener {
    private final String[] answers = new String[] {
            "Скорее да, чем нет.",
            "Скорее нет, чем да."
    };

    private final SearchWords searchWords = SearchWords.builder()
            .words("четко скажи").words("скажи четко")
            .words("скажи точно").words("точно скажи")
            .words("скажи честно").words("честно скажи")
            .words("скажи правду").words("правду скажи")
            .build();

    public ClarifyListener(ListeningBot bot) {
        super(Key.of("CLARIFY"), PostOrder.LAST, bot);
    }

    @Override
    public boolean onListen(Receiver receiver, Words words) {
        if (words.size() == 1) {
            if (words.equals(0, "четко")) {
                this.addAskListener(receiver.getId());

                receiver.reply(
                        Random.nextElement(answers)
                ).reputation(3);

                return true;
            }

            return false;
        }

        if (words.containsAny(searchWords)) {
            this.addAskListener(receiver.getId());

            receiver.reply(
                    Random.nextElement(answers)
            ).reputation(3);

            return true;
        }

        return false;
    }

    @Override
    public boolean onAsked(Receiver receiver, Words words) {
        if (words.containsAny(AskType.WHY.getSearchWords())) {
            receiver.reply("Ну потому что я еще не уверен, что ответить...")
                    .reputation(1);

            return true;
        }

        if (words.containsAny(AskType.SURE.getSearchWords())) {
            receiver.reply("Ну я не уверен еще в этом вопросе.")
                    .reputation(1);

            return true;
        }

        return false;
    }
}
