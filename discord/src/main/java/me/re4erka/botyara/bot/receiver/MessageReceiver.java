package me.re4erka.botyara.bot.receiver;

import lombok.extern.log4j.Log4j2;
import me.re4erka.botyara.api.bot.user.UserData;
import org.javacord.api.entity.message.Message;

@Log4j2
public class MessageReceiver extends DiscordReceiver {
    public MessageReceiver(Message message, UserData data) {
        super(message, data);
    }

    @Override
    public void onReply(String message) {
        this.message.edit(message).exceptionally(throwable -> {
            log.error("Failed to change the message!", throwable);
            return null;
        });
    }

    @Override
    public void replyWithoutDelay(String respondMessage) {
        this.onReply(respondMessage);
    }
}
