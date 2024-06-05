package me.re4erka.botyara.bot.listeners;

import me.re4erka.botyara.api.bot.listener.common.IListener;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.api.util.similarity.SimilarityUtil;

public class FilterListener implements IListener {
    private final String[] filterWords = new String[] {
            "nigger", "nigga", "naga", "нигер", "нига", "нага",
            "niggers", "нигеры", "ниги", "наги",
            "faggot", "пидор", "пидорас", "педик", "гомик",
            "пидоры", "пидорасы", "педики", "гомики",
            "хохол", "хач", "жид", "аллах",
            "хохлы", "хачы", "жиды", "аллахи",
            "даун", "аутист", "дебил", "retard",
            "дауны", "аутисты", "дебилы", "retards",
            "virgin", "simp", "incel", "девственник", "симп", "инцел",
            "virgins", "simps", "incels", "девственники", "симпы", "инцелы",
            "cunt", "пизда", "хиджаб", "куколд",
            "suicide", "суицид", "самоубийство",
            "куколды", "суицидники", "1488",
            "tranny", "транc",
    };

    @Override
    public boolean onListen(Receiver receiver, Words words) {
        for (int i = 0; i < words.sizeRaw(); i++) {
            final String word = words.getRaw(i);

            for (String filterWord : filterWords) {
                if (SimilarityUtil.check(word, filterWord, 0.9)) {
                    receiver.reputation(-50);
                    return true;
                }
            }
        }

        return false;
    }
}



















