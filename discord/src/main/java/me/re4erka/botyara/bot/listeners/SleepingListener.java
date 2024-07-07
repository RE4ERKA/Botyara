package me.re4erka.botyara.bot.listeners;

import me.re4erka.botyara.api.bot.listener.common.IListener;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.WordEnd;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.api.bot.answer.MultiAnswer;
import org.jetbrains.annotations.NotNull;

public class SleepingListener implements IListener {
    private final MultiAnswer multiAnswer = MultiAnswer.builder()
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
    public boolean onListen(@NotNull Receiver receiver, @NotNull Words words) {
        receiver.reply(
                multiAnswer.generate()
        ).reputation(-10);

        return true;
    }
}
