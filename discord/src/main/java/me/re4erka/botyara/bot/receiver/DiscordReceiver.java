package me.re4erka.botyara.bot.receiver;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.api.history.logging.HistoryMessage;
import me.re4erka.botyara.Botyara;
import me.re4erka.botyara.api.bot.user.UserData;
import me.re4erka.botyara.api.util.random.Random;
import me.re4erka.botyara.bot.ActiveBot;
import me.re4erka.botyara.bot.receiver.message.MessageReceiver;
import me.re4erka.botyara.executor.ScheduledExecutor;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.message.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Log4j2
public abstract class DiscordReceiver implements Receiver {
    protected final Message message;
    protected final UserData data;

    @Accessors(fluent = true)
    @Getter
    private final boolean hasMessageBeenChanged;

    public DiscordReceiver(@NotNull Message message, @NotNull UserData data, boolean hasMessageBeenChanged) {
        this.message = message;
        this.data = data;

        this.hasMessageBeenChanged = hasMessageBeenChanged;
    }

    @Override
    public Receiver reply(@NotNull String respondMessage) {
        botTyping(() -> onSend(respondMessage, null), respondMessage.length());

        return this;
    }

    public Receiver replyThenRun(@NotNull String respondMessage, @Nullable Consumer<Message> thenAction) {
        botTyping(() -> onSend(respondMessage, thenAction), respondMessage.length());

        return this;
    }

    public void replyWithoutDelay(@NotNull String respondMessage) {
        onSend(respondMessage, null);
    }

    public Receiver emojiThenRun(@NotNull String emoji, @NotNull Consumer<Message> thenAction) {
        message.addReactions(emoji).exceptionally(throwable -> {
            log.error("Failed to post emoji to user message!", throwable);
            return null;
        }).thenRun(() -> thenAction.accept(message));

        return this;
    }

    public Optional<ServerVoiceChannel> getConnectedVoiceChannel() {
        return message.getAuthor().getConnectedVoiceChannel();
    }

    public long calculateDelay(long length) {
        return length > 50 ? length * 30L : length * 15;
    }

    public CompletableFuture<Void> botTyping() {
        return this.message.getChannel().type();
    }

    protected void onSend(@NotNull String respondMessage, @Nullable Consumer<Message> thenAction) {
        message.reply(respondMessage).thenAccept(botMessage -> {
            message.addMessageEditListener(listener -> {
                final Message message = listener.getMessage();

                Botyara.INSTANCE.getDiscordManager().getBot().onListen(
                        new MessageReceiver(botMessage, data),
                        Words.create(message.getReadableContent())
                );

                ActiveBot.USER_HISTORY.log(
                        HistoryMessage.create("Сообщение изменено: '%bot_message%' на '%bot_changed_message%' из-за пользователя: %user_name%(%user_id%)")
                                .replace("bot_message", respondMessage)
                                .replace("bot_changed_message", botMessage.getReadableContent())
                                .getMessage(),
                        this
                );
            });

            message.addMessageDeleteListener(listener -> botMessage.deleteAfter(
                    Duration.ofSeconds(Random.range(3, 10))
            ).thenRun(() -> ActiveBot.USER_HISTORY.log(
                    HistoryMessage.create("Сообщение удалено: '%bot_message%' из-за пользователя: %user_name%(%user_id%)")
                            .replace("bot_message", respondMessage)
                            .getMessage(),
                    this
            )));

            ActiveBot.USER_HISTORY.log(
                    HistoryMessage.create("%user_name%(%user_id%): '%user_message%'  <-  '%bot_message%'")
                            .replace("user_message", message.getReadableContent())
                            .replace("bot_message", respondMessage)
                            .getMessage(),
                    this
            );

            if (thenAction != null) {
                thenAction.accept(botMessage);
            }
        }).exceptionally(throwable -> {
            log.error("Failed to reply the message!", throwable);
            return null;
        });
    }

    protected void botTyping(@NotNull Runnable runnable, long length) {
        message.getChannel().type().thenRun(() -> ScheduledExecutor.executeLater(
                runnable,
                calculateDelay(length)
        ));
    }
}
