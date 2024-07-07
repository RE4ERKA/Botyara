package me.re4erka.botyara.api.bot.memory.word;

import me.re4erka.botyara.api.util.similarity.SimilarityUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryWords {
    private final Map<String, String> answers;

    public MemoryWords(int size) {
        this.answers = new LinkedHashMap<>(size);
    }

    public void add(@NotNull String words, @NotNull String answer) {
        answers.put(words, answer);
    }

    public Optional<String> getIfMatches(@NotNull String words) {
        return Optional.ofNullable(
                getIfMatchesOrElse(words, null)
        );
    }

    public String getIfMatchesOrElse(@NotNull String words, @Nullable String answer) {
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
