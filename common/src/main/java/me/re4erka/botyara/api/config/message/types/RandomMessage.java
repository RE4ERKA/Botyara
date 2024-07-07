package me.re4erka.botyara.api.config.message.types;

import com.google.common.collect.ImmutableList;
import me.re4erka.botyara.api.config.exception.ConfigLoadException;
import me.re4erka.botyara.api.config.handler.ConfigVariables;
import me.re4erka.botyara.api.config.message.MessageHandler;
import me.re4erka.botyara.api.util.random.Random;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.ConfigurationSection;

public class RandomMessage extends MessageHandler {
    private final ImmutableList<String> messages;

    public RandomMessage(@NotNull ConfigurationSection section) throws ConfigLoadException {
        super(section);

        if (section.getStringList("messages").size() < 2) {
            throw new ConfigLoadException(
                    "The 'messages' field for the 'RANDOM' type cannot contain less than 2 lines."
            );
        }

        if (useLineSkip) {
            final ImmutableList.Builder<String> builder = new ImmutableList.Builder<>();

            section.getStringList("messages")
                    .forEach(message -> builder.add(replaceLinefeed(message)));

            this.messages = builder.build();
        } else {
            this.messages = ImmutableList.copyOf(
                    section.getStringList("messages")
            );
        }
    }

    @Override
    public String handle(@NotNull ConfigVariables variables) {
        final String message = Random.nextElement(messages);

        if (useVariables) {
            return variables.format(message);
        } else {
            return message;
        }
    }
}
