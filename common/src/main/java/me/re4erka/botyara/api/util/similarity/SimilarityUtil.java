package me.re4erka.botyara.api.util.similarity;

import lombok.experimental.UtilityClass;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class SimilarityUtil {
    private static final JaroWinklerSimilarity JARO_WINKLER_SIMILARITY = new JaroWinklerSimilarity();

    public boolean check(@NotNull String word, @NotNull String anotherWord) {
        return JARO_WINKLER_SIMILARITY.apply(word, anotherWord) >= 0.8;
    }

    public boolean check(@NotNull String word, @NotNull String anotherWord, double distance) {
        return JARO_WINKLER_SIMILARITY.apply(word, anotherWord) >= distance;
    }
}
