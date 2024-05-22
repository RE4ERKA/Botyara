package me.re4erka.api.bot.listener.ask;

import me.re4erka.api.bot.receiver.Receiver;
import me.re4erka.api.bot.word.Words;

public interface IAskListener {
    boolean onAsked(Receiver receiver, Words words);
}
