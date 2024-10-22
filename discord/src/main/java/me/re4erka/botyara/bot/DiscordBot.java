package me.re4erka.botyara.bot;

import lombok.extern.log4j.Log4j2;
import me.re4erka.botyara.api.bot.activity.Activity;
import me.re4erka.botyara.api.bot.activity.scheduler.ActivityScheduler;
import me.re4erka.botyara.api.bot.listener.ListeningBot;
import me.re4erka.botyara.api.bot.listener.common.IListener;
import me.re4erka.botyara.api.bot.mood.MoodType;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.bot.response.PendingResponse;
import me.re4erka.botyara.api.bot.sleep.SleepQuality;
import me.re4erka.botyara.api.bot.sleep.scheduler.SleepScheduler;
import me.re4erka.botyara.api.bot.word.Words;
import me.re4erka.botyara.api.history.HistoryFactory;
import me.re4erka.botyara.api.history.type.SimpleHistory;
import me.re4erka.botyara.api.history.type.ActivityHistory;
import me.re4erka.botyara.api.history.type.UserHistory;
import me.re4erka.botyara.bot.activities.*;
import me.re4erka.botyara.bot.listeners.DefaultListener;
import me.re4erka.botyara.bot.listeners.FilterListener;
import me.re4erka.botyara.bot.listeners.RepetitionListener;
import me.re4erka.botyara.bot.listeners.SleepingListener;
import me.re4erka.botyara.file.type.Properties;
import me.re4erka.botyara.voice.VoiceManager;
import org.apache.commons.lang3.StringUtils;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.Nameable;
import org.javacord.api.entity.activity.ActivityParty;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.user.UserStatus;
import org.jetbrains.annotations.NotNull;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Log4j2
public class DiscordBot extends ListeningBot {
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

    private final SleepScheduler sleepScheduler;
    private final ActivityScheduler activityScheduler;

    private final VoiceManager voiceManager;

    public static final ZoneId ZONE_ID = ZoneId.of(
            Properties.SCHEDULER_SLEEP_ZONE_ID.asString()
    );

    public static final UserHistory USER_HISTORY = HistoryFactory.createUser("ListeningBot")
            .logging((message, receiver) -> {
                message.replace("user_id", Long.toString(receiver.getId()));
                message.replace("user_name", receiver.getName());
                message.replace("user_reputation", Integer.toString(receiver.getReputation()));
                message.replace("friendship_type", receiver.getFriendshipType().toString());
            });
    private static final ActivityHistory ACTIVITY_HISTORY = HistoryFactory.createActivity(
            "BotActivity",
            "Статус активности обновлен. Тип: %activity_type%. Текст: '%activity_text%'"
    );

    private final SimpleHistory history = HistoryFactory.createSimple("ActiveBot");

    public DiscordBot(@NotNull DiscordApi api) {
        super(Properties.LISTENER_WAITING_MAXIMUM_SIZE.asInt(),
                Properties.LISTENER_CLARIFYING_MAXIMUM_SIZE.asInt());
        this.api = api;

        log.info("Initializing SleepScheduler...");
        this.sleepScheduler = SleepScheduler.builder(this)
                .setOrigin(Properties.SCHEDULER_SLEEP_UPDATE_PERIOD_ORIGIN.asInt())
                .setBound(Properties.SCHEDULER_SLEEP_UPDATE_PERIOD_BOUND.asInt())
                .setSleepHours(Properties.SCHEDULER_SLEEP_HOURS_SLEEP.asInt())
                .setWakeUpHours(Properties.SCHEDULER_SLEEP_HOURS_WAKE_UP.asInt())
                .setRequiredSleepMinutes(Properties.SCHEDULER_SLEEP_REQUIRED_SLEEP.asInt())
                .setDivisionOfSleepQuality(Properties.SCHEDULER_SLEEP_DIVISION_OF_SLEEP_QUALITY.asDouble())
                .build();

        log.info("Initializing ActivityScheduler...");
        ActivityScheduler.Builder builder = ActivityScheduler.builder(this)
                .setOrigin(Properties.SCHEDULER_ACTIVITY_UPDATE_PERIOD_ORIGIN.asInt())
                .setBound(Properties.SCHEDULER_ACTIVITY_UPDATE_PERIOD_BOUND.asInt());

        if (Properties.ACTIVITIES_LISTENING_ENABLED.asBoolean()) {
            builder.add(new ListeningActivity());
        }

        if (Properties.ACTIVITIES_PLAYING_ENABLED.asBoolean()) {
            builder.add(new PlayingActivity());
        }

        if (Properties.ACTIVITIES_WATCHING_ENABLED.asBoolean()) {
            if (Properties.ACTIVITIES_WATCHING_API_KEY.asString().equals("EMPTY")) {
                log.warn("Set the key for activities watching to work in 'properties.yml'!");
                log.warn("https://developers.google.com/youtube/v3/getting-started");
            } else {
                builder.add(new WatchActivity());
            }
        }

        if (Properties.ACTIVITIES_EATING_ENABLED.asBoolean()) {
            builder.add(new EatingActivity());
        }

        if (Properties.ACTIVITIES_NOTHING_ENABLED.asBoolean()) {
            builder.add(new NothingActivity());
        }

        this.activityScheduler = builder.build();
        this.voiceManager = new VoiceManager(Properties.TEXT_TO_SPEECH_API_KEY.asString());
    }

