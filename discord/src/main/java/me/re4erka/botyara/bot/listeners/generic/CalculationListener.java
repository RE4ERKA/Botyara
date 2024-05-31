package me.re4erka.botyara.bot.listeners.generic;

import me.re4erka.botyara.api.bot.listener.ListeningBot;
import me.re4erka.botyara.api.bot.listener.ask.AskListener;
import me.re4erka.botyara.api.bot.listener.ask.AskType;
import me.re4erka.botyara.api.bot.listener.common.PostOrder;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.api.util.key.Key;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.OptionalInt;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class CalculationListener extends AskListener {
    private final String[] searchWordArray = new String[] {
            "подсчитай", "посчитай", "считай", "посчитаешь", "подсчитаешь", "сколько", "сложи", "вычитай", "реши"
    };

    private final static Pattern VALID_REGEX = Pattern.compile("[^А-Яа-яA-Za-z]");

    public CalculationListener(ListeningBot bot) {
        super(Key.of("CALCULATION"), PostOrder.LAST, bot);
    }

    @Override
    public boolean onListen(Receiver receiver, Words words) {
        final OptionalInt index = words.find(searchWordArray);

        if (index.isPresent()) {
            final StringBuilder builder = new StringBuilder();

            for (int i = index.getAsInt() + 1; i < words.sizeRaw(); i++) {
                final String word = words.getRaw(i);

                if (VALID_REGEX.matcher(word).find()) {
                    builder.append(word);
                }
            }

            Expression expression;

            try {
                expression = new ExpressionBuilder(builder.toString()).build();
            } catch (IllegalArgumentException e) {
                receiver.reply("Ваш пример неверно написан!");

                return true;
            }

            double result;

            try {
                result = expression.evaluate();
            } catch (IllegalArgumentException e) {
                receiver.reply("Я не могу решить этот пример!");

                return true;
            } catch (ArithmeticException exception) {
                if (exception.getMessage().equals("Division by zero!")) {
                    receiver.reply("Деление на ноль - невозможно!");
                } else {
                    receiver.reply("Этот пример невозможно решить!");
                }

                return true;
            }

            if (result == 1488) {
                return true;
            }

            this.addAskListener(receiver.getId());

            if (result % 1 != 0) {
                receiver.reply("По моим математическим подсчетам, итоговый ответ: " + result);
            } else {
                receiver.reply("По моим математическим подсчетам, итоговый ответ: " + Math.round(result));
            }

            receiver.reputation(1);

            return true;
        }

        return false;
    }

    @Override
    public boolean onAsked(Receiver receiver, Words words) {
        if (words.containsAny(AskType.WHY.getSearchWords())) {
            receiver.reply("Ты сомневаешься в моим подсчетах? Вот сам посчитай и сверь с моим ответом!");

            return true;
        }

        if (words.containsAny(AskType.SURE.getSearchWords())) {
            receiver.reply("Мой ответ точно верный потому что я лучший математик всего интернета!");

            return true;
        }

        return false;
    }
}
