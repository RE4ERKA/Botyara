package me.re4erka.botyara.bot.user;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.re4erka.botyara.api.bot.user.UserData;
import me.re4erka.botyara.database.DatabaseManager;
import me.re4erka.botyara.file.type.Properties;
import org.apache.logging.log4j.message.ParameterizedMessage;

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
     * Карта имеет лимиты которые указывается в properties.json
     *
     * Через время указанное в properties.json - карта убирает
     * пользователя, что уменьшает потребление оперативной памяти.
     * */
    @Getter
    private final Cache<Long, UserData> cache;

    private final DatabaseManager databaseManager;

    public Users(DatabaseManager databaseManager) {
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

    public void find(final long id, final Consumer<UserData> action) {
        final UserData data = cache.getIfPresent(id);

        if (data == null) {
            databaseManager.getUserData(id).thenAccept(optionalUserData ->
                optionalUserData.ifPresentOrElse(userData -> {
                    userData.checkLastDialog();

                    action.accept(userData);
                    cache.put(id, userData);
                }, () -> {
                    final UserData userData = UserData.newStranger(id);

                    action.accept(userData);
                    cache.put(id, userData);
                })
            );
        } else {
            action.accept(data);
        }
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
