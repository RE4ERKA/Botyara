package me.re4erka.api.config.message;

import me.re4erka.api.config.handler.ConfigResult;
import me.re4erka.api.config.handler.ConfigVariables;
import org.simpleyaml.configuration.ConfigurationSection;

public abstract class MessageHandler implements ConfigResult {
    protected final boolean useVariables;
    protected final boolean useLineSkip;

    public MessageHandler(ConfigurationSection section) {
        this.useVariables = section.getBoolean("use_variables", false);
        this.useLineSkip = section.getBoolean("use_line_skip", false);
    }

    public abstract String get(ConfigVariables variables);
}
