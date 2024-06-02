package me.re4erka.botyara.api.bot.word.random.answer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.re4erka.botyara.api.bot.word.random.RandomWord;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE) @ToString
public class MultiAnswer {
    private final LinkedList<RandomWord> randomWords = new LinkedList<>();

    public String generate() {
        final StringBuilder builder = new StringBuilder();

        randomWords.forEach(randomWord ->
                builder.append(randomWord.generate())
                        .append(StringUtils.SPACE)
        );

        return builder.toString();
    }

    @SafeVarargs
    public final String generate(Pair<String, String>... replaces) {
        final StringBuilder builder = new StringBuilder();

        randomWords.forEach(randomWord -> {
            String word = randomWord.generate();

            for (Pair<String, String> replace : replaces) {
                word = StringUtils.replaceOnce(
                        word,
                        replace.getKey(),
                        replace.getValue()
                );
            }

            builder.append(word).append(StringUtils.SPACE);
        });

        return builder.toString();
    }

    public static Builder newBuilder() {
        return new MultiAnswer().new Builder();
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public class Builder {
        public Builder part(String[] words, WordEnd wordEnd) {
            randomWords.add(
                    new RandomWord(List.of(words), wordEnd)
            );

            return this;
        }

        public Builder part(String word, WordEnd wordEnd) {
            randomWords.add(
                    new RandomWord(List.of(word), wordEnd)
            );

            return this;
        }

        public MultiAnswer build() {
            return MultiAnswer.this;
        }
    }
}
