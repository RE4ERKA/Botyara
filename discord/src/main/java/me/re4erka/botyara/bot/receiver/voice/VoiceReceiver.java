package me.re4erka.botyara.bot.receiver.voice;

import lombok.extern.log4j.Log4j2;
import me.re4erka.botyara.api.bot.user.UserData;
import me.re4erka.botyara.bot.receiver.type.DataReceiver;
import me.re4erka.botyara.voice.VoiceManager;
import org.javacord.api.entity.message.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@Log4j2
public class VoiceReceiver extends DataReceiver {
    private final VoiceManager voiceManager;

    public VoiceReceiver(@NotNull Message message, @NotNull UserData data, @NotNull VoiceManager voiceManager) {
        super(message, data, false);
        this.voiceManager = voiceManager;
    }

    @Override
    protected void onSend(@NotNull String respondMessage, @Nullable Consumer<Message> thenAction) {
        // Пока что игнорируем thenAction так как он будет работать некорректно.
//        if (thenAction != null) {
//        }
    }

    @Override
    public void botTyping(@NotNull Runnable runnable, long length) {
        runnable.run();
    }
}
