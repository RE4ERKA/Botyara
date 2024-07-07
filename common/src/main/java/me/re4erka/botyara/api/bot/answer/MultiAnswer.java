package me.re4erka.botyara.api.bot.answer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.re4erka.botyara.api.bot.word.WordEnd;
import me.re4erka.botyara.api.bot.word.random.RandomWord;
import me.re4erka.botyara.api.bot.word.replace.Replace;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MultiAnswer {
    private final RandomWord[] randomWords;

    public String generate() {
        final StringBuilder builder = new StringBuilder();

        for (RandomWord randomWord : randomWords) {
            builder.append(randomWord.generate())
                    .append(StringUtils.SPACE);
        }

        return builder.toString();
    }

    public String generate(@NotNull Replace replace) {
        final StringBuilder builder = new StringBuilder();

        for (RandomWord randomWord : randomWords) {
            final String word = StringUtils.replaceOnce(
                    randomWord.generate(),
                    replace.getSearch(),
                    replace.getReplace()
            );

            builder.append(word)
                    .append(StringUtils.SPACE);
        }

        return builder.toString();
    }

    @SuppressWarnings("unused")
    public String generate(@NotNull Replace[] replaces) {
        final StringBuilder builder = new StringBuilder();

        for (RandomWord randomWord : randomWords) {
            String word = randomWord.generate();

            for (Replace replace : replaces) {
                word = StringUtils.replaceOnce(
                        word,
                        replace.getSearch(),
                        replace.getReplace()
                );
            }

            builder.append(word)
                    .append(StringUtils.SPACE);
        }

        return builder.toString();
    }

    public static Builder builder() {
        return new MultiAnswer.Builder();
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Builder {
        private final List<RandomWord> randomWords = new LinkedList<>();

        public Builder part(@NotNull String[] words, @NotNull WordEnd wordEnd) {
            randomWords.add(new RandomWord(words, wordEnd));

            return this;
        }

        public Builder part(@NotNull String word, @NotNull WordEnd wordEnd) {
            randomWords.add(new RandomWord(new String[]{ word }, wordEnd));

            return this;
        }

        public MultiAnswer build() {
            return new MultiAnswer(
                    randomWords.toArray(new RandomWord[]{})
            );
        }
    }
}
