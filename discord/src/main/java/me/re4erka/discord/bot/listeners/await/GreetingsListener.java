package me.re4erka.discord.bot.listeners.await;

import me.re4erka.api.bot.listener.ListeningBot;
import me.re4erka.api.bot.listener.await.AwaitingListener;
import me.re4erka.api.bot.listener.common.PostOrder;
import me.re4erka.api.bot.receiver.Receiver;
import me.re4erka.api.bot.word.Words;
import me.re4erka.api.bot.word.random.answer.MultiAnswer;
import me.re4erka.api.bot.word.random.answer.WordEnd;
import me.re4erka.api.util.user.UserNameUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

@SuppressWarnings("unused")
public class GreetingsListener extends AwaitingListener {
    private final String[] greetingWords = {
            "здравствуй", "привет",
            "хай", "здарова",
            "прив", "ку"
    };

    private final MultiAnswer familiarityAnswer;
    private final MultiAnswer greetingAnswer;

    public GreetingsListener(ListeningBot bot) {
        super("USER_GREETING", PostOrder.LAST, bot);

        familiarityAnswer = MultiAnswer.newBuilder()
                .part(greetingWords, WordEnd.COMMA)
                .part(new String[] {
                        "меня зовут Ботяра", "зовут меня Ботяра",
                        "мое имя Ботяра", "меня обычно называют Ботяра",
                        "я, если что, Ботяра"
                }, WordEnd.COMMA)
                .part(new String[] {
                        "а как тебя зовут",
                        "а тебя как зовут",
                        "а какое твое имя",
                        "а как тебя прозвали",
                        "а у тебя какое имя"
                }, WordEnd.QUESTION)
                .build();

        greetingAnswer = MultiAnswer.newBuilder()
                .part(greetingWords, WordEnd.COMMA)
                .part("%user_name%", WordEnd.EXCLAMATION)
                .build();
    }

    @Override
    public boolean onListen(Receiver receiver, Words words) {
        if (words.contains(greetingWords)) {
            if (receiver.getName() == null) {
                addAwaitingListener(receiver.getId());

                receiver.reply(
                        StringUtils.capitalize(
                                familiarityAnswer.generate()
                        )
                );
            } else {
                receiver.reply(
                        StringUtils.capitalize(
                                greetingAnswer.generate(
                                        Pair.of("%user_name%", receiver.getName())
                                )
                        )
                );
            }

            return true;
        }

        return false;
    }

    @Override
    protected boolean onAwaitingListen(Receiver receiver, Words words, int attempts) {
        if (words.size() == 1) {
            final String name = words.get(0);
            final UserNameUtil.InvalidType invalidType = UserNameUtil.valid(name);

            switch (invalidType) {
                case TOO_LONG -> receiver.reply("Я не верю, что это твое имя! Оно слишком длинное.");
                case TOO_SMALL -> receiver.reply("Я не верю, что это твое имя! Оно слишком короткое.");
                case CONTAINS_SPECIFIC_SYMBOLS -> receiver.reply("Пожалуйста, используй для своего имени только русские буквы!");
                default -> {
                    removeAwaitingListener(receiver.getId());

                    final String capitalizeName = StringUtils.capitalize(name);

                    receiver.intoFamiliar(capitalizeName);
                    receiver.reply(
                            "Вот как значит тебя зовут.. Запомню, %user_name%",
                            Pair.of("%user_name%", capitalizeName)
                    );
                }
            }

            return true;
        }

        receiver.reply("Я не понимаю, пожалуйста, напиши свое имя одним словом в сообщении!");

        return true;
    }
}
