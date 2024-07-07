package me.re4erka.botyara.api.bot.word.search;

import lombok.ToString;
import me.re4erka.botyara.api.bot.word.Words;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ToString
public class SearchWords {
    private final Words[] searchWords;

    private SearchWords(@NotNull Words[] searchWords) {
        this.searchWords = searchWords;
    }

    public Words get(int index) {
        return searchWords[index];
    }

    public int size() {
        return searchWords.length;
    }

    public static SearchWords of(@NotNull Collection<String> collection) {
        final Builder builder = SearchWords.builder();

        collection.forEach(builder::words);

        return builder.build();
    }

    public static SearchWords.Builder builder() {
        return new SearchWords.Builder();
    }

    public static final class Builder {
        private final List<Words> wordList = new ArrayList<>();

        public Builder words(@NotNull String words) {
            wordList.add(
                    Words.of(StringUtils.split(words, ' '))
            );

            return this;
        }

        public SearchWords build() {
            final Words[] words = new Words[wordList.size()];
            wordList.toArray(words);
            wordList.clear();

            return new SearchWords(
                    words
            );
        }
    }
}
