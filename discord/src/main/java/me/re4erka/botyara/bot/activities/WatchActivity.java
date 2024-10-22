package me.re4erka.botyara.bot.activities;

import me.re4erka.botyara.api.bot.Bot;
import me.re4erka.botyara.api.bot.activity.Activity;
import me.re4erka.botyara.api.util.random.Random;
import me.re4erka.botyara.api.util.youtube.YoutubeVideos;
import me.re4erka.botyara.file.type.Properties;
import org.jetbrains.annotations.NotNull;

public class WatchActivity implements Activity {
    private final YoutubeVideos videos;

    public WatchActivity() {
        this.videos = new YoutubeVideos(
                Properties.ACTIVITIES_WATCHING_API_KEY.asString(),
                Properties.ACTIVITIES_WATCHING_CHANNELS.asStringList()
        );
    }

    @Override
    public boolean update(@NotNull Bot bot) {
        if (Random.chance(20)) {
            videos.random().thenAccept(
                    optionalVideo -> optionalVideo.ifPresent(video -> bot.updateActivity(Type.WATCHING, video.getLeft()))
            );
            return true;
        }

        return false;
    }
}
