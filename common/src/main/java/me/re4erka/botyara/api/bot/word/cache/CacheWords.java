package me.re4erka.botyara.api.bot.word.cache;

import lombok.Getter;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.api.util.similarity.SimilarityUtil;
import org.jetbrains.annotations.NotNull;

public class CacheWords {
    @Getter
    private final long id;
    private final String wordsToString;

    public CacheWords(long id, @NotNull String wordsToString) {
        this.id = id;
        this.wordsToString = wordsToString;
    }

    public boolean similarity(@NotNull Words words) {
        return SimilarityUtil.check(
                wordsToString,
                words.toString(),
                0.9
        );
    }
}
