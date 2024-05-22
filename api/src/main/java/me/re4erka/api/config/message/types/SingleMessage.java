package me.re4erka.api.config.message.types;

import me.re4erka.api.config.handler.ConfigVariables;
import me.re4erka.api.config.message.MessageHandler;
import org.apache.commons.lang3.StringUtils;
import org.simpleyaml.configuration.ConfigurationSection;

public class SingleMessage extends MessageHandler {
    private final String message;

    public SingleMessage(ConfigurationSection section) {
        super(section);
        this.message = useLineSkip
                ? StringUtils.replace(section.getString("message"), "\\n", "\n")
                : section.getString("message");
    }

    @Override
    public String get(ConfigVariables variables) {
        if (useVariables) {
            return variables.format(message);
        } else {
            return message;
        }
    }
}
