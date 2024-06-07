package me.re4erka.botyara.bot.receiver.message;

import lombok.extern.log4j.Log4j2;
import me.re4erka.botyara.api.bot.user.UserData;
import me.re4erka.botyara.api.util.random.Random;
import me.re4erka.botyara.bot.receiver.type.DataReceiver;
import me.re4erka.botyara.executor.ScheduledExecutor;
import org.javacord.api.entity.message.Message;

import java.util.function.Consumer;
@Log4j2
public class MessageReceiver extends DataReceiver {
    public MessageReceiver(Message message, UserData data) {
        super(message, data, true);
    }

    @Override
    protected void onSend(String respondMessage, Consumer<Message> thenAction) {
        this.message.edit(respondMessage).thenAccept(botMessage -> {
            if (thenAction != null) {
                thenAction.accept(botMessage);
            }
        }).exceptionally(throwable -> {
            log.error("Failed to edit the message!", throwable);
            return null;
        });
    }

    @Override
    public void botTyping(Runnable runnable, long length) {
        message.getChannel().type().thenRun(() -> ScheduledExecutor.executeLater(
                runnable,
                calculateDelay(length) * Random.range(3, 5) // Умножаю на рандомное число, дабы изменение сообщения происходило не моментально.
        ));
    }
}
