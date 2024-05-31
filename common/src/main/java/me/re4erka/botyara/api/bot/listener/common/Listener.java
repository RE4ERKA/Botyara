package me.re4erka.botyara.api.bot.listener.common;

import lombok.Getter;
import me.re4erka.botyara.api.util.key.Key;

@Getter
public abstract class Listener implements IListener {
    private final Key name;
    private final PostOrder postOrder;

    public Listener(Key name) {
        this.name = name;
        this.postOrder = PostOrder.NORMAL;
    }

    public Listener(Key name, PostOrder postOrder) {
        this.name = name;
        this.postOrder = postOrder;
    }
}
