package me.re4erka.botyara.api.util.similarity;

import lombok.experimental.UtilityClass;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;

@UtilityClass
public class SimilarityUtil {
    private static final JaroWinklerSimilarity JARO_WINKLER_SIMILARITY = new JaroWinklerSimilarity();

    public boolean check(String word, String anotherWord) {
        return JARO_WINKLER_SIMILARITY.apply(word, anotherWord) >= 0.8;
    }

    public boolean check(String word, String anotherWord, double distance) {
        return JARO_WINKLER_SIMILARITY.apply(word, anotherWord) >= distance;
    }
}
