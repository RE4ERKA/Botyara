package me.re4erka.botyara.api.bot.word.replace;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class Replace {
    private final String search;
    private final String replace;

    public static Replace of(@NotNull String search, @NotNull String replace) {
        return new Replace(
                "%" + search + "%",
                replace
        );
    }
}
