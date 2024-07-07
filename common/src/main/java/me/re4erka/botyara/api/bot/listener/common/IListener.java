package me.re4erka.botyara.api.bot.listener.common;

import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.Words;
import org.jetbrains.annotations.NotNull;

public interface IListener {
    boolean onListen(@NotNull Receiver receiver, @NotNull Words words);
}
