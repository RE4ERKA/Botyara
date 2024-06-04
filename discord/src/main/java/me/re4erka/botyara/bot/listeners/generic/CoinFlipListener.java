package me.re4erka.botyara.bot.listeners.generic;

import lombok.RequiredArgsConstructor;
import me.re4erka.botyara.api.bot.listener.common.Listener;
import me.re4erka.botyara.api.bot.listener.common.PostOrder;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.api.bot.word.search.SearchWords;
import me.re4erka.botyara.api.util.key.Key;
import me.re4erka.botyara.api.util.random.Random;
import me.re4erka.botyara.bot.receiver.DiscordReceiver;
import me.re4erka.botyara.executor.ScheduledExecutor;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class CoinFlipListener extends Listener {
    private final SearchWords searchWords = SearchWords.builder()
            .words("подбрось монетку")
            .words("подкинь монетку")
            .words("брось монетку")
            .words("кинь монетку")
            .words("подбрось монету")
            .words("подкинь монету")
            .words("брось монету")
            .words("кинь монету")
            .build();

    public CoinFlipListener() {
        super(Key.of("FLIP_A_COIN"), PostOrder.NORMAL);
    }

    @Override
    public boolean onListen(Receiver receiver, Words words) {
        if (words.containsAny(searchWords)) {
            if (receiver instanceof DiscordReceiver discordReceiver) {
                discordReceiver.replyThenRun(
                        "Подкинул монетку... *летит*",
                        message -> ScheduledExecutor.executeLater(
                                () -> message.edit(
                                        "Подкинул монетку... " + Random.nextEnum(CoinSides.class).name
                                ),
                                Random.range(1,2), TimeUnit.SECONDS
                        )
                ).reputation(1);
            }

            return true;
        }

        return false;
    }

    @RequiredArgsConstructor()
    private enum CoinSides {
        FRONT("Решка"),
        BACK("Орёл");

        private final String name;
    }
}
