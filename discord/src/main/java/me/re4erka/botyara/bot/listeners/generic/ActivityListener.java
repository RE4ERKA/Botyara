package me.re4erka.botyara.bot.listeners.generic;

import me.re4erka.botyara.api.bot.listener.common.Listener;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.search.SearchWords;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.Botyara;
import me.re4erka.botyara.api.util.key.Key;
import me.re4erka.botyara.bot.ActiveBot;
import org.javacord.api.entity.activity.Activity;
import org.javacord.api.entity.activity.ActivityType;

@SuppressWarnings("unused")
public class ActivityListener extends Listener {
    private final ActiveBot bot;

    private final SearchWords whatYouDoWords = SearchWords.builder()
            .words("что делаешь")
            .words("что ты делаешь")
            .words("чем занят")
            .words("чем ты занят")
            .words("чем занимаешься")
            .words("чем ты занимаешься")
            .words("что прямо сейчас делаешь")
            .words("чем прямо сейчас занят")
            .words("чем прямо сейчас занимаешься")
            .words("что щас делаешь")
            .words("что ты щас делаешь")
            .words("чем щас занимаешься")
            .words("чем ты щас занимаешься")
            .build();

    private final SearchWords whatYouListening = SearchWords.builder()
            .words("что слушаешь")
            .words("что ты слушаешь")
            .words("что за трек слушаешь")
            .words("что за музыку слушаешь")
            .words("что за песню слушаешь")
            .words("что ты за трек слушаешь")
            .words("что ты за музыку слушаешь")
            .words("что ты за песню слушаешь")
            .words("что за трек ты слушаешь")
            .words("что за музыку ты слушаешь")
            .words("что за песню ты слушаешь")
            .words("что прямо сейчас слушаешь")
            .words("что ты прямо сейчас слушаешь")
            .words("что прямо сейчас ты слушаешь")
            .build();

    private final SearchWords whatYouPlaying = SearchWords.builder()
            .words("во что играешь")
            .words("что играешь")
            .words("в какую игру играешь")
            .words("в какую мини-игру играешь")
            .words("что за игру играешь")
            .words("в какую играешь игру")
            .words("в какую играешь мини-игру")
            .words("во что это играешь")
            .words("во что это ты играешь")
            .words("во что ты играешь")
            .build();

    private final SearchWords whatYouWatching = SearchWords.builder()
            .words("что смотришь")
            .words("что ты смотришь")
            .words("что за видео смотришь")
            .words("какое видео смотришь")
            .words("какое видео смотришь")
            .words("что прямо сейчас смотришь")
            .words("что ты прямо сейчас смотришь")
            .build();

    public ActivityListener() {
        super(Key.of("USER_ASKS_FOR_ACTIVITY"));

        this.bot = Botyara.INSTANCE.getDiscordManager().getBot();
    }

    @Override
    public boolean onListen(Receiver receiver, Words words) {
        /* Скорее всего никогда не будет пустой. */
        if (bot.getDiscordActivity().isEmpty()) {
            return false;
        }

        final Activity activity = bot.getDiscordActivity().get();

        if (words.containsAny(whatYouDoWords)) {
            switch (activity.getType()) {
                case LISTENING -> replyWhatListening(receiver, activity.getName());
                case PLAYING -> replyWhatPlaying(receiver, activity.getName());
                case WATCHING -> replyWhatWatching(receiver, activity.getName());
                default -> receiver.reply(
                        "Я прямо сейчас ничем интересным не занят!"
                ).reputation(1);
            }

            return true;
        }

        if (words.containsAny(whatYouListening)) {
            if (activity.getType() == ActivityType.LISTENING) {
                replyWhatListening(receiver, activity.getName());
            } else {
                receiver.reply("Я сейчас ничего не слушаю!").reputation(1);
            }

            return true;
        }

        if (words.containsAny(whatYouPlaying)) {
            if (activity.getType() == ActivityType.PLAYING) {
                replyWhatPlaying(receiver, activity.getName());
            } else {
                receiver.reply("Я сейчас не во что не играю!").reputation(1);
            }

            return true;
        }

        if (words.containsAny(whatYouWatching)) {
            if (activity.getType() == ActivityType.WATCHING) {
                replyWhatWatching(receiver, activity.getName());
            } else {
                receiver.reply("Я сейчас ничего не смотрю!").reputation(1);
            }

            return true;
        }

        return false;
    }

    private void replyWhatListening(Receiver receiver, String content) {
        final String[] track = content.split(" - ");

        receiver.reply(
                String.format(
                        "Я в данный момент слушаю %s и его песню %s",
                        track[0],
                        track[1]
                )
        ).reputation(1);
    }

    private void replyWhatPlaying(Receiver receiver, String content) {
        receiver.reply("Я, прямо сейчас, играю в мини-игру " + content).reputation(1);
    }

    private void replyWhatWatching(Receiver receiver, String content) {
        receiver.reply(String.format("Я сейчас смотрю видео \"%s\"", content)).reputation(1);
    }
}
