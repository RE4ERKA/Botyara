package me.re4erka.botyara.api.bot.activity.scheduler;

import com.google.common.collect.ImmutableSet;
import lombok.extern.log4j.Log4j2;
import me.re4erka.botyara.api.bot.Bot;
import me.re4erka.botyara.api.bot.activity.Activity;
import me.re4erka.botyara.api.bot.scheduler.BotScheduler;
import me.re4erka.botyara.api.util.random.Random;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Log4j2
public class ActivityScheduler extends BotScheduler {

    private final ImmutableSet<Activity> activities;

    private ActivityScheduler(@NotNull Bot bot,
                              @NotNull ImmutableSet<Activity> activities,
                              int origin, int bound) {
        super("Activity", bot);
        this.activities = activities;

        executor.scheduleAtFixedRate(this::onUpdate, 0, Random.range(origin, bound), TimeUnit.MINUTES);
    }

    @Override
    public CompletableFuture<Void> update() {
        return CompletableFuture.runAsync(this::onUpdate, executor);
    }

    private void onUpdate() {
        if (bot.isSleep()) {
            return;
        }

        for (Activity activity : activities) {
            if (check(activity)) {
                break;
            }
        }
    }

    private boolean check(@NotNull Activity activity) {
        try {
            if (activity.update(bot)) {
                return true;
            }
        } catch (Exception e) {
            log.error("Error executing scheduled activity: {}", activity.getClass().getName(), e);
        }

        return false;
    }

    public static ActivityScheduler.Builder builder(@NotNull Bot bot) {
        return new ActivityScheduler.Builder(bot);
    }

    public static final class Builder {
        private final Bot bot;

        private final ImmutableSet.Builder<Activity> activitiesBuilder = ImmutableSet.builder();

        private int origin = 30;
        private int bound = 60;

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

        public Builder add(@NotNull Activity activity) {
            activitiesBuilder.add(activity);
            return this;
        }

        public ActivityScheduler build() {
            return new ActivityScheduler(
                    bot,
                    activitiesBuilder.build(),
                    origin,
                    bound
            );
        }
    }
}
