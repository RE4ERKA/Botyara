package me.re4erka.discord.bot.activities;

import me.re4erka.api.bot.Bot;
import me.re4erka.api.bot.activity.Activity;
import me.re4erka.api.util.youtube.YoutubeVideos;
import me.re4erka.discord.file.type.Properties;

public class WatchActivity implements Activity {
    private final Bot bot;
    private final YoutubeVideos videos;

    public WatchActivity(Bot bot) {
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

        videos.random().thenAccept(optionalTitle ->
                optionalTitle.ifPresent(bot::watch)
        );

        return true;
    }
}
