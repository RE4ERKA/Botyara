package me.re4erka.botyara.bot.activities;

import me.re4erka.botyara.api.bot.Bot;
import me.re4erka.botyara.api.bot.activity.Activity;
import me.re4erka.botyara.api.util.youtube.YoutubeVideos;
import me.re4erka.botyara.file.type.Properties;
import org.jetbrains.annotations.NotNull;

public class WatchActivity implements Activity {
    private final Bot bot;
    private final YoutubeVideos videos;

    public WatchActivity(@NotNull Bot bot) {
        this.bot = bot;

        this.videos = new YoutubeVideos(
                Properties.ACTIVITIES_WATCHING_API_KEY.asString(),
                Properties.ACTIVITIES_WATCHING_CHANNELS.asStringList()
        );
    }

    @Override
    public boolean update() {
        if (bot.isSleep()) {
            return true;
        }

        videos.random().thenAccept(
                optionalVideo -> optionalVideo.ifPresent(video -> bot.watch(video.getLeft()))
        );

        return true;
    }
}
