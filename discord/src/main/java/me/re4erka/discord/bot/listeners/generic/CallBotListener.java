package me.re4erka.discord.bot.listeners.generic;

import me.re4erka.api.bot.listener.common.Listener;
import me.re4erka.api.bot.listener.common.PostOrder;
import me.re4erka.api.bot.receiver.Receiver;
import me.re4erka.api.bot.word.Words;
import me.re4erka.api.bot.word.random.answer.MultiAnswer;
import me.re4erka.api.bot.word.random.answer.WordEnd;

@SuppressWarnings("unused")
public class CallBotListener extends Listener {
    private final MultiAnswer answer = MultiAnswer.newBuilder()
            .part(new String[]{ "Слушаю", "Чего зовешь", "Я весь во внимании", "Ну давай, говори" }, WordEnd.DOT)
            .build();

    public CallBotListener() {
        super("CALL_BOT_LISTENER", PostOrder.FIRST);
    }

    @Override
    public boolean onListen(Receiver receiver, Words words) {
        if (words.sizeRaw() == 1) {
            receiver.reply(
                    answer.generate()
            ).reputation(1);

            return true;
        }

        return false;
    }
}
