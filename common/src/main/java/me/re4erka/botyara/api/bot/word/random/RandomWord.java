package me.re4erka.botyara.api.bot.word.random;

import me.re4erka.botyara.api.bot.word.random.answer.WordEnd;
import me.re4erka.botyara.api.util.random.Random;

import java.util.List;

public class RandomWord {
    private final List<String> words;
    private final WordEnd wordEnd;

    private final int wordsLength;

    public RandomWord(List<String> words, WordEnd wordEnd) {
        this.words = words;
        this.wordEnd = wordEnd;

        this.wordsLength = words.size();
    }

    public String generate() {
        if (wordsLength == 0) {
            return words.get(0) + wordEnd.getSymbol();
        }

        return words.get(
                Random.next(wordsLength)
        ) + wordEnd.getSymbol();
    }
}
