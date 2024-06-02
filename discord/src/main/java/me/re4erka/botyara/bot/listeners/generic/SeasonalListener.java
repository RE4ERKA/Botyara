package me.re4erka.botyara.bot.listeners.generic;

import me.re4erka.botyara.api.bot.listener.common.Listener;
import me.re4erka.botyara.api.bot.listener.common.PostOrder;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.api.bot.word.random.answer.MultiAnswer;
import me.re4erka.botyara.api.bot.word.random.answer.WordEnd;
import me.re4erka.botyara.api.bot.word.search.SearchWords;
import me.re4erka.botyara.api.util.key.Key;
import me.re4erka.botyara.api.util.time.Pluralizer;
import me.re4erka.botyara.bot.ActiveBot;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@SuppressWarnings("unused")
public class SeasonalListener extends Listener {
    private final SearchWords summerWords = SearchWords.builder()
            .words("когда лето")
            .words("когда настанет лето")
            .words("сколько до лета")
            .words("сколько ждать до лета")
            .words("сколько дней до лета")
            .words("сколько минут до лета")
            .words("сколько часов до лета")
            .words("сколько месяцев до лета")
            .words("сколько осталось дней до лета")
            .words("сколько осталось минут до лета")
            .words("сколько осталось часов до лета")
            .words("сколько осталось месяцев до лета")
            .words("сколько дней осталось до лета")
            .words("сколько минут осталось до лета")
            .words("сколько часов осталось до лета")
            .words("сколько месяцев осталось до лета")
            .build();

    private final SearchWords winterWords = SearchWords.builder()
            .words("когда зима")
            .words("когда настанет зима")
            .words("сколько до зимы")
            .words("сколько ждать до зимы")
            .words("сколько дней до зимы")
            .words("сколько минут до зимы")
            .words("сколько часов до зимы")
            .words("сколько месяцев до зимы")
            .words("сколько осталось дней до зимы")
            .words("сколько осталось минут до зимы")
            .words("сколько осталось часов до зимы")
            .words("сколько осталось месяцев до зимы")
            .words("сколько дней осталось до зимы")
            .words("сколько минут осталось до зимы")
            .words("сколько часов осталось до зимы")
            .words("сколько месяцев осталось до зимы")
            .build();

    private final MultiAnswer summerAnswer = MultiAnswer.newBuilder()
            .part("Осталось %days_formatted% до лета", WordEnd.DOT)
            .part(new String[] {
                    "Вот бы поскорее это лето! :sweat_smile:",
                    "Я бы хотел погреть на солнце свои железяки! :sunglasses:",
                    "Золотые лучи, на землю смотрят. :white_sun_cloud: Летний день приходит, тепло приносит. :sun_with_face:",
                    "Ждем-с! :sweat_smile: :sunny:"
            }, WordEnd.EMPTY)
            .build();

    private final MultiAnswer winterAnswer = MultiAnswer.newBuilder()
            .part("Осталось %days_formatted% до зимы", WordEnd.DOT)
            .part(new String[] {
                    "Зима не хуже лета, не так ли? :relaxed:",
                    "Я бы хотел поиграть в снежки! Жаль рук нет.. :confounded:",
                    "Зимний лес, как в сказке чудной. :christmas_tree: Белый снег покрыл земную. :snowflake:",
                    "Ждем-с! :sweat_smile: :snowflake:"
            }, WordEnd.EMPTY)
            .build();

    public SeasonalListener() {
        super(Key.of("USER_ASKS_ABOUT_THE_SEASON"), PostOrder.NORMAL);
    }

    @Override
    public boolean onListen(Receiver receiver, Words words) {
        if (words.containsAny(summerWords)) {
            final ZonedDateTime today = ZonedDateTime.now(ActiveBot.ZONE_ID);
            ZonedDateTime summer = ZonedDateTime.of(
                    LocalDate.of(today.getYear(), Month.JUNE, 1),
                    today.toLocalTime(),
                    ActiveBot.ZONE_ID
            );

            if (today.isAfter(summer)) {
                summer = summer.plusYears(1);
            }

            final long days = ChronoUnit.DAYS.between(
                    today.toLocalDate(),
                    summer.toLocalDate()
            );

            receiver.reply(
                    summerAnswer.generate(
                            Pair.of("%days_formatted%", days + StringUtils.SPACE + Pluralizer.formatDays(days))
                    )
            ).reputation(1);

            return true;
        }

        if (words.containsAny(winterWords)) {
            final ZonedDateTime today = ZonedDateTime.now(ActiveBot.ZONE_ID);
            ZonedDateTime winter = ZonedDateTime.of(
                    LocalDate.of(today.getYear(), Month.DECEMBER, 1),
                    today.toLocalTime(),
                    ActiveBot.ZONE_ID
            );

            if (today.isAfter(winter)) {
                winter = winter.plusYears(1);
            }

            final long days = ChronoUnit.DAYS.between(today.toLocalDate(), winter.toLocalDate());

            receiver.reply(
                    winterAnswer.generate(
                            Pair.of("%days_formatted%", days + StringUtils.SPACE + Pluralizer.formatDays(days))
                    )
            ).reputation(1);

            return true;
        }

        return false;
    }
}
