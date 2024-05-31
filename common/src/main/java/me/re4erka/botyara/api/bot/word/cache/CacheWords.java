package me.re4erka.botyara.api.bot.word.cache;

import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.api.util.similarity.SimilarityUtil;

public class CacheWords {
    private final long id;
    private final String wordsToString;

    public CacheWords(long id, String wordsToString) {
        this.id = id;
        this.wordsToString = wordsToString;
    }

    public long getId() {
        return id;
    }

    public boolean similarity(Words words) {
        return SimilarityUtil.check(
                wordsToString,
                words.toString(),
                0.9
        );
    }
}
