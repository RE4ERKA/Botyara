package me.re4erka.botyara.bot.receiver;

import lombok.extern.log4j.Log4j2;
import me.re4erka.botyara.api.bot.friendship.FriendshipType;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.api.history.logging.HistoryMessage;
import me.re4erka.botyara.Botyara;
import me.re4erka.botyara.api.bot.user.UserData;
import me.re4erka.botyara.api.util.random.Random;
import me.re4erka.botyara.bot.ActiveBot;
import me.re4erka.botyara.executor.ScheduledExecutor;
import org.javacord.api.entity.message.Message;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Log4j2
public class DiscordReceiver extends Receiver {
    protected final Message message;
    private final UserData data;

    public DiscordReceiver(Message message, UserData data) {
        this.message = message;
        this.data = data;
    }

    @Override
    public long getId() {
        return data.getId();
    }

    @Override
    public FriendshipType getFriendshipType() {
        return data.getFriendshipType();
    }

    @Override
    public void intoFamiliar(String name) {
        data.intoFamiliar(name);
        ActiveBot.USER_HISTORY.log(
                "Имя пользователя добавлено. Имя: '%user_name%'. Пользователь: %user_id%",
                this
        );
    }

    @Override
    public Receiver reply(String respondMessage) {
        botTyping(() -> onSend(respondMessage, null), respondMessage.length());

        return this;
    }

    public Receiver replyThenRun(String respondMessage, Consumer<Message> thenAction) {
        botTyping(() -> onSend(respondMessage, thenAction), respondMessage.length());

        return this;
    }

    public void replyWithoutDelay(String respondMessage) {
        message.reply(respondMessage).exceptionally(throwable -> {
            log.error("Failed to reply to user message!", throwable);
            return null;
        });
    }

    public Receiver emojiThenRun(String emoji, Consumer<Message> thenAction) {
        message.addReactions(emoji).exceptionally(throwable -> {
            log.error("Failed to post emoji to user message!", throwable);
            return null;
        }).thenRun(() -> thenAction.accept(message));

        return this;
    }

    @Override
    public void setName(String name) {
        data.setName(name);
        ActiveBot.USER_HISTORY.log(
                "Имя пользователя изменено. Имя: '%user_name%'. Пользователь: %user_id%",
                this
        );
    }

    @Override
    public String getName() {
        return data.getName();
    }

    @Override
    public void reputation(int delta) {
        if (isStranger()) {
            return;
        }

        /* Проверяем произошли ли изменения при установлении репутации */
        if (data.setReputation(data.getReputation() + delta)) {
            /* Проверяем изменился ли статус дружбы. */
            if (data.checkFriendshipStatus()) {
                ActiveBot.USER_HISTORY.log(
                        "Статус дружбы обновлен. Статус: %friendship_type%. Репутация: %user_reputation%. Пользователь: %user_name%(%user_id%)",
                        this
                );
            }
        }
    }

    @Override
    public int getReputation() {
        return data.getReputation();
    }

    @Override
    public boolean isStranger() {
        return data.isStranger();
    }

    public CompletableFuture<Void> botTyping() {
        return message.getChannel().type();
    }

    protected void onSend(String respondMessage, Consumer<Message> thenAction) {
        message.reply(respondMessage).thenAccept(botMessage -> {
            message.addMessageEditListener(listener -> {
                final Message message = listener.getMessage();

                Botyara.INSTANCE.getDiscordManager().getBot().onListen(
                        new MessageReceiver(botMessage, data),
                        Words.create(
                                message.getReadableContent(),
                                message.isPrivateMessage(),
                                true
                        )
                );

                ActiveBot.USER_HISTORY.log(
                        HistoryMessage.create("Сообщение изменено: '%bot_message%' на '%bot_changed_message%' из-за пользователя: %user_name%(%user_id%)")
                                .replace("bot_message", respondMessage)
                                .replace("bot_changed_message", botMessage.getReadableContent())
                                .get(),
                        this,
                        message.isPrivateMessage()
                );
            });

            message.addMessageDeleteListener(listener -> botMessage.deleteAfter(
                    Duration.ofSeconds(Random.range(3, 10))
            ).thenRun(() -> ActiveBot.USER_HISTORY.log(
                    HistoryMessage.create("Сообщение удалено: '%bot_message%' из-за пользователя: %user_name%(%user_id%)")
                            .replace("bot_message", respondMessage)
                            .get(),
                    this,
                    message.isPrivateMessage()
            )));

            ActiveBot.USER_HISTORY.log(
                    HistoryMessage.create("%user_name%(%user_id%): '%user_message%'  <-  '%bot_message%'")
                            .replace("user_message", message.getReadableContent())
                            .replace("bot_message", respondMessage)
                            .get(),
                    this,
                    message.isPrivateMessage()
            );

            if (thenAction != null) {
                thenAction.accept(botMessage);
            }
        }).exceptionally(throwable -> {
            log.error("Failed to reply the message!", throwable);
            return null;
        });
    }

    protected void botTyping(Runnable runnable, long length) {
        message.getChannel().type().thenRun(() -> ScheduledExecutor.executeLater(
                runnable,
                calculateDelay(length)
        ));
    }

    public long calculateDelay(long length) {
        return length > 50 ? length * 30L : length * 15;
    }
}
