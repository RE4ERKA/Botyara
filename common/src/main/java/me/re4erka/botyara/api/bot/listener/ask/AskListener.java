package me.re4erka.botyara.api.bot.listener.ask;

import me.re4erka.botyara.api.bot.listener.ListeningBot;
import me.re4erka.botyara.api.bot.listener.common.Listener;
import me.re4erka.botyara.api.bot.listener.common.PostOrder;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.api.util.key.Key;
import org.jetbrains.annotations.NotNull;

public abstract class AskListener extends Listener implements IAskListener {
    private final ListeningBot bot;

    protected AskListener(@NotNull Key name, @NotNull PostOrder postOrder, @NotNull ListeningBot bot) {
        super(name, postOrder);
        this.bot = bot;
    }

    public abstract boolean onAsked(@NotNull Receiver receiver, @NotNull Words words);

    protected void addAskListener(long id) {
        bot.addAskListener(id, this);
    }
}
