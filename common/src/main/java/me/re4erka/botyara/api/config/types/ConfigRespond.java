package me.re4erka.botyara.api.config.types;

import me.re4erka.botyara.api.config.exception.ConfigLoadException;
import me.re4erka.botyara.api.config.handler.ConfigVariables;
import me.re4erka.botyara.api.config.message.ConfigMessage;
import org.simpleyaml.configuration.ConfigurationSection;

public class ConfigRespond {
    private final ConfigMessage defaultMessage;
    private final ConfigMessage friendMessage;
    private final ConfigMessage bestFriendMessage;

    public ConfigRespond(ConfigurationSection section) throws ConfigLoadException {
        this.defaultMessage = new ConfigMessage(section.getConfigurationSection("DEFAULT"));

        this.friendMessage = section.contains("FRIEND")
                ? new ConfigMessage(section.getConfigurationSection("FRIEND"))
                : null;

        this.bestFriendMessage = section.contains("BEST_FRIEND")
                ? new ConfigMessage(section.getConfigurationSection("BEST_FRIEND"))
                : null;
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
