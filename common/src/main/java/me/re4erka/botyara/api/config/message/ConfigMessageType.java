package me.re4erka.botyara.api.config.message;

import lombok.RequiredArgsConstructor;
import me.re4erka.botyara.api.config.message.types.RandomMessage;
import me.re4erka.botyara.api.config.message.types.SingleMessage;
import org.simpleyaml.configuration.ConfigurationSection;

import java.lang.reflect.InvocationTargetException;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public enum ConfigMessageType {
    SINGLE(SingleMessage.class),
    RANDOM(RandomMessage.class);

    private final Class<? extends MessageHandler> handler;

    public MessageHandler newInstance(final ConfigurationSection section) {
        try {
            return handler.getConstructor(
                    ConfigurationSection.class
            ).newInstance(section);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
