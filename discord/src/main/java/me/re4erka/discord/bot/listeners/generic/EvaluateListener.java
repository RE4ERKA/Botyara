package me.re4erka.discord.bot.listeners.generic;

import me.re4erka.api.bot.listener.ListeningBot;
import me.re4erka.api.bot.listener.ask.AskListener;
import me.re4erka.api.bot.listener.ask.AskType;
import me.re4erka.api.bot.listener.common.PostOrder;
import me.re4erka.api.bot.receiver.Receiver;
import me.re4erka.api.bot.word.Words;

import java.util.OptionalInt;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
public class EvaluateListener extends AskListener {
    private final String[] evaluate = new String[] {
            "от", "до"
    };

    public EvaluateListener(ListeningBot bot) {
        super("EVALUATE", PostOrder.NORMAL, bot);
    }

    @Override
    public boolean onListen(Receiver receiver, Words words) {
        if (words.size() > 4) {
            final OptionalInt optionalIndex = words.find(evaluate[0]);

            if (optionalIndex.isPresent()) {
                int first;

                try {
                    first = Integer.parseInt(
                            words.get(optionalIndex.getAsInt())
                    );
                } catch (NumberFormatException ex) {
                    return false;
                }

                if (words.equals(optionalIndex.getAsInt() + 1, evaluate[1])) {
                    if (first < 1) {
                        receiver.reply("Первое число не может быть меньше одного!");

                        return true;
                    }

                    int second;

                    try {
                        second = Integer.parseInt(
                                words.get(optionalIndex.getAsInt() + 2)
                        );
                    } catch (NumberFormatException ex) {
                        return false;
                    }

                    if (second < 2) {
                        receiver.reply("Второе число не может быть меньше двух!");

                        return true;
                    }

                    if (first == second) {
                        receiver.reply("Первое и второе число не могут быть одинаковые!");

                        return true;
                    } else if (first > second) {
                        receiver.reply("Первое число не может быть меньше второго!");

                        return true;
                    }

                    if (second > 1000000) {
                        receiver.reply("Второе число не может быть больше миллиона!");

                        return true;
                    }

                    this.addAskListener(receiver.getId());

                    receiver.reply(
                            "По моему субъективному мнению, я думаю... " + ThreadLocalRandom.current().nextInt(first, second)
                    ).reputation(1);

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean onAsked(Receiver receiver, Words words) {
        if (words.containsAny(AskType.WHY.getSearchWords())) {
            receiver.reply("Потому что я решил оценить на такое количество баллов!");

            return true;
        }

        if (words.containsAny(AskType.SURE.getSearchWords())) {
            receiver.reply("Я думаю прежде чем ответить! Поэтому я уверен в своем ответе.");

            return true;
        }

        return false;
    }
}
