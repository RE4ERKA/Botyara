package me.re4erka.botyara.api.config.types;

import lombok.Getter;
import me.re4erka.botyara.api.bot.listener.common.PostOrder;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.ConfigurationSection;

@Getter
public class ConfigOptions {
    private final PostOrder postOrder;

    public ConfigOptions(@Nullable ConfigurationSection section) {
        this.postOrder = section == null
                ? PostOrder.NORMAL
                : PostOrder.valueOf(section.getString("post_order", "NORMAL"));
    }
}