    @Override
    public void listen(@NotNull Receiver receiver, @NotNull Words words) {
        if (filterListener.onListen(receiver, words)) {
            USER_HISTORY.log(
                    StringUtils.replaceOnce(
                            "Запрещенное сообщение. Сообщение: '%user_message%'. Пользователь: %user_name%(%user_id%)",
                            "%user_message%",
                            words.toString()
                    ),
                    receiver
            );

            return;
        }

        // Учитываем отвечает ли бот уже кому-то.
        if (isResponding()) {
            queueResponse(PendingResponse.of(receiver, words));
            return;
        }

        onListen(receiver, words);
    }

    @Override
    public void onListen(@NotNull Receiver receiver, @NotNull Words words) {
        if (listenAwaiting(receiver, words)) {
            return;
        }

        /*
         * Проверяем обращался ли пользователь к боту
         * */
        if (words.isDidUserMentionBot()) {
            beginResponse();

            /*
             * Переменная спит ли Бот.
             *
             * Если да, то он просыпается и выполняется слушатель - SleepingListener.
             */
            if (isSleep) {
                wakeUp();
                activityScheduler.update()
                        .thenRun(() -> sleepingListener.onListen(receiver, words));

                return;
            }

            /* Проверяем задавал ли вопрос к прошлом слушателю пользователь */
            if (listenAsk(receiver, words)) {
                return;
            }

            /* Отправляем всем прослушивателем получателя (receiver) и слова (words) */
            if (listenOther(receiver, words)) {
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
    public void setMood(@NotNull MoodType type) {
        this.mood = type;
        history.log("Статус настроения обновлен. Тип: %bot_mood%.");
    }

    @Override
    public void sleep() {
        this.isSleep = true;

        if (api.getStatus() != UserStatus.IDLE) {
            api.updateStatus(UserStatus.IDLE);
            api.unsetActivity();

            api.getCachedMessages().deleteAll();
            cleanUp(); // Обязательно очищаем, дабы избежать странностей.

            history.log("Бот уснул.");
        }
    }

    @Override
    public void wakeUp() {
        this.isSleep = false;

        if (api.getStatus() != UserStatus.ONLINE) {
            api.updateStatus(UserStatus.ONLINE);
            history.log("Бот проснулся.");
        }
    }

    @Override
    public SleepQuality getSleepQuality() {
        return sleepScheduler.getSleepQuality();
    }

    @Override
    public void updateActivities() {
        activityScheduler.update();
    }

    @Override
    public void updateActivity(@NotNull Activity.Type type, @NotNull String content) {
        switch (type) {
            case LISTENING -> {
                api.updateActivity(ActivityType.LISTENING, content);
                ACTIVITY_HISTORY.log(Activity.Type.LISTENING, content);
            }
            case PLAYING -> {
                api.updateActivity(ActivityType.PLAYING, content);
                ACTIVITY_HISTORY.log(Activity.Type.PLAYING, content);
            }
            case WATCHING -> {
                api.updateActivity(ActivityType.WATCHING, content);
                ACTIVITY_HISTORY.log(Activity.Type.WATCHING, content);
            }
            case EATING -> {
                api.updateActivity(ActivityType.CUSTOM, content);
                ACTIVITY_HISTORY.log(Activity.Type.EATING, content);
            }
            default -> log.error("Activity type '{}' is not supported for update!", type.toString());
        }
    }

    @Override
    public Activity.Type getActivityType() {
        return api.getActivity().map(activity -> switch (activity.getType()) {
            case PLAYING -> Activity.Type.PLAYING;
            case WATCHING -> Activity.Type.WATCHING;
            case LISTENING -> Activity.Type.LISTENING;
            case CUSTOM -> Activity.Type.EATING;
            default -> Activity.Type.NOTHING;
        }).orElse(Activity.Type.NOTHING);
    }

    @Override
    public String getActivityContent() {
        return api.getActivity().map(Nameable::getName).orElse(StringUtils.EMPTY);
    }

    @Override
    public void doesNothing() {
        api.unsetActivity();
        ACTIVITY_HISTORY.logNothing();
    }

    @Override
    public int getCurrentHours() {
        return ZonedDateTime.now(ZONE_ID).getHour();
    }

    @Override
    public ZonedDateTime getCurrentDateTime() {
        return ZonedDateTime.now(ZONE_ID);
    }

    public void shutdown() {
        sleepScheduler.shutdown();
        activityScheduler.shutdown();

        cleanUp().unregisterAll();
    }
}
