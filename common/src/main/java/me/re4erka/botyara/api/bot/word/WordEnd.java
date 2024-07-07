package me.re4erka.botyara.api.bot.word;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WordEnd {
    COMMA(','),
    QUESTION('?'),
    EXCLAMATION('!'),
    DOT('.'),
    EMPTY(Character.MIN_VALUE);

    private final char symbol;
}
