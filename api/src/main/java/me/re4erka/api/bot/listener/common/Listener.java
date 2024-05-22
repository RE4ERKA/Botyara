package me.re4erka.api.bot.listener.common;

import lombok.Getter;

@Getter
public abstract class Listener implements IListener {
    private final String name;
    private final PostOrder postOrder;

    public Listener(String name) {
        this.name = name;
        this.postOrder = PostOrder.NORMAL;
    }

    public Listener(String name, PostOrder postOrder) {
        this.name = name;
        this.postOrder = postOrder;
    }
}
