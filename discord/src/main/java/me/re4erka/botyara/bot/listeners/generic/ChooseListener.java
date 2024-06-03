package me.re4erka.botyara.bot.listeners.generic;

import com.google.common.collect.ImmutableList;
import me.re4erka.botyara.api.bot.listener.ListeningBot;
import me.re4erka.botyara.api.bot.listener.ask.AskListener;
import me.re4erka.botyara.api.bot.listener.common.PostOrder;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.api.util.key.Key;
import me.re4erka.botyara.api.util.random.Random;
import org.apache.commons.lang3.StringUtils;

import java.util.OptionalInt;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class ChooseListener extends AskListener {
    private final String[] searchWordArray = new String[] {
            "выбери", "выбирай", "выберите",
            "решай", "реши", "решайте",
            "лучше", "между"
    };

    private final String[] enumeration = new String[] {
            "или", "либо"
    };

    private static final Pattern VALID_REGEX = Pattern.compile("[^А-Яа-я^A-za-z0-9Ёё\\s]");

    public ChooseListener(ListeningBot bot) {
        super(Key.of("CHOOSE"), PostOrder.NORMAL, bot);
    }

    @Override
    public boolean onListen(Receiver receiver, Words words) {
        OptionalInt optionalIndex = words.find(searchWordArray);
        ImmutableList<String> chooseWords;

        if (optionalIndex.isEmpty()) {
            optionalIndex = words.find(enumeration);

            if (optionalIndex.isPresent()) {
                chooseWords = chooseWords(words, optionalIndex.getAsInt() - 2);
            } else {
                return false;
            }
        } else {
            chooseWords = chooseWords(words, optionalIndex.getAsInt());
        }

        if (chooseWords.size() < 2) {
            return false;
        }

        final String answer = chooseWords.get(
                Random.next(chooseWords.size())
        );

        if (VALID_REGEX.matcher(answer).find()) {
            receiver.reply("Пожалуйста, не используй специфичные символы для выбора!");

            return true;
        }

        final String[] answerWords = StringUtils.split(answer, ' ');

        for (int i = 0; i < answerWords.length; i++) {
            final String word = answerWords[i];
            switch (word) {
                case "он" -> answerWords[i] = "его";
                case "она" -> answerWords[i] = "её";
                case "они" -> answerWords[i] = "их";
                case "мне" -> answerWords[1] = "тебе";
                case "ты", "тебя" -> answerWords[i] = "себя";
                case "я", "меня" -> answerWords[i] = "тебя";
            }
        }

        this.addAskListener(receiver.getId());

        receiver.reply(
                "Из всех вариантов, я выбираю... "
                        + StringUtils.capitalize(
                                StringUtils.join(answerWords, ' ')
                )
        ).reputation(1);

        return true;
    }

    private ImmutableList<String> chooseWords(Words words, int firstIndex) {
        final ImmutableList.Builder<String> builder = new ImmutableList.Builder<>();

        if (firstIndex < 0 || firstIndex == words.size()) {
            return ImmutableList.of();
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = firstIndex; i < words.size(); i++) {
            final String word = words.get(i);

            if (word.equals(enumeration[0]) || word.equals(enumeration[1])) {
                builder.add(stringBuilder.toString());

                stringBuilder = new StringBuilder();

                continue;
            }

            if (!stringBuilder.isEmpty()) {
                stringBuilder.append(" ");
            }

            stringBuilder.append(word);
        }

        if (!stringBuilder.isEmpty()) {
            builder.add(stringBuilder.toString());
        }

        return builder.build();
    }

    @Override
    public boolean onAsked(Receiver receiver, Words words) {
        return false;
    }
}
