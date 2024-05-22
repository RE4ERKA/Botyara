package me.re4erka.api.config.types;

import lombok.Getter;
import me.re4erka.api.bot.listener.common.PostOrder;
import org.simpleyaml.configuration.ConfigurationSection;

@Getter
public class ConfigOptions {
    private final PostOrder postOrder;

    public ConfigOptions(ConfigurationSection section) {
        this.postOrder = PostOrder.valueOf(section.getString("post_order", "NORMAL"));
    }
}
