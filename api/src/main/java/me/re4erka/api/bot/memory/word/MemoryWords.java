package me.re4erka.api.bot.memory.word;

import me.re4erka.api.util.similarity.SimilarityUtil;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryWords {
    private final Map<String, String> answers;

    public MemoryWords(int size) {
        this.answers = new LinkedHashMap<>(size);
    }

    public void add(String words, String answer) {
        answers.put(words, answer);
    }

    public Optional<String> getIfMatches(String words) {
        return Optional.ofNullable(
                getIfMatchesOrElse(words, null)
        );
    }

    public String getIfMatchesOrElse(String words, String answer) {
        for (Map.Entry<String, String> memory : answers.entrySet()) {
            if (SimilarityUtil.check(words, memory.getKey())) {
                return memory.getValue();
            }
        }

        if (answer != null) {
            add(words, answer);
        }

        return answer;
    }
}
