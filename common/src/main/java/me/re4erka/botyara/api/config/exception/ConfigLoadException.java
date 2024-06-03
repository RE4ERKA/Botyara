package me.re4erka.botyara.api.config.exception;

import lombok.Getter;
import lombok.Setter;
import me.re4erka.botyara.api.util.key.Key;

public class ConfigLoadException extends Exception {
    @Getter @Setter
    private Key listenerName;

    public ConfigLoadException(String message) {
        this(null, message);
    }

    public ConfigLoadException(Key listenerName, String message) {
        super(message);
        this.listenerName = listenerName;
    }
}
