package me.re4erka.botyara.discord;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.re4erka.botyara.api.bot.listener.config.ConfigListener;
import me.re4erka.botyara.api.bot.listener.loader.ListenerLoader;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.api.manager.Manager;
import me.re4erka.botyara.Botyara;
import me.re4erka.botyara.bot.DiscordBot;
import me.re4erka.botyara.bot.receiver.DiscordReceiver;
import me.re4erka.botyara.bot.receiver.type.DataReceiver;
import me.re4erka.botyara.bot.receiver.type.EmptyReceiver;
import me.re4erka.botyara.discord.user.Users;
import me.re4erka.botyara.file.type.Properties;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.message.Message;
import org.javacord.core.BotActivity;

import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public class DiscordManager extends Manager {
    @Getter
    private DiscordBot bot;
    private DiscordApi api;

    @Getter
    private Users users;

    @Override
    public boolean start() {
        log.info("Launching the bot...");

        if (Properties.BOT_TOKEN.asString().equals("EMPTY")) {
            log.warn("Set the token for the bot in the 'properties.yml' file!");
            log.warn("https://discord.com/developers/docs/topics/oauth2");

            if (!Properties.BOT_DEBUG.asBoolean()) {
                return false;
            }
        }

        api = new DiscordApiBuilder()
                .setToken(Properties.BOT_TOKEN.asString())
                .addIntents(Intent.MESSAGE_CONTENT)
                .setWaitForServersOnStartup(false)
                .setWaitForUsersOnStartup(false)
                .setTrustAllCertificates(false)
                .setUserCacheEnabled(true)
                .login().join();

        bot = new DiscordBot(api);

        users = new Users(Botyara.INSTANCE.getDatabaseManager());

        api.setMessageCacheSize(
                Properties.CACHE_MESSAGE_MAXIMUM_SIZE.asInt(),
                Properties.CACHE_MESSAGE_EXPIRATION.asInt()
        );

        api.addMessageCreateListener(event -> {
            if (event.getMessageAuthor().isBotUser()) {
                return;
            }

            final Message message = event.getMessage();

            // TODO: Вызывать уникальный слушатель или систему проверок слушателей.
            if (message.isPrivateMessage()) {
                return;
            }

            final long id = message.getAuthor().getId();

            users.find(id, userData -> {
                final DiscordReceiver receiver = userData.isStranger()
                        ? new EmptyReceiver(message, bot)
                        : new DataReceiver(message, userData, bot);

                final Words words = Words.create(
                        message.getReadableContent()
                );

                bot.listen(receiver, words);
            });
        });

        loadListeners();

        return true;
    }

    public void loadListeners() {
        final AtomicInteger count = new AtomicInteger();

        final String packagePrefix = DiscordBot.class.getPackage().getName();
        final String[] packageNames = new String[] {
                packagePrefix + ".listeners.generic",
                packagePrefix + ".listeners.await",
                packagePrefix + ".listeners.friendship"
        };

        ListenerLoader.fromPackage(
                packageNames,
                bot
        ).forEach(listener -> {
            bot.register(listener);
            count.getAndIncrement();
        });

        if (!Properties.BOT_DEBUG.asBoolean()) {
            ListenerLoader.fromDirectory(
                    Botyara.INSTANCE.getJarDirectory(),
                    Properties.LISTENER_IGNORE_EMPTY_LISTENERS_FOLDER.asBoolean()
            ).forEach(configHandler -> {
                bot.register(
                        new ConfigListener(configHandler, bot)
                );

                count.getAndIncrement();
            });
        }

        /* Убираем все слушатели в черном списке из конфига */
        Properties.LISTENER_BLACKLIST.asStringList()
                .forEach(name -> {
                    if (bot.unregister(name)) {
                        count.decrementAndGet();
                    }
                });

        log.info("There was successfully loaded {} listeners!", count.get());
    }

    @Override
    public void stop() {
        if (api != null) {
            log.info("Disconnecting from the Discord...");

            bot.shutdown();
            api.disconnect().join();
            users.saveAll();
        }
    }
}
