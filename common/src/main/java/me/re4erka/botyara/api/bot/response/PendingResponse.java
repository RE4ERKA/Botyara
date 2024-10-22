package me.re4erka.botyara.api.bot.response;

import lombok.Getter;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.Words;
import org.jetbrains.annotations.NotNull;

@Getter
public final class PendingResponse {
    private final Receiver receiver;
    private final Words words;

    private PendingResponse(@NotNull Receiver receiver, @NotNull Words words) {
        this.receiver = receiver;
        this.words = words;
    }

    public static PendingResponse of(@NotNull Receiver receiver, @NotNull Words words) {
        return new PendingResponse(receiver, words);
    }
}
