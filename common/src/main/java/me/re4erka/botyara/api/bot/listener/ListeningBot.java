package me.re4erka.botyara.api.bot.listener;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import me.re4erka.botyara.api.bot.Bot;
import me.re4erka.botyara.api.bot.listener.common.Listener;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.listener.ask.IAskListener;
import me.re4erka.botyara.api.bot.listener.await.AwaitingListener;
import me.re4erka.botyara.api.bot.word.Words;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ListeningBot extends Bot {
    /*
     * Списоки всех слушателей бота в разных приоритетах.
     *
     * Тем выше приоритет, тем первее выполнится слушатель.
     * */
    private final Set<Listener> highestListeners;
    private final Set<Listener> normalListeners;
    private final Set<Listener> lowestListeners;

    /*
     * Список ожидающих слушателей без вызова бота.
     *
     * Является картой потому что Бот всегда ожидает ответ
     * от конкретного айди пользователя (ключа карты).
     *
     * В карту добавляются элементы в слушателях.
     *
     * Из карты убирается элемент в том случаи если бот
     * дождался ответа, прошло 3 попытки, карта переполнена.
     *
     * Карта является потокобезопасной.
     * */
    private final Map<Long, AwaitingListener> awaitingListeners;

    private final Map<Long, IAskListener> askListeners;

    public ListeningBot(int awaitingListenersSize, int askListenersSize) {
        highestListeners = new HashSet<>();
        normalListeners = new HashSet<>();
        lowestListeners = new HashSet<>();

        awaitingListeners = new ConcurrentHashMap<>(
                awaitingListenersSize
        );

        askListeners = new LinkedHashMap<>(
                askListenersSize
        );
    }

    @Override
    public ListeningBot cleanUp() {
        awaitingListeners.clear();
        askListeners.clear();
        return this;
    }

    public ListeningBot register(@NotNull Listener listener) {
        switch (listener.getPostOrder()) {
            case FIRST -> highestListeners.add(listener);
            case NORMAL -> normalListeners.add(listener);
            case LAST -> lowestListeners.add(listener);
        }
        return this;
    }

    public boolean unregister(@NotNull String name) {
        final String formattedName = name.toUpperCase(Locale.ROOT);

        for (Listener listener : highestListeners) {
            if (listener.getName().equals(formattedName)) {
                highestListeners.remove(listener);
                return true;
            }
        }

        for (Listener listener : normalListeners) {
            if (listener.getName().equals(formattedName)) {
                normalListeners.remove(listener);
                return true;
            }
        }

        for (Listener listener : lowestListeners) {
            if (listener.getName().equals(formattedName)) {
                lowestListeners.remove(listener);
                return true;
            }
        }

        return false;
    }

    public void unregisterAll() {
        highestListeners.clear();
        normalListeners.clear();
        lowestListeners.clear();
    }

    protected boolean listen(@NotNull Receiver receiver, @NotNull Words words) {
        if (listenAll(receiver, words, highestListeners)) {
            return true;
        }

        if (listenAll(receiver, words, normalListeners)) {
            return true;
        }

        return listenAll(receiver, words, lowestListeners);
    }

    protected boolean listenAwaiting(@NotNull Receiver receiver, @NotNull Words words) {
        final AwaitingListener listener = awaitingListeners.get(
                receiver.getId()
        );

        if (listener == null) {
            return false;
        }

        // Иногда размер может быть 0, проверяем, дабы избегать багов.
        if (words.size() == 0) {
            return false;
        }

        return listener.onAwaitingListen(receiver, words);
    }

    protected boolean listenAsk(@NotNull Receiver receiver, @NotNull Words words) {
        final IAskListener listener = askListeners.get(
                receiver.getId()
        );

        if (listener == null) {
            return false;
        } else {
            askListeners.remove(
                    receiver.getId()
            );
        }

        return listener.onAsked(receiver, words);
    }

    private boolean listenAll(@NotNull Receiver receiver, @NotNull Words words, @NotNull Collection<Listener> listeners) {
        for (final Listener listener : listeners) {
            if (listener.onListen(receiver, words)) {
                return true;
            }
        }

        return false;
    }

    public ImmutableSet<Listener> getListeners() {
        return Sets.union(
                Sets.union(highestListeners, normalListeners),
                lowestListeners
        ).immutableCopy();
    }

    public void addAwaitingListener(long id, @NotNull AwaitingListener listener) {
        awaitingListeners.put(id, listener);
    }

    public void removeAwaitingListener(long id) {
        awaitingListeners.remove(id);
    }

    public void addAskListener(long id, @NotNull IAskListener listener) {
        askListeners.put(id, listener);
    }
}
