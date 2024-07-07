package me.re4erka.botyara.api.bot.word.random;

import me.re4erka.botyara.api.bot.word.WordEnd;
import me.re4erka.botyara.api.util.random.Random;
import org.jetbrains.annotations.NotNull;

public class RandomWord {
    private final String[] words;
    private final WordEnd wordEnd;

    public RandomWord(@NotNull String[] words, @NotNull WordEnd wordEnd) {
        this.words = words;
        this.wordEnd = wordEnd;
    }

    public String generate() {
        if (words.length == 1) {
            return words[0] + wordEnd.getSymbol();
        }

        return words[Random.next(words.length)] + wordEnd.getSymbol();
    }
}
