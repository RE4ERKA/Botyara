package me.re4erka.botyara.api.config.message.types;

import me.re4erka.botyara.api.config.exception.ConfigLoadException;
import me.re4erka.botyara.api.config.handler.ConfigVariables;
import me.re4erka.botyara.api.config.message.MessageHandler;
import org.apache.commons.lang3.StringUtils;
import org.simpleyaml.configuration.ConfigurationSection;

import java.util.Objects;

public class SingleMessage extends MessageHandler {
    private final String message;

    public SingleMessage(ConfigurationSection section) throws ConfigLoadException {
        super(section);

        if (Objects.isNull(section.getString("message"))) {
            throw new ConfigLoadException("The 'message' field for the 'SINGLE' type cannot be empty!");
        }

        this.message = useLineSkip
                ? StringUtils.replace(section.getString("message"), "\\n", StringUtils.LF)
                : section.getString("message");
    }

    @Override
    public String handle(ConfigVariables variables) {
        if (useVariables) {
            return variables.format(message);
        } else {
            return message;
        }
    }
}
