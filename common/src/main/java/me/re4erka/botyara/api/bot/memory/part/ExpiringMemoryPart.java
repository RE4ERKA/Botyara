package me.re4erka.botyara.api.bot.memory.part;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class ExpiringMemoryPart<E> extends MemoryPart<E> {
    private final Supplier<E> updateMethod;
    private final long duration;

    private long expiration;

    private ExpiringMemoryPart(Supplier<E> updateMethod, long duration) {
        this.updateMethod = updateMethod;
        this.duration = duration;

        updateNow();
    }

    public E updateAndGet() {
        update();

        return part;
    }

    public void update() {
        if (isExpired()) {
            updateNow();
        }
    }

    public void updateNow() {
        expiration = System.currentTimeMillis() + duration;
        set(updateMethod.get());
    }

    private boolean isExpired() {
        return System.currentTimeMillis() > expiration;
    }

    public static <E> ExpiringMemoryPart.Builder<E> builder() {
        return new ExpiringMemoryPart.Builder<>();
    }

    public static final class Builder<E> {
        private Supplier<E> updateMethod;
        private long duration;

        public Builder<E> update(Supplier<E> method) {
            this.updateMethod = method;

            return this;
        }

        public Builder<E> expireAfter(long expiration, TimeUnit unit) {
            this.duration = unit.toMillis(expiration);

            return this;
        }

        public ExpiringMemoryPart<E> build() {
            return new ExpiringMemoryPart<>(updateMethod, duration);
        }
    }
}
