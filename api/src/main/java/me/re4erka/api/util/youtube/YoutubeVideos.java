package me.re4erka.api.util.youtube;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.common.collect.ImmutableList;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

@Log4j2
public final class YoutubeVideos {
    private final YouTube youtube;
    private final ImmutableList<String> channels;

    public YoutubeVideos(String key, List<String> channels) {
        final NetHttpTransport httpTransport;

        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException | IOException e) {
            log.error("Failed to connect to the server!");
            throw new RuntimeException(e);
        }

        final JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        final YouTubeRequestInitializer requestInitializer = new YouTubeRequestInitializer(key);

        this.youtube = new YouTube.Builder(httpTransport, jsonFactory, null)
                .setYouTubeRequestInitializer(requestInitializer)
                .setApplicationName("Botyara")
                .build();

        this.channels = ImmutableList.copyOf(channels);
    }

    public CompletableFuture<Optional<String>> random() {
        return CompletableFuture.supplyAsync(() -> {
            YouTube.Search.List request = null;
            try {
                request = youtube.search()
                        .list("snippet");
            } catch (IOException e) {
                log.error("Failed to search through the YouTube api!", e);
            }

            if (request == null) {
                return Optional.empty();
            }

            final int channelIndex = ThreadLocalRandom.current().nextInt(0, channels.size());

            SearchListResponse response = null;
            try {
                response = request.setChannelId(channels.get(channelIndex))
                        .setOrder("date")
                        .setMaxResults(3L)
                        .setVideoDuration("medium")
                        .setType("video")
                        .execute();
            } catch (IOException e) {
                log.error("Failed to query a specific channel via YouTube api!", e);
            }

            if (response == null) {
                return Optional.empty();
            }

            if (response.getItems().isEmpty()) {
                return Optional.empty();
            }

            final int videoIndex = ThreadLocalRandom.current().nextInt(0, response.getItems().size());

            return Optional.of(
                    response.getItems()
                            .get(videoIndex)
                            .getSnippet()
                            .getTitle()
            );
        });
    }
}
