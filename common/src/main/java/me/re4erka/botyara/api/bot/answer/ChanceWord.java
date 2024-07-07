package me.re4erka.botyara.api.bot.answer;

import org.jetbrains.annotations.NotNull;

public record ChanceWord(short index, @NotNull String text, short lowerLimit, short upperLimit) {
}
