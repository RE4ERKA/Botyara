package me.re4erka.api.bot.memory.part;

import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@NoArgsConstructor
public class MemoryPart<E> {
    protected E part;

    public void set(E part) {
        this.part = part;
    }

    public Optional<E> get() {
        return Optional.ofNullable(part);
    }

    public E getOrElse(@NotNull E otherwise) {
        return part == null ? otherwise : part;
    }

    public boolean isEmpty() {
        return part == null;
    }
}
