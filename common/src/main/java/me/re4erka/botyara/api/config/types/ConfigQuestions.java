package me.re4erka.botyara.api.config.types;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import me.re4erka.botyara.api.bot.listener.ask.AskType;
import me.re4erka.botyara.api.config.exception.ConfigLoadException;
import me.re4erka.botyara.api.config.message.ConfigMessage;
import org.simpleyaml.configuration.ConfigurationSection;

import java.util.Map;

public class ConfigQuestions {
    private final ImmutableMap<AskType, ConfigMessage> questionMap;

    public ConfigQuestions(ConfigurationSection section) throws ConfigLoadException {
        if (section == null || section.getKeys(false) == null) {
            questionMap = null;
            return;
        }

        ImmutableMap.Builder<AskType, ConfigMessage> builder = ImmutableMap.builder();

        for (String key : section.getKeys(false)) {
            final ConfigMessage message = new ConfigMessage(
                    section.getConfigurationSection(key)
            );

            try {
                builder.put(AskType.valueOf(key), message);
            } catch (IllegalArgumentException exception) {
                throw new ConfigLoadException("Не был найден тип 'questions'!");
            }
        }

        this.questionMap = builder.build();
    }

    public boolean isPresent() {
        return questionMap != null;
    }

    public ImmutableSet<Map.Entry<AskType, ConfigMessage>> entrySet() {
        return questionMap.entrySet();
    }
}
