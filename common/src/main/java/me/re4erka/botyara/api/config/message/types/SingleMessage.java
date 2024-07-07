package me.re4erka.botyara.api.config.message.types;

import me.re4erka.botyara.api.config.exception.ConfigLoadException;
import me.re4erka.botyara.api.config.handler.ConfigVariables;
import me.re4erka.botyara.api.config.message.MessageHandler;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.ConfigurationSection;

public class SingleMessage extends MessageHandler {
    private final String message;

    public SingleMessage(@NotNull ConfigurationSection section) throws ConfigLoadException {
        super(section);

        if (!section.isSet("message")) {
            throw new ConfigLoadException("The 'message' field for the 'SINGLE' type cannot be empty!");
        }

        this.message = useLineSkip
                ? replaceLinefeed(section.getString("message"))
                : section.getString("message");
    }

    @Override
    public String handle(@NotNull ConfigVariables variables) {
        if (useVariables) {
            return variables.format(message);
        } else {
            return message;
        }
    }
}
