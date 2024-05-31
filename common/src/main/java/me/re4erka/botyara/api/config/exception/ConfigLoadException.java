package me.re4erka.botyara.api.config.exception;

import lombok.Getter;
import lombok.Setter;

public class ConfigLoadException extends Exception {
    @Getter @Setter
    private String listenerName;

    public ConfigLoadException(String message) {
        super(message);
        this.listenerName = null;
    }

    public ConfigLoadException(String listenerName, String message) {
        super(message);
        this.listenerName = listenerName;
    }
}
