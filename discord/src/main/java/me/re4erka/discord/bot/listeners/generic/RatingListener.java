package me.re4erka.discord.bot.listeners.generic;

import me.re4erka.api.bot.listener.common.Listener;
import me.re4erka.api.bot.listener.common.PostOrder;
import me.re4erka.api.bot.receiver.Receiver;
import me.re4erka.api.bot.word.random.answer.ChanceAnswer;
import me.re4erka.api.bot.word.search.SearchWords;
import me.re4erka.api.bot.word.Words;

@SuppressWarnings("unused")
public class RatingListener extends Listener {
    private final SearchWords matchesWords = SearchWords.builder()
            .words("как тебе").words("как тебе это")
            .words("как тебе вот это").words("оцени")
            .build();

    private final SearchWords searchWords = SearchWords.builder()
            .words("оцени фото").words("оцени фотку")
            .words("оцени картинку").words("оцени фотографию")
            .words("на оценку")
            .build();

    private final ChanceAnswer answer = ChanceAnswer.builder()
            .word("Я оцениваю на хорошо.", 20)
            .word("Я оцениваю на выше среднего.", 20)
            .word("Я оцениваю на нормально.", 20)
            .word("Я оцениваю на ниже среднего.", 20)
            .word("Я оцениваю на плохо.", 20)
            .build();

    public RatingListener() {
        super("USER_ASKS_MESSAGE_RATING", PostOrder.NORMAL);
    }

    @Override
    public boolean onListen(Receiver receiver, Words words) {
        if (words.matchesAny(matchesWords)
                || words.containsAny(searchWords)) {
            receiver.reply(
                    answer.generate().text()
            );

            return true;
        }

        return false;
    }
}
