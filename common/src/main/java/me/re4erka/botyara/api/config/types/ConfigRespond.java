package me.re4erka.botyara.api.config.types;

import me.re4erka.botyara.api.config.exception.ConfigLoadException;
import me.re4erka.botyara.api.config.handler.ConfigVariables;
import me.re4erka.botyara.api.config.message.ConfigMessage;
import org.simpleyaml.configuration.ConfigurationSection;

import java.util.Objects;

public class ConfigRespond {
    private final ConfigMessage defaultMessage;
    private final ConfigMessage friendMessage;
    private final ConfigMessage bestFriendMessage;

    public ConfigRespond(ConfigurationSection section) throws ConfigLoadException {
        if (Objects.isNull(section)) {
            throw new ConfigLoadException("The 'Response' list cannot be empty!");
        }

        this.defaultMessage = section.contains("DEFAULT")
                ? new ConfigMessage(section.getConfigurationSection("DEFAULT"))
                : null;

        this.friendMessage = section.contains("FRIEND")
                ? new ConfigMessage(section.getConfigurationSection("FRIEND"))
                : null;

        this.bestFriendMessage = section.contains("BEST_FRIEND")
                ? new ConfigMessage(section.getConfigurationSection("BEST_FRIEND"))
                : null;

        if (Objects.isNull(defaultMessage)
                && Objects.isNull(friendMessage)
                && Objects.isNull(bestFriendMessage)) {
            throw new ConfigLoadException("There must be at least one response!");
        }
    }

    public String getDefault(ConfigVariables variables) {
        return defaultMessage.get(variables);
    }

    public String getIfFriend(ConfigVariables variables) {
        return friendMessage == null ? getDefault(variables) : friendMessage.get(variables);
    }

    public String getIfBestFriend(ConfigVariables variables) {
        return bestFriendMessage == null ? getIfFriend(variables) : bestFriendMessage.get(variables);
    }
}
