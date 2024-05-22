package me.re4erka.api.config.handler;

import me.re4erka.api.bot.receiver.Receiver;
import org.apache.commons.lang3.StringUtils;

public class ConfigVariables {
    private final String userName;

    /* Переделать в будущем когда будет сделана система дружбы */
    public ConfigVariables(String userName) {
        this.userName = userName == null ? "Незнакомец" : userName;
    }

    public String format(String message) {
        message = StringUtils.replaceOnce(message, "%user_name%", userName);

        return message;
    }

    public static ConfigVariables from(Receiver receiver) {
        return new ConfigVariables(receiver.getName());
    }
}
