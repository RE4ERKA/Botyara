package me.re4erka.botyara.api.bot.listener;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import me.re4erka.botyara.api.bot.Bot;
import me.re4erka.botyara.api.bot.listener.common.Listener;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.listener.clarify.IClarifyingListener;
import me.re4erka.botyara.api.bot.listener.wait.WaitingListener;
import me.re4erka.botyara.api.bot.response.PendingResponse;
import me.re4erka.botyara.api.bot.word.Words;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private final Map<Long, WaitingListener> waitingListeners;

    private final Map<Long, IClarifyingListener> clarifyingListeners;

    private final Deque<PendingResponse> responses = new ArrayDeque<>();
    private final AtomicBoolean isResponding = new AtomicBoolean(false);

    public ListeningBot(int waitingListenersSize, int clarifyingListenersSize) {
        highestListeners = new HashSet<>();
        normalListeners = new HashSet<>();
        lowestListeners = new HashSet<>();

        waitingListeners = new ConcurrentHashMap<>(
                waitingListenersSize
        );

        clarifyingListeners = new LinkedHashMap<>(
                clarifyingListenersSize
        );
    }

    protected abstract void onListen(@NotNull Receiver receiver, @NotNull Words words);

    @Override
    public ListeningBot cleanUp() {
        waitingListeners.clear();
        clarifyingListeners.clear();
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

    public void beginResponse() {
        isResponding.compareAndSet(false, true);
    }

    public void finishResponse() {
        isResponding.compareAndSet(true, false);
    }

    public void queueResponse(@NotNull PendingResponse response) {
        responses.addLast(response);
    }

    public void processNextResponse() {
        final PendingResponse response = responses.pollFirst();

        if (response == null) {
            finishResponse();
            return;
        }

        this.onListen(response.getReceiver(), response.getWords());
    }

    public boolean isResponding() {
        return isResponding.get();
    }

    public boolean hasPendingResponses() {
        return !responses.isEmpty();
    }

    protected boolean listenOther(@NotNull Receiver receiver, @NotNull Words words) {
        if (listenAll(receiver, words, highestListeners)) {
            return true;
        }

        if (listenAll(receiver, words, normalListeners)) {
            return true;
        }

        return listenAll(receiver, words, lowestListeners);
    }

    protected boolean listenAwaiting(@NotNull Receiver receiver, @NotNull Words words) {
        final WaitingListener listener = waitingListeners.get(
                receiver.getId()
        );

        if (listener == null) {
            return false;
        }

        // Иногда размер может быть 0, проверяем, дабы избегать багов.
        if (words.size() == 0) {
            return false;
        }

        return listener.onWaitingListen(receiver, words);
    }

    protected boolean listenAsk(@NotNull Receiver receiver, @NotNull Words words) {
        final IClarifyingListener listener = clarifyingListeners.get(
                receiver.getId()
        );

        if (listener == null) {
            return false;
        } else {
            clarifyingListeners.remove(
                    receiver.getId()
            );
        }

        return listener.onClarify(receiver, words);
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

    public void addWaitingListener(long id, @NotNull WaitingListener listener) {
        waitingListeners.put(id, listener);
    }

    public void removeWaitingListener(long id) {
        waitingListeners.remove(id);
    }

    public void addClarifyingListener(long id, @NotNull IClarifyingListener listener) {
        clarifyingListeners.put(id, listener);
    }
}
