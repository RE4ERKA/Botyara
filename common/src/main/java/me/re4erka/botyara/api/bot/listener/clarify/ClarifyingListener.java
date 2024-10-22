package me.re4erka.botyara.api.bot.listener.clarify;

import me.re4erka.botyara.api.bot.listener.ListeningBot;
import me.re4erka.botyara.api.bot.listener.common.Listener;
import me.re4erka.botyara.api.bot.listener.common.PostOrder;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.api.util.key.Key;
import org.jetbrains.annotations.NotNull;

public abstract class ClarifyingListener extends Listener implements IClarifyingListener {
    private final ListeningBot bot;

    protected ClarifyingListener(@NotNull Key name, @NotNull PostOrder postOrder, @NotNull ListeningBot bot) {
        super(name, postOrder);
        this.bot = bot;
    }

    public abstract boolean onClarify(@NotNull Receiver receiver, @NotNull Words words);

    protected void addClarifyingListener(long id) {
        bot.addClarifyingListener(id, this);
    }
}
