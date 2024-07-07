package me.re4erka.botyara.api.bot.memory.part;

import me.re4erka.botyara.api.util.random.Random;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class ExpiringMemoryPart<E> extends MemoryPart<E> {
    private final Supplier<E> updateMethod;

    private final int origin;
    private final int bound;

    private long expiration;

    private ExpiringMemoryPart(@NotNull Supplier<E> updateMethod, int origin, int bound) {
        this.updateMethod = updateMethod;

        this.origin = origin;
        this.bound = bound;

        this.updateNow();
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
        expiration = System.currentTimeMillis() + Random.range(origin, bound);
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

        private int origin;
        private int bound;

        public Builder<E> update(@NotNull Supplier<E> method) {
            this.updateMethod = method;

            return this;
        }

        public Builder<E> expireAfter(long origin, long bound, @NotNull TimeUnit unit) {
            this.origin = Math.toIntExact(unit.toMillis(origin));
            this.bound = Math.toIntExact(unit.toMillis(bound));

            return this;
        }

        public ExpiringMemoryPart<E> build() {
            return new ExpiringMemoryPart<>(updateMethod, origin, bound);
        }
    }
}
