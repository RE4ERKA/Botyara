package me.re4erka.botyara.bot.listeners.generic;

import lombok.RequiredArgsConstructor;
import me.re4erka.botyara.api.bot.listener.ListeningBot;
import me.re4erka.botyara.api.bot.listener.await.AwaitingListener;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.search.SearchWords;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.api.util.key.Key;
import me.re4erka.botyara.api.util.random.Random;
import me.re4erka.botyara.bot.receiver.DiscordReceiver;

public class PlayListener extends AwaitingListener {
    private final SearchWords searchWords = SearchWords.builder()
            .words("давай поиграем")
            .words("давай сыграем")
            .words("давай играть")
            .words("го поиграем")
            .words("го сыграем")
            .words("го играть")
            .words("давай в камень ножницы бумага")
            .words("пошли в камень ножницы бумага")
            .build();

    public PlayListener(ListeningBot bot) {
        super(Key.of("USER_ASKS_TO_PLAY"), bot);
    }

    @Override
    public boolean onListen(Receiver receiver, Words words) {
        if (words.containsAny(searchWords)) {
            receiver.reply("Играем в камень, ножницы, бумага! Говори, что выбираешь из трех вариантов!");
            this.addAwaitingListener(receiver.getId());

            return true;
        }

        return false;
    }

    @Override
    protected boolean onAwaitingListen(Receiver receiver, Words words, int attempts) {
        if (words.contains("камень")) {
            this.replyWhoWin(receiver, MoveType.ROCK, attempts);
            return true;
        }

        if (words.contains("бумага")) {
            this.replyWhoWin(receiver, MoveType.PAPER, attempts);
            return true;
        }

        if (words.contains("ножницы")) {
            this.replyWhoWin(receiver, MoveType.SCISSORS, attempts);
            return true;
        }

        receiver.reply("Я не понимаю, что ты выбрал! Напиши \"Камень\" или \"Ножницы\" или \"Бумага\".");

        return false;
    }

    private void replyWhoWin(Receiver receiver, MoveType userMove, int attempts) {
        final MoveType botMove = Random.nextEnum(MoveType.class);

        if (receiver instanceof DiscordReceiver discordReceiver) {
            discordReceiver.replyWithoutDelay(
                    "Я выбираю " + botMove.name
            );

            receiver.reputation(1);
        }

        if (userMove == botMove) {
            if (attempts == 2) {
                receiver.reply("У нас снова ничья! Видимо, не судьба...");
                removeAwaitingListener(receiver.getId());
            } else {
                receiver.reply("Ничья! Давай попробуем еще раз, говори что выбираешь!");
            }

            return;
        }

        if (userMove == MoveType.ROCK && botMove == MoveType.SCISSORS
                || userMove == MoveType.SCISSORS && botMove == MoveType.PAPER
                || userMove == MoveType.PAPER && botMove == MoveType.ROCK) {
            receiver.reply("Вот блин! Ты победил меня...");
        } else {
            receiver.reply("Ура! Я смог выиграть тебя!");
        }

        removeAwaitingListener(receiver.getId());
    }

    @RequiredArgsConstructor
    private enum MoveType {
        ROCK("камень"),
        PAPER("бумагу"),
        SCISSORS("ножницы");

        private final String name;
    }
}
