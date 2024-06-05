package me.re4erka.botyara.api.config.handler;

import lombok.Getter;
import me.re4erka.botyara.api.bot.listener.ask.AskType;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.api.config.exception.ConfigLoadException;
import me.re4erka.botyara.api.config.message.ConfigMessage;
import me.re4erka.botyara.api.config.types.ConfigOptions;
import me.re4erka.botyara.api.config.types.ConfigQuestions;
import me.re4erka.botyara.api.config.types.ConfigRespond;
import me.re4erka.botyara.api.bot.word.search.SearchWords;
import me.re4erka.botyara.api.util.key.Key;
import org.simpleyaml.configuration.file.YamlConfiguration;

import java.util.Map;

@Getter
public class ConfigHandler {
    private final Key listenerName;

    private final SearchWords searchWords;
    private final SearchWords matchesWords;

    private final ConfigOptions options;
    private final ConfigRespond respond;
    private final ConfigQuestions questions;

    public ConfigHandler(Key listenerName, YamlConfiguration configuration) throws ConfigLoadException {
        this.listenerName = listenerName;

        this.searchWords = SearchWords.of(configuration.getStringList("Contains"));
        this.matchesWords = SearchWords.of(configuration.getStringList("Matches"));

        if (searchWords.size() == 0
                && matchesWords.size() == 0) {
            throw new ConfigLoadException(
                    listenerName,
                    "The 'Contains' and 'Matches' list can't be empty!"
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

    public boolean handle(Receiver receiver, Words words) {
        if (words.containsAny(searchWords) || words.matchesAny(matchesWords)) {
            final ConfigMessage message = switch (receiver.getFriendshipType()) {
                case STRANGER -> respond.getMessageForStranger();
                case FAMILIAR -> respond.getMessageForFamiliar();
                case FRIEND -> respond.getMessageForFriend();
                case BEST_FRIEND -> respond.getMessageForBestFriend();
            };

            final ConfigVariables variables = ConfigVariables.from(receiver);

            receiver.reply(
                    message.getHandler()
                            .handle(variables)
            );

            if (message.getReputation() > 1) {
                receiver.reputation(
                        message.getReputation()
                );
            }

            return true;
        }

        return false;
    }

    public boolean handleAsk(Receiver receiver, Words words) {
        for (Map.Entry<AskType, ConfigMessage> question : questions.entrySet()) {
            if (words.containsAny(question.getKey().getSearchWords())) {
                receiver.reply(
                        question.getValue().getHandler().handle(
                                ConfigVariables.from(receiver)
                        )
                );

                return true;
            }
        }

        return false;
    }
}
