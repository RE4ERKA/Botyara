package me.re4erka.botyara.api.bot.listener.common;

import lombok.Getter;
import me.re4erka.botyara.api.util.key.Key;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class Listener implements IListener {
    private final Key name;
    private final PostOrder postOrder;

    public Listener(@NotNull Key name) {
        this.name = name;
        this.postOrder = PostOrder.NORMAL;
    }

    public Listener(@NotNull Key name, @NotNull PostOrder postOrder) {
        this.name = name;
        this.postOrder = postOrder;
    }
}
