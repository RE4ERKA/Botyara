package me.re4erka.botyara.api.config.message;

import me.re4erka.botyara.api.config.exception.ConfigLoadException;
import me.re4erka.botyara.api.config.handler.ConfigResult;
import me.re4erka.botyara.api.config.handler.ConfigVariables;
import org.simpleyaml.configuration.ConfigurationSection;

import java.util.Objects;

public abstract class MessageHandler implements ConfigResult {
    protected final boolean useVariables;
    protected final boolean useLineSkip;

    public MessageHandler(ConfigurationSection section) throws ConfigLoadException {
        if (Objects.isNull(section)) {
            throw new ConfigLoadException("The message section cannot be empty!");
        }

        this.useVariables = section.getBoolean("use_variables", false);
        this.useLineSkip = section.getBoolean("use_line_skip", false);
    }

    public abstract String get(ConfigVariables variables);
}
