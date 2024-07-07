package me.re4erka.botyara.api.config.message;

import lombok.Getter;
import me.re4erka.botyara.api.config.exception.ConfigLoadException;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.ConfigurationSection;

@Getter
public class ConfigMessage {
    private final MessageHandler handler;
    private final int reputation;

    public ConfigMessage(@Nullable ConfigurationSection section) throws ConfigLoadException {
        if (section == null) {
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
