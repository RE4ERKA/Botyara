package me.re4erka.botyara.api.util.user;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

@UtilityClass
public class UserNameValidation {
    private final static Pattern VALID_REGEX = Pattern.compile("[^А-ЯЁа-яё]");

    public InvalidType valid(@NotNull String name) {
        if (name.length() < 3) {
            return InvalidType.TOO_SMALL;
        }

        /* Лимит символов имени установлен еще и в базе данных, так же 16.
        *
        * Увеличение числа, без изменений в базе данных - приведет к проблемам.
        *  */
        if (name.length() > 16) {
            return InvalidType.TOO_LONG;
        }

        if (VALID_REGEX.matcher(name).find()) {
            return InvalidType.CONTAINS_SPECIFIC_SYMBOLS;
        }

        return InvalidType.NONE;
    }

    public enum InvalidType {
        CONTAINS_SPECIFIC_SYMBOLS,
        TOO_LONG,
        TOO_SMALL,
        NONE
    }
}
