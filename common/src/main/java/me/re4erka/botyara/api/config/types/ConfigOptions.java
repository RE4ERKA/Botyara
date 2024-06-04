package me.re4erka.botyara.api.config.types;

import lombok.Getter;
import me.re4erka.botyara.api.bot.listener.common.PostOrder;
import org.simpleyaml.configuration.ConfigurationSection;

import java.util.Objects;

@Getter
public class ConfigOptions {
    private final PostOrder postOrder;

    public ConfigOptions(ConfigurationSection section) {
        this.postOrder = Objects.isNull(section)
                ? PostOrder.NORMAL
                : PostOrder.valueOf(section.getString("post_order", "NORMAL"));
    }
}
