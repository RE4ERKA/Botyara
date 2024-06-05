package me.re4erka.botyara.api.config.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.re4erka.botyara.api.config.exception.ConfigLoadException;
import me.re4erka.botyara.api.config.message.types.RandomMessage;
import me.re4erka.botyara.api.config.message.types.SingleMessage;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.simpleyaml.configuration.ConfigurationSection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Log4j2
@SuppressWarnings("unused")
@RequiredArgsConstructor
public enum ConfigMessageType {
    SINGLE(SingleMessage.class),
    RANDOM(RandomMessage.class);

    private final Class<? extends MessageHandler> handler;
    private Constructor<? extends MessageHandler> constructor;

    static {
        for (ConfigMessageType type : values()) {
            try {
                type.constructor = type.handler.getConstructor(ConfigurationSection.class);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Failed to initialize ConfigMessageType: no valid constructor found", e);
            }
        }
    }

    public MessageHandler newInstance(final ConfigurationSection section) {
        try {
            return constructor.newInstance(section);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.error(new ParameterizedMessage(
                    "Failed to instantiate message handler for type {}", name()
            ), e);
            return null;
        }
    }
}