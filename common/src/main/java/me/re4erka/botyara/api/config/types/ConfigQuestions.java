package me.re4erka.botyara.api.config.types;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import me.re4erka.botyara.api.bot.listener.ask.AskType;
import me.re4erka.botyara.api.config.exception.ConfigLoadException;
import me.re4erka.botyara.api.config.message.ConfigMessage;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.ConfigurationSection;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ConfigQuestions {
    private final ImmutableMap<AskType, ConfigMessage> questionMap;

    public ConfigQuestions(@Nullable ConfigurationSection section) throws ConfigLoadException {
        if (section == null) {
            questionMap = ImmutableMap.of();
            return;
        }

        final Set<String> keys = section.getKeys(false);

        if (keys == null) {
            questionMap = ImmutableMap.of();
            return;
        }

        final ImmutableMap.Builder<AskType, ConfigMessage> builder = ImmutableMap.builder();

        for (String key : keys) {
            final ConfigMessage message = new ConfigMessage(
                    section.getConfigurationSection(key)
            );

            try {
                builder.put(AskType.valueOf(key), message);
            } catch (IllegalArgumentException exception) {
                throw new ConfigLoadException("No type was found in the 'Questions' list!");
            }
        }

        this.questionMap = builder.build();
    }

    public boolean isPresent() {
        return Objects.nonNull(questionMap);
    }

    public ImmutableSet<Map.Entry<AskType, ConfigMessage>> entrySet() {
        return questionMap.entrySet();
    }
}
