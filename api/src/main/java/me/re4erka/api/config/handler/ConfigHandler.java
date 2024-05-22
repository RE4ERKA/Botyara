package me.re4erka.api.config.handler;

import lombok.Getter;
import me.re4erka.api.bot.listener.ask.AskType;
import me.re4erka.api.bot.receiver.Receiver;
import me.re4erka.api.bot.word.Words;
import me.re4erka.api.config.exception.ConfigLoadException;
import me.re4erka.api.config.message.ConfigMessage;
import me.re4erka.api.config.types.ConfigOptions;
import me.re4erka.api.config.types.ConfigQuestions;
import me.re4erka.api.config.types.ConfigRespond;
import me.re4erka.api.bot.word.search.SearchWords;
import org.simpleyaml.configuration.file.YamlConfiguration;

import java.util.Map;

@Getter
public class ConfigHandler {
    private final String listenerName;

    private final SearchWords searchWords;

    private final ConfigOptions options;
    private final ConfigRespond respond;
    private final ConfigQuestions questions;

    public ConfigHandler(String listenerName, YamlConfiguration configuration) throws ConfigLoadException {
        this.listenerName = listenerName;

        this.searchWords = SearchWords.of(configuration.getStringList("Contains"));

        if (searchWords.size() == 0) {
            throw new ConfigLoadException(
                    listenerName,
                    "The 'Contains' list can't be empty!"
            );
        }

        try {
            this.options = new ConfigOptions(configuration.getConfigurationSection("Options"));
            this.respond = new ConfigRespond(configuration.getConfigurationSection("Respond"));
            this.questions = new ConfigQuestions(configuration.getConfigurationSection("Questions"));
        } catch (ConfigLoadException exception) {
            exception.setListenerName(listenerName);
            throw exception;
        }
    }

    public boolean handleAsk(Receiver receiver, Words words) {
        for (Map.Entry<AskType, ConfigMessage> question : questions.entrySet()) {
            if (words.containsAny(question.getKey().getSearchWords())) {
                receiver.reply(
                        question.getValue().get(
                                ConfigVariables.from(receiver)
                        )
                );

                return true;
            }
        }

        return false;
    }

    public boolean handle(Receiver receiver, Words words) {
        if (words.containsAny(searchWords)) {
            final ConfigVariables variables = ConfigVariables.from(receiver);

            switch (receiver.getFriendshipType()) {
                case STRANGER, FAMILIAR -> receiver.reply(
                        respond.getDefault(variables)
                );

                case FRIEND -> receiver.reply(
                        respond.getIfFriend(variables)
                );

                case BEST_FRIEND -> receiver.reply(
                        respond.getIfBestFriend(variables)
                );
            }

            return true;
        }

        return false;
    }
}
