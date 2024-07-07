package me.re4erka.botyara.api.config.message;

import me.re4erka.botyara.api.config.handler.ConfigVariables;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.ConfigurationSection;

public abstract class MessageHandler {
    protected final boolean useVariables;
    protected final boolean useLineSkip;

    public MessageHandler(@NotNull ConfigurationSection section) {
        this.useVariables = section.getBoolean("use_variables", false);
        this.useLineSkip = section.getBoolean("use_line_skip", false);
    }

    public abstract String handle(@NotNull ConfigVariables variables);

    protected String replaceLinefeed(@NotNull String message) {
        return StringUtils.replace(message, "\\n", StringUtils.LF);
    }
}
