package me.re4erka.botyara.api.bot.listener.ask;

import me.re4erka.botyara.api.bot.listener.ListeningBot;
import me.re4erka.botyara.api.bot.listener.common.Listener;
import me.re4erka.botyara.api.bot.listener.common.PostOrder;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.Words;

public abstract class AskListener extends Listener implements IAskListener {
    private final ListeningBot bot;

    protected AskListener(String name, PostOrder postOrder, ListeningBot bot) {
        super(name, postOrder);
        this.bot = bot;
    }

    public abstract boolean onAsked(Receiver receiver, Words words);

    protected void addAskListener(long id) {
        bot.addAskListener(id, this);
    }
}
