package me.re4erka.botyara.api.bot.listener.ask;

import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.Words;

public interface IAskListener {
    boolean onAsked(Receiver receiver, Words words);
}
