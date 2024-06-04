package me.re4erka.botyara.api.config.handler;

import me.re4erka.botyara.api.bot.receiver.Receiver;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class ConfigVariables {
    private final String userName;

    public ConfigVariables(String userName) {
        this.userName = Objects.isNull(userName) ? "Незнакомец" : userName;
    }

    public String format(String message) {
        message = StringUtils.replaceOnce(message, "%user_name%", userName);

        return message;
    }

    public static ConfigVariables from(Receiver receiver) {
        return new ConfigVariables(receiver.getName());
    }
}
