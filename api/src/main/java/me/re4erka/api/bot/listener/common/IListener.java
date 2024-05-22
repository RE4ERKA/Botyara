package me.re4erka.api.bot.listener.common;

import me.re4erka.api.bot.receiver.Receiver;
import me.re4erka.api.bot.word.Words;

public interface IListener {
    boolean onListen(Receiver receiver, Words words);
}
