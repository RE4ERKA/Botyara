package me.re4erka.botyara.api.bot.sleep.scheduler;

import me.re4erka.botyara.api.bot.Bot;
import me.re4erka.botyara.api.bot.scheduler.BotScheduler;
import me.re4erka.botyara.api.bot.sleep.SleepQuality;
import me.re4erka.botyara.api.util.random.Random;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class SleepScheduler extends BotScheduler {

    // Время, когда бот должен лечь спать
    private final int sleepHours;

    // Время, когда бот должен проснуться
    private final int wakeUpHours;

    // Сколько бот должен поспать в минутах
    private final int requiredSleepMinutes;

    // Значение деления для вычисления качества сна
    private final double divisionOfSleepQuality;

    // Время, когда бот последний раз лег спать
    private ZonedDateTime lastSleepTime;

    // Сколько фактически бот проспал (в минутах)
    private int actualSleepMinutes;

    protected SleepScheduler(@NotNull Bot bot,
                             int origin, int bound,
                             int sleepHours, int wakeUpHours,
                             int requiredSleepMinutes, double divisionOfSleepQuality) {
        super("Sleep", bot);

        this.sleepHours = sleepHours;
        this.wakeUpHours = wakeUpHours;

        this.requiredSleepMinutes = requiredSleepMinutes;
        this.divisionOfSleepQuality = divisionOfSleepQuality;

        executor.scheduleAtFixedRate(
                this::onUpdate,
                0,
                Random.range(origin, bound),
                TimeUnit.MINUTES
        );
    }

    @Override
    public CompletableFuture<Void> update() {
        return CompletableFuture.runAsync(this::onUpdate, executor);
    }

    public SleepQuality getSleepQuality() {
        final int sleepDifference = requiredSleepMinutes - actualSleepMinutes;

        if (sleepDifference <= 0) {
            return SleepQuality.HIGH;
        } else if (sleepDifference <= requiredSleepMinutes / divisionOfSleepQuality) {
            return SleepQuality.MEDIUM;
        } else {
            return SleepQuality.LOW;
        }
    }

    private void onUpdate() {
        final ZonedDateTime currentTime = bot.getCurrentDateTime();

        ZonedDateTime todaySleepTime = formatTime(currentTime, sleepHours);
        ZonedDateTime todayWakeUpTime = formatTime(currentTime, wakeUpHours);

        // Если время сна уже прошло, переносим его на следующий день
        if (todaySleepTime.isBefore(currentTime)) {
            todaySleepTime = todaySleepTime.plusDays(1);
        }

        // Если время пробуждения уже прошло, переносим его на следующий день
        if (todayWakeUpTime.isBefore(todaySleepTime)) {
            todayWakeUpTime = todayWakeUpTime.plusDays(1);
        }

        // Проверяем, нужно ли боту ложиться спать
        if (currentTime.isAfter(todaySleepTime) && currentTime.isBefore(todayWakeUpTime)) {
            bot.sleep();
            lastSleepTime = currentTime;
        }

        // Проверяем, нужно ли боту проснуться
        if (currentTime.isAfter(todayWakeUpTime)) {
            bot.wakeUp();
            calculateActualSleep();
        }
    }

    private void calculateActualSleep() {
        if (lastSleepTime != null) {
            final Duration sleepDuration = Duration.between(
                    lastSleepTime,
                    bot.getCurrentDateTime()
            );

            actualSleepMinutes = (int) sleepDuration.toMinutes();
        } else {
            actualSleepMinutes = 0;
        }
    }

    private ZonedDateTime formatTime(@NotNull ZonedDateTime dateTime, int hours) {
        return dateTime.withHour(hours)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    public static SleepScheduler.Builder builder(@NotNull Bot bot) {
        return new SleepScheduler.Builder(bot);
    }

    public static final class Builder {
        private final Bot bot;

        private int origin = 30;
        private int bound = 60;

        private int sleepHours;
        private int wakeUpHours;
        private int requiredSleepMinutes;
        private double divisionOfSleepQuality;

        public Builder(@NotNull Bot bot) {
            this.bot = bot;
        }

        public Builder setOrigin(int origin) {
            this.origin = origin;
            return this;
        }

        public Builder setBound(int bound) {
            this.bound = bound;
            return this;
        }

        public Builder setSleepHours(@Range(from = 0, to = 24) int sleepHours) {
            this.sleepHours = sleepHours;
            return this;
        }

        public Builder setWakeUpHours(@Range(from = 0, to = 24) int wakeUpHours) {
            this.wakeUpHours = wakeUpHours;
            return this;
        }

        public Builder setRequiredSleepMinutes(int requiredSleepMinutes) {
            this.requiredSleepMinutes = requiredSleepMinutes;
            return this;
        }

        public Builder setDivisionOfSleepQuality(double divisionOfSleepQuality) {
            this.divisionOfSleepQuality = divisionOfSleepQuality;
            return this;
        }

        public SleepScheduler build() {
            return new SleepScheduler(
                    bot, origin, bound, sleepHours, wakeUpHours, requiredSleepMinutes, divisionOfSleepQuality
            );
        }
    }
}
