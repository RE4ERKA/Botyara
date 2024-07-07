package me.re4erka.botyara.api.bot.listener.await;

import me.re4erka.botyara.api.bot.listener.ListeningBot;
import me.re4erka.botyara.api.bot.listener.common.Listener;
import me.re4erka.botyara.api.bot.listener.common.PostOrder;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.api.util.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class AwaitingListener extends Listener {
    /* Попыток дождаться ответа от пользователя после сообщения */
    private final AtomicInteger attempts = new AtomicInteger(0);

    private final ListeningBot bot;

    public AwaitingListener(@NotNull Key name, @NotNull ListeningBot bot) {
        super(name);
        this.bot = bot;
    }

    public AwaitingListener(@NotNull Key name, @NotNull PostOrder postOrder, @NotNull ListeningBot bot) {
        super(name, postOrder);
        this.bot = bot;
    }

    /* Вызывается до основных слушателей, когда Бот ожидает ответа. */
    public boolean onAwaitingListen(@NotNull Receiver receiver, @NotNull Words words) {
        /* Проверяем количество попыток с возможным максимум */
        if (attempts.get() == 3) {
            removeAwaitingListener(receiver.getId());

            return false;
        }

        return onAwaitingListen(receiver, words, attempts.incrementAndGet());
    }

    /* Вызывается когда Бот ожидает ответа от пользователя */
    protected abstract boolean onAwaitingListen(@NotNull Receiver receiver, @NotNull Words words, int attempts);

    protected void addAwaitingListener(long id) {
        bot.addAwaitingListener(id, this);
    }

    protected void removeAwaitingListener(long id) {
        attempts.set(0);

        bot.removeAwaitingListener(id);
    }
}
