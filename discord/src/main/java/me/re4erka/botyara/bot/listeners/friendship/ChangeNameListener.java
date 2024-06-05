package me.re4erka.botyara.bot.listeners.friendship;

import me.re4erka.botyara.api.bot.listener.ListeningBot;
import me.re4erka.botyara.api.bot.listener.await.AwaitingListener;
import me.re4erka.botyara.api.bot.listener.common.PostOrder;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.search.SearchWords;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.api.util.key.Key;
import me.re4erka.botyara.api.util.user.UserNameUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

@SuppressWarnings("unused")
public class ChangeNameListener extends AwaitingListener {
    private final SearchWords searchWords = SearchWords.builder()
            .words("измени имя").words("измени мое имя")
            .words("изменить имя").words("изменить мое имя")
            .words("измените имя").words("измените мое имя")
            .words("измените мне имя").words("измени мне имя")
            .words("изменика мне имя").words("изменика имя")
            .words("имя измените").words("имя измени")
            .words("поменяй имя").words("поменяй мое имя")
            .words("поменять имя").words("поменять мое имя")
            .words("поменяйте имя").words("поменяйте мне имя")
            .words("поменяйте мне имя").words("поменяй мне имя")
            .words("поменяйка имя").words("поменяйка мне имя")
            .words("имя поменяй").words("имя поменяйте")
            .words("смени имя").words("смени мое имя")
            .words("сменить имя").words("сменить мое имя")
            .words("смените имя").words("смените мое имя")
            .words("смените мне имя").words("смени мне имя")
            .words("сменика мне имя").words("сменика имя")
            .words("имя смени").words("имя смените")
            .words("меняй имя").words("имя меняй")
            .words("переименуй меня").words("переименуй мое имя").words("переименуй мне имя")
            .words("имя переименуй").words("переименуйте мое имя").words("переименуйте мне имя")
            .words("поменяешь мне имя").words("сменишь мне имя").words("изменишь мне имя")
            .words("редактируй имя").words("редактируй мое имя").words("редактируй мне имя")
            .words("редактировать имя").words("редактировать мое имя").words("редактировать мне имя")
            .build();

    public ChangeNameListener(ListeningBot bot) {
        super(Key.of("CHANGE_USER_NAME"), PostOrder.LAST, bot);
    }

    @Override
    public boolean onListen(Receiver receiver, Words words) {
        if (words.containsAny(searchWords)) {
            if (receiver.getName() == null) {
                receiver.reply("Я не знаю тебя... Напиши мне привет - познакомимся.")
                        .reputation(1);
            } else {
                askChangeName(receiver);
            }

            return true;
        }

        return false;
    }

    private void askChangeName(Receiver receiver) {
        this.addAwaitingListener(receiver.getId());

        receiver.reply("Какое новое имя хочешь?");
    }

    @Override
    protected boolean onAwaitingListen(Receiver receiver, Words words, int attempts) {
        if (words.size() == 1) {
            final String name = words.get(0);
            final UserNameUtil.InvalidType invalidType = UserNameUtil.valid(name);

            switch (invalidType) {
                case TOO_LONG -> receiver.reply("Я не верю, что это твое имя! Оно слишком длинное.").reputation(1);
                case TOO_SMALL -> receiver.reply("Я не верю, что это твое имя! Оно слишком короткое.").reputation(1);
                case CONTAINS_SPECIFIC_SYMBOLS -> receiver.reply(
                        "Пожалуйста, используй для своего имени только русские буквы!"
                ).reputation(1);
                default -> {
                    final String capitalizeName = StringUtils.capitalize(name);

                    removeAwaitingListener(receiver.getId());
                    receiver.setName(capitalizeName);

                    receiver.reply(
                            "Запомнил, твое новое имя - %user_name%",
                            Pair.of("%user_name%", capitalizeName)
                    ).reputation(3);
                }
            }

            return true;
        }

        receiver.reply("Я не понимаю, пожалуйста, напиши свое имя одним словом в сообщении!")
                .reputation(1);

        return true;
    }
}
