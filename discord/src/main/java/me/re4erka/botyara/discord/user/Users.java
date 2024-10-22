package me.re4erka.botyara.discord.user;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.re4erka.botyara.api.bot.user.UserData;
import me.re4erka.botyara.database.DatabaseManager;
import me.re4erka.botyara.file.type.Properties;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Log4j2
public class Users {
    /*
     * Карта пользователей с айди и данными пользователя.
     *
     * Является кешом, чтобы сократить обращения к базе данных.
     *
     * Карта имеет лимиты которые указывается в properties.yml
     *
     * Через время указанное в properties.yml - карта убирает
     * пользователя, что уменьшает потребление оперативной памяти.
     * */
    @Getter
    private final Cache<Long, UserData> cache;

    private final DatabaseManager databaseManager;

    public Users(@NotNull DatabaseManager databaseManager) {
        cache = CacheBuilder.newBuilder()
                .maximumSize(Properties.CACHE_USERS_MAXIMUM_SIZE.asInt())
                .expireAfterAccess(Properties.CACHE_USERS_EXPIRATION.asInt(), TimeUnit.MINUTES)
                .removalListener((RemovalListener<Long, UserData>) notification -> {
                    final UserData data = notification.getValue();

                    if (data == null || data.isStranger()) {
                        return;
                    }

                    databaseManager.addUserOrUpdate(
                            Objects.requireNonNull(notification.getKey()).toString(),
                            data.getName(),
                            data.getFriendshipType(),
                            data.getReputation()
                    ).exceptionally(throwable -> {
                        log.error(
                                new ParameterizedMessage(
                                        "Failed to save user with id '{}' in the database!",
                                        data.getId()
                                ),
                                throwable
                        );
                        return null;
                    });
                })
                .build();

        this.databaseManager = databaseManager;
    }

    public void find(long id, @NotNull Consumer<UserData> action) {
        final UserData cachedUserData = cache.getIfPresent(id);

        if (cachedUserData == null) {
            databaseManager.getUserData(id).thenAccept(userData ->
                    userData.ifPresentOrElse(data -> {
                        data.checkLastDialog();
                        action.accept(data);

                        cache.put(id, data);
                    }, () -> {
                        action.accept(UserData.newStranger());
                        cache.put(id, UserData.newStranger());
                    }
            ));
        }
        else {
            action.accept(cachedUserData);
        }
    }

    public void intoFamiliar(long id, @NotNull String name) {
        cache.put(id, UserData.newFamiliar(id, name));
    }

    public void saveAll() {
        log.info("Saving users...");

        for (Map.Entry<Long, UserData> entry : cache.asMap().entrySet()) {
            final UserData data = entry.getValue();

            if (data.isStranger()) {
                continue;
            }

            databaseManager.addUserOrUpdate(
                    entry.getKey().toString(),
                    data.getName(),
                    data.getFriendshipType(),
                    data.getReputation()
            ).join();
        }
    }
}
