package me.re4erka.botyara.api.config.message;

import me.re4erka.botyara.api.config.exception.ConfigLoadException;
import me.re4erka.botyara.api.config.handler.ConfigResult;
import me.re4erka.botyara.api.config.handler.ConfigVariables;
import org.simpleyaml.configuration.ConfigurationSection;

public class ConfigMessage implements ConfigResult {
    private final MessageHandler message;

    public ConfigMessage(ConfigurationSection section) throws ConfigLoadException {
        final ConfigMessageType type;
        try {
            type = ConfigMessageType.valueOf(section.getString("type", "SINGLE"));
        } catch (IllegalArgumentException e) {
            throw new ConfigLoadException("В строке 'type' не был найден тип сообщения!");
        }

        this.message = type.newInstance(section);
    }

    @Override
    public String get(ConfigVariables variables) {
        return message.get(variables);
    }
}
