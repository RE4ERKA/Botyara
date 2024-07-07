package me.re4erka.botyara.api.bot.listener.config;

import me.re4erka.botyara.api.bot.listener.ListeningBot;
import me.re4erka.botyara.api.bot.listener.ask.AskListener;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.api.config.handler.ConfigHandler;
import org.jetbrains.annotations.NotNull;

public class ConfigListener extends AskListener {
    private final ConfigHandler configHandler;

    public ConfigListener(@NotNull ConfigHandler configHandler, @NotNull ListeningBot bot) {
        super(
                configHandler.getListenerName(),
                configHandler.getOptions().getPostOrder(),
                bot
        );

        this.configHandler = configHandler;
    }

    @Override
    public boolean onListen(@NotNull Receiver receiver, @NotNull Words words) {
        if (configHandler.handle(receiver, words)) {
            if (configHandler.getQuestions().isPresent()) {
                addAskListener(receiver.getId());
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean onAsked(@NotNull Receiver receiver, @NotNull Words words) {
        return configHandler.handleAsk(receiver, words);
    }
}
