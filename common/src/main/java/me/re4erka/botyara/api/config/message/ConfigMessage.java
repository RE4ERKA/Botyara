package me.re4erka.botyara.api.config.message;

import lombok.Getter;
import me.re4erka.botyara.api.config.exception.ConfigLoadException;
import org.simpleyaml.configuration.ConfigurationSection;

import java.util.Objects;

@Getter
public class ConfigMessage {
    private final MessageHandler handler;
    private final int reputation;

    public ConfigMessage(ConfigurationSection section) throws ConfigLoadException {
        if (Objects.isNull(section)) {
            throw new ConfigLoadException("The message section cannot be empty!");
        }

        final ConfigMessageType type;
        try {
            type = ConfigMessageType.valueOf(
                    section.getString("type", "SINGLE")
            );
        } catch (IllegalArgumentException e) {
            throw new ConfigLoadException("A type was not found in the 'type' field!");
        }

        this.handler = type.newInstance(section);
        this.reputation = section.getInt("reputation", 0);
    }
}
