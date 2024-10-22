package me.re4erka.botyara.api.bot.listener.clarify;

import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.Words;

public interface IClarifyingListener {
    boolean onClarify(Receiver receiver, Words words);
}
