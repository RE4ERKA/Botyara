package me.re4erka.api.bot.listener.await;

import me.re4erka.api.bot.listener.ListeningBot;
import me.re4erka.api.bot.listener.common.Listener;
import me.re4erka.api.bot.listener.common.PostOrder;
import me.re4erka.api.bot.receiver.Receiver;
import me.re4erka.api.bot.word.Words;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class AwaitingListener extends Listener {
    /* Попыток дождаться ответа от пользователя после сообщения */
    private final AtomicInteger attempts = new AtomicInteger(0);

    private final ListeningBot bot;

    public AwaitingListener(String name, ListeningBot bot) {
        super(name);
        this.bot = bot;
    }

    public AwaitingListener(String name, PostOrder postOrder, ListeningBot bot) {
        super(name, postOrder);
        this.bot = bot;
    }

    /* Вызывается до основных слушателей, когда Бот ожидает ответа. */
    public boolean onAwaitingListen(Receiver receiver, Words words) {
        /* Проверяем количество попыток с возможным максимум */
        if (attempts.get() == 3) {
            removeAwaitingListener(receiver.getId());

            return false;
        }

        return onAwaitingListen(receiver, words, attempts.incrementAndGet());
    }

    /* Вызывается когда Бот ожидает ответа от пользователя */
    protected abstract boolean onAwaitingListen(Receiver receiver, Words words, int attempts);

    protected void addAwaitingListener(long id) {
        bot.addAwaitingListener(id, this);
    }

    protected void removeAwaitingListener(long id) {
        attempts.set(0);

        bot.removeAwaitingListener(id);
    }
}
