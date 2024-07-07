package me.re4erka.botyara.api.config.handler;

import me.re4erka.botyara.api.bot.receiver.Receiver;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfigVariables {
    private final String userName;

    public ConfigVariables(@Nullable String userName) {
        this.userName = userName == null ? "Незнакомец" : userName;
    }

    public String format(@NotNull String message) {
        message = StringUtils.replaceOnce(message, "%user_name%", userName);

        return message;
    }

    public static ConfigVariables from(@NotNull Receiver receiver) {
        return new ConfigVariables(receiver.getName());
    }
}
