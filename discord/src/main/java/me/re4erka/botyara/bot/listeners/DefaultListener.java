package me.re4erka.botyara.bot.listeners;

import me.re4erka.botyara.api.bot.listener.common.IListener;
import me.re4erka.botyara.api.bot.memory.word.MemoryWords;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.api.bot.word.random.answer.ChanceAnswer;
import me.re4erka.botyara.api.bot.word.random.answer.ChanceWord;
import me.re4erka.botyara.api.util.random.Random;
import me.re4erka.botyara.file.type.Properties;

import java.util.Timer;
import java.util.TimerTask;

public class DefaultListener implements IListener {
    private final ChanceAnswer answer = ChanceAnswer.builder()
            .word("Да, это правда.", 45)
            .word("Нет, это ложь.", 45)
            .word("Возможно, я не уверен.", 10)
            .build();

    private final MemoryWords memoryWords = new MemoryWords(
            Properties.LISTENER_DEFAULT_RESPONSE_MEMORY.asInt()
    );

    @Override
    public boolean onListen(Receiver receiver, Words words) {
        memoryWords.getIfMatches(
                words.toString()
        ).ifPresentOrElse(receiver::reply, () -> {
            final ChanceWord word = answer.generate();

            receiver.reply(word.text());

            if (word.index() != 2 && Random.chance(5)) {
                PrankTask.start(receiver, word.index());

                memoryWords.add(
                        words.toString(),
                        answer.get(word.index() == 0 ? 1 : 0)
                );
            }
        });

        receiver.reputation(1);

        return true;
    }

    private static final class PrankTask extends TimerTask {
        private final Receiver receiver;
        private final short index;
        
        private PrankTask(Receiver receiver, short index) {
            this.receiver = receiver;
            this.index = index;
        }

        public static void start(Receiver receiver, short index) {
            new Timer(false).schedule(
                    new PrankTask(receiver, index),
                    Random.range(1000, 2000)
            );
        }

        @Override
        public void run() {
            final String answer = index == 0
                    ? "Ладно, это пранк, на самом деле: Нет, это ложь."
                    : "Ладно, это пранк, на самом деле: Да, это правда.";

            receiver.reply(answer);
        }
    }
}
