package me.re4erka.botyara.api.bot.word.random.answer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum WordEnd {
    COMMA(','),
    QUESTION('?'),
    EXCLAMATION('!'),
    DOT('.'),
    EMPTY(Character.MIN_VALUE);

    @Getter
    private final char symbol;
}
