package me.re4erka.botyara.api.bot.answer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.re4erka.botyara.api.util.random.Random;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

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
        final int index = Random.next(sum);

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
        private short sum = 0;
        private short index = 0;

        public Builder word(@NotNull String word, int chance) {
            if (chance > 100) {
                throw new IllegalArgumentException("The chance cannot be above 100!");
            }

            chanceWords.add(
                    new ChanceWord(index, word, this.sum, (short) (this.sum + chance))
            );

            sum += (short) chance;
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
