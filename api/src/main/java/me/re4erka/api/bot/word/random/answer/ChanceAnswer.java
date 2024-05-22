package me.re4erka.api.bot.word.random.answer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChanceAnswer {
    private final ChanceWord[] chanceWords;
    private final int sum;

    public String get(int index) {
        for (ChanceWord word : chanceWords) {
            if (word.index() == index) {
                return word.text();
            }
        }

        return null;
    }

    public ChanceWord generate() {
        final int index = ThreadLocalRandom.current().nextInt(sum);

        for (ChanceWord chance : chanceWords) {
            if (chance.lowerLimit() <= index && chance.upperLimit() > index) {
                return chance;
            }
        }

        return null;
    }

    public static ChanceAnswer.Builder builder() {
        return new ChanceAnswer.Builder();
    }

    public static class Builder {
        private final Set<ChanceWord> chanceWords = new HashSet<>();
        private int sum = 0;
        private int index = 0;

        public Builder word(String word, int chance) {
            chanceWords.add(new ChanceWord(index, word, this.sum, this.sum + chance));
            sum += chance;
            index++;

            return Builder.this;
        }

        public ChanceAnswer build() {
            return new ChanceAnswer(
                    chanceWords.toArray(new ChanceWord[0]),
                    sum
            );
        }
    }
}
