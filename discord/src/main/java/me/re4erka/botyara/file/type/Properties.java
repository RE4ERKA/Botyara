package me.re4erka.botyara.file.type;

import lombok.RequiredArgsConstructor;
import me.re4erka.botyara.Botyara;

import java.util.List;

@RequiredArgsConstructor
public enum Properties {
    BOT_TOKEN("Bot.token", "EMPTY"),
    BOT_DEBUG("Bot.debug", true),

//    DATABASE_TYPE("Database.type", "sqlite"),
    DATABASE_SQLITE_FILE_NAME("Database.sqlite.file_name", "users"),

    CACHE_USERS_MAXIMUM_SIZE("Cache.users.maximum_size", 64),
    CACHE_USERS_EXPIRATION("Cache.users.expiration", 480),

    CACHE_MESSAGE_MAXIMUM_SIZE("Cache.message.maximum_size", 16),
    CACHE_MESSAGE_EXPIRATION("Cache.message.expiration", 1800),

    LISTENER_AWAITING_MAXIMUM_SIZE("Listener.types.AWAITING.maximum_size", 64),
    LISTENER_ASK_MAXIMUM_SIZE("Listener.types.ASK.maximum_size", 64),

//    LISTENER_MAXIMUM_RESPONSE_MEMORY("Listener.maximum_response_memory", 128),
    LISTENER_DEFAULT_RESPONSE_MEMORY("Listener.default_response_memory", 256),

    LISTENER_REPETITION_WORDS_SIZE("Listener.repetition_words_size", 16),

    LISTENER_IGNORE_EMPTY_LISTENERS_FOLDER("Listener.ignore_empty_listeners_folder", false),

    LISTENER_BLACKLIST("Listener.blacklist", null),

    FILTER_SIMILARITY("Filter.similarity", 1.0),
    FILTER_WORDS("Filter.words", null),

//    TEXT_TO_SPEECH_TYPE("Text_to_speech.type", "VOICE_RSS"),
    TEXT_TO_SPEECH_API_KEY("Text_to_speech.api_key", "EMPTY"),
    TEXT_TO_SPEECH_VOICE_NAME("Text_to_speech.voice_name", ""),

    SCHEDULER_UPDATE_PERIOD_ORIGIN("Scheduler.update_period.origin", 30),
    SCHEDULER_UPDATE_PERIOD_BOUND("Scheduler.update_period.bound", 60),

    ACTIVITIES_SLEEPING_ENABLED("Activities.SLEEPING.enabled", false),
    ACTIVITIES_SLEEPING_ZONE_ID("Activities.SLEEPING.zone_id", "Europe/Moscow"),
    ACTIVITIES_SLEEPING_PATTERN_BETWEEN("Activities.SLEEPING.pattern.between", 0),
    ACTIVITIES_SLEEPING_PATTERN_TO("Activities.SLEEPING.pattern.to", 1),

    ACTIVITIES_WATCHING_ENABLED("Activities.WATCHING.enabled", false),
    ACTIVITIES_WATCHING_API_KEY("Activities.WATCHING.api_key", "EMPTY"),
    ACTIVITIES_WATCHING_CHANNELS("Activities.WATCHING.channels", null),

    ACTIVITIES_LISTENING_ENABLED("Activities.LISTENING.enabled", false),
    ACTIVITIES_LISTENING_SONGS("Activities.LISTENING.songs", null),

    ACTIVITIES_PLAYING_ENABLED("Activities.PLAYING.enabled", false),
    ACTIVITIES_PLAYING_GAMES("Activities.PLAYING.games", null);

    private final String path;
    private final Object def;

    public String asString() {
        return Botyara.INSTANCE.getFileManager()
                .getProperties().getString(path, (String) def);
    }

    public int asInt() {
        return Botyara.INSTANCE.getFileManager()
                .getProperties().getInt(path, (int) def);
    }

    public boolean asBoolean() {
        return Botyara.INSTANCE.getFileManager()
                .getProperties().getBoolean(path, (boolean) def);
    }

    public List<String> asStringList() {
        return Botyara.INSTANCE.getFileManager()
                .getProperties().getStringList(path);
    }

    public double asDouble() {
        return Botyara.INSTANCE.getFileManager()
                .getProperties().getDouble(path, (double) def);
    }
}
