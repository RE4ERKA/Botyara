package me.re4erka.botyara.api.config.message.types;

import com.google.common.collect.ImmutableList;
import me.re4erka.botyara.api.config.handler.ConfigVariables;
import me.re4erka.botyara.api.config.message.MessageHandler;
import me.re4erka.botyara.api.util.random.Random;
import org.apache.commons.lang3.StringUtils;
import org.simpleyaml.configuration.ConfigurationSection;

public class RandomMessage extends MessageHandler {
    private final ImmutableList<String> messages;

    public RandomMessage(ConfigurationSection section) {
        super(section);

        if (useLineSkip) {
            final ImmutableList.Builder<String> builder = new ImmutableList.Builder<>();

            section.getStringList("messages").forEach(message -> builder.add(
                    StringUtils.replace(message, "\\n", "\n"))
            );

            this.messages = builder.build();
        } else {
            this.messages = ImmutableList.copyOf(
                    section.getStringList("messages")
            );
        }
    }

    @Override
    public String get(ConfigVariables variables) {
        final String message = Random.nextElement(messages);

        if (useVariables) {
            return variables.format(message);
        } else {
            return message;
        }
    }
}
