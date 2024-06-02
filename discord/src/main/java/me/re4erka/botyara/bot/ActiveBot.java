package me.re4erka.botyara.bot;

import lombok.extern.log4j.Log4j2;
import me.re4erka.botyara.api.bot.activity.scheduler.ActivityScheduler;
import me.re4erka.botyara.api.bot.listener.ListeningBot;
import me.re4erka.botyara.api.bot.listener.common.IListener;
import me.re4erka.botyara.api.bot.mood.MoodType;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.api.history.HistoryManager;
import me.re4erka.botyara.history.ActivityHistory;
import me.re4erka.botyara.api.history.type.BotHistory;
import me.re4erka.botyara.api.history.type.UserHistory;
import me.re4erka.botyara.bot.activities.*;
import me.re4erka.botyara.bot.listeners.DefaultListener;
import me.re4erka.botyara.bot.listeners.FilterListener;
import me.re4erka.botyara.bot.listeners.RepetitionListener;
import me.re4erka.botyara.bot.listeners.SleepingListener;
import me.re4erka.botyara.file.type.Properties;
import org.apache.commons.lang3.StringUtils;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.activity.Activity;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.user.UserStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@Log4j2
public class ActiveBot extends ListeningBot {
    private final DiscordApi api;

    /*
     * Основной слушатель.
     *
     * Выполняется после всех слушателей.
     *  */
    private final IListener defaultListener = new DefaultListener();

    /*
     * Слушатель при сне.
     *
     * Выполняется если Бот - спит.
     *  */
    private final IListener sleepingListener = new SleepingListener();

    /*
    * Слушатель при повторении предложений.
    *
    * Выполняется после всех слушателей, но до основного.
    * */
    private final IListener repetitionListener = new RepetitionListener();

    /*
    * Слушатель при запрещенных словах.
    *
    * Выполняется до любых слушателей, бот ничего отвечать не будет.
    * */
    private final IListener filterListener = new FilterListener();

    private final ActivityScheduler scheduler;

    public static final ZoneId ZONE_ID = ZoneId.of(
            Properties.ACTIVITIES_SLEEPING_ZONE_ID.asString()
    );

    public static final UserHistory USER_HISTORY = HistoryManager.newUser("ListeningBot")
            .logging((message, receiver) -> {
                message.replace("user_id", String.valueOf(receiver.getId()));
                message.replace("user_name", receiver.getName());
                message.replace("friendship_type", receiver.getFriendshipType().toString());
            });
    private static final ActivityHistory ACTIVITY_HISTORY = new ActivityHistory(
            "Статус активности обновлен. Тип: %activity_type%. Текст: '%activity_text%'"
    );

    private final BotHistory history = HistoryManager.newBot("ActiveBot", this)
            .logging((message, bot) -> message.replace("bot_mood", bot.getMood().toString()));

    public ActiveBot(DiscordApi api) {
        super(Properties.LISTENER_AWAITING_MAXIMUM_SIZE.asInt(),
                Properties.LISTENER_ASK_MAXIMUM_SIZE.asInt());

        ActivityScheduler.Builder builder = ActivityScheduler.builder()
                .setOrigin(Properties.SCHEDULER_UPDATE_PERIOD_ORIGIN.asInt())
                .setBound(Properties.SCHEDULER_UPDATE_PERIOD_BOUND.asInt());

        if (Properties.ACTIVITIES_SLEEPING_ENABLED.asBoolean()) {
            builder.add(new SleepActivity(this));
        }

        builder.add(new MoodActivity(this));

        if (Properties.ACTIVITIES_LISTENING_ENABLED.asBoolean()) {
            builder.add(new ListeningActivity(this));
        }

        if (Properties.ACTIVITIES_PLAYING_ENABLED.asBoolean()) {
            builder.add(new PlayingActivity(this));
        }

        if (Properties.ACTIVITIES_WATCHING_ENABLED.asBoolean()) {
            if (Properties.ACTIVITIES_WATCHING_API_KEY.asString().equals("EMPTY")) {
                log.warn("Set the key for activities watching to work in 'properties.yml'!");
                log.warn("https://developers.google.com/youtube/v3/getting-started");
            } else {
                builder.add(new WatchActivity(this));
            }
        }

        this.api = api;
        this.scheduler = builder.build();
    }

    @Override
    public void onListen(Receiver receiver, Words words) {
        if (filterListener.onListen(receiver, words)) {
            USER_HISTORY.log(
                    StringUtils.replaceOnce(
                            "Запрещенное сообщение. Сообщение: '%user_message%'. Пользователь: %user_name%(%user_id%)",
                            "%user_message%",
                            words.toString()
                    ),
                    receiver,
                    words.isPrivate()
            );

            return;
        }

        if (listenAwaiting(receiver, words)) {
            return;
        }

        /*
         * Проверяем обращался ли пользователь к боту
         * */
        if (words.isUserCalledBot()) {
            /*
             * Переменная спит ли Бот.
             *
             * Если да, то он просыпается и выполняется слушатель - SleepingListener.
             */
            if (isSleep) {
                sleep(false);

                scheduler.updateNow(SleepActivity.class);

                sleepingListener.onListen(receiver, words);

                return;
            }

            /* Проверяем задавал ли вопрос к прошлом слушателю пользователь */
            if (listenAsk(receiver, words)) {
                return;
            }

            /* Отправляем всем прослушивателем получателя (receiver) и слова (words) */
            if (listen(receiver, words)) {
                return;
            }

            /* Проверяем повторяются ли предложения ранее написанные */
            if (repetitionListener.onListen(receiver, words)) {
                return;
            }

            /*
             * Если условия слушателей не выполнилось, то выполнится действие по-дефолту
             *
             * Это обычные ответы: Да, Нет, Наверное...
             *
             * У defaultListener нету условий - так как его условия были выполнены
             * еще в начале метода, делать их повторно - не имеет смысла.
             * */
            defaultListener.onListen(receiver, words);
        }
    }

    @Override
    public void setMood(MoodType type) {
        this.mood = type;
        history.log("Статус настроения обновлен. Тип: %bot_mood%.");
    }

    @Override
    public void sleep(boolean isSleep) {
        this.isSleep = isSleep;

        if (isSleep) {
            if (api.getStatus() != UserStatus.IDLE) {
                api.updateStatus(UserStatus.IDLE);
                api.unsetActivity();
                history.log("Бот уснул.");
            }
        } else {
            if (api.getStatus() != UserStatus.ONLINE) {
                api.updateStatus(UserStatus.ONLINE);
                history.log("Бот проснулся.");
            }
        }
    }

    @Override
    public void watch(String title) {
        api.updateActivity(ActivityType.WATCHING, title);
        ACTIVITY_HISTORY.log(ActivityType.WATCHING, title);
    }

    @Override
    public void listen(String song) {
        api.updateActivity(ActivityType.LISTENING, song);
        ACTIVITY_HISTORY.log(ActivityType.LISTENING, song);
    }

    @Override
    public void play(String game) {
        api.updateActivity(ActivityType.PLAYING, game);
        ACTIVITY_HISTORY.log(ActivityType.PLAYING, game);
    }

    @Override
    public int getCurrentTime() {
        return ZonedDateTime.now(ZONE_ID).getHour();
    }

    public Optional<Activity> getDiscordActivity() {
        return api.getActivity();
    }

    public void stop() {
        scheduler.shutdown();
        cleanUp().unregisterAll();
    }
}
