package me.re4erka.botyara.api.bot.listener.config;

import me.re4erka.botyara.api.bot.listener.ListeningBot;
import me.re4erka.botyara.api.bot.listener.ask.AskListener;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.api.config.handler.ConfigHandler;

public class ConfigListener extends AskListener {
    private final ConfigHandler configHandler;

    public ConfigListener(ConfigHandler configHandler, ListeningBot bot) {
        super(
                configHandler.getListenerName(),
                configHandler.getOptions().getPostOrder(),
                bot
        );

        this.configHandler = configHandler;
    }

    @Override
    public boolean onListen(Receiver receiver, Words words) {
        if (configHandler.handle(receiver, words)) {
            if (configHandler.getQuestions().isPresent()) {
                addAskListener(receiver.getId());
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean onAsked(Receiver receiver, Words words) {
        return configHandler.handleAsk(receiver, words);
    }
}
