package me.re4erka.botyara.api.bot.listener.common;

import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.Words;

public interface IListener {
    boolean onListen(Receiver receiver, Words words);
}
