package me.re4erka.botyara.bot.listeners;

import me.re4erka.botyara.api.bot.listener.common.IListener;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.api.util.similarity.SimilarityUtil;
import me.re4erka.botyara.file.type.Properties;
import org.jetbrains.annotations.NotNull;

public class FilterListener implements IListener {
    private final String[] filterWords;
    private final double similarity;

    public FilterListener() {
        this.filterWords = Properties.FILTER_WORDS.asStringList().toArray(new String[0]);
        this.similarity = Properties.FILTER_SIMILARITY.asDouble();
    }

    @Override
    public boolean onListen(final @NotNull Receiver receiver, final @NotNull Words words) {
        for (int i = 0; i < words.sizeRaw(); ++i) {
            final String word = words.getRaw(i);

            for (final String filterWord : filterWords) {
                if (SimilarityUtil.check(word, filterWord, similarity)) {
                    receiver.reputation(-50);
                    return true;
                }
            }
        }

        return false;
    }
}



















