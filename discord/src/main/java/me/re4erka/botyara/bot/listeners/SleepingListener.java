package me.re4erka.botyara.bot.listeners;

import me.re4erka.botyara.api.bot.listener.common.IListener;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.api.bot.word.random.answer.MultiAnswer;
import me.re4erka.botyara.api.bot.word.random.answer.WordEnd;

public class SleepingListener implements IListener {
    private final MultiAnswer multiAnswer = MultiAnswer.newBuilder()
            .part(new String[] {
                    "Прости, что-то хотел", "Кто говорит",
                    "Кто меня разбудил", "Что за звуки"
            }, WordEnd.QUESTION)
            .part(new String[] {
                    "Я спал сладким сном, а ты меня разбудил :sleeping:",
                    "Я отсыпался и меня так внезапно разбудили :sleeping:",
                    "Не буди меня так больше, пожалуйста! :sleeping:",
                    "В следующий раз не буди меня так, ладно? :sleeping:"
            }, WordEnd.EMPTY)
            .build();

    @Override
    public boolean onListen(Receiver receiver, Words words) {
        receiver.reply(
                multiAnswer.generate()
        ).reputation(-100);

        return true;
    }
}
