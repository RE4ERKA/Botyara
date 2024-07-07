package me.re4erka.botyara.api.bot.activity.scheduler;

import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.log4j.Log4j2;
import me.re4erka.botyara.api.bot.activity.Activity;
import me.re4erka.botyara.api.util.random.Random;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Log4j2
public class ActivityScheduler {
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(
            new ThreadFactoryBuilder()
                    .setNameFormat("Activity-Scheduler-Thread")
                    .setPriority(Thread.NORM_PRIORITY)
                    .setDaemon(false)
                    .build()
    );
    private final ImmutableSet<Activity> activities;

    private ActivityScheduler(@NotNull ImmutableSet<Activity> activities, int origin, int bound) {
        this.activities = activities;

        executor.scheduleAtFixedRate(() -> {
            for (Activity activity : activities) {
                if (check(activity)) {
                    break;
                }
            }
        }, 0, Random.range(origin, bound), TimeUnit.MINUTES);
    }

    public void updateNow(@NotNull Class<? extends Activity> excludeActivity) {
        for (Activity activity : activities) {
            if (activity.getClass() == excludeActivity) {
                continue;
            }

            if (check(activity)) {
                break;
            }
        }
    }

    public void shutdown() {
        try {
            executor.shutdown();

            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                log.warn("Executor did not terminate in the specified time.");

                executor.shutdownNow();
                if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                    log.error("Executor did not terminate after shutdownNow.");
                }
            }
        } catch (InterruptedException e) {
            log.error("Shutdown interrupted: {}", e.getMessage(), e);

            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private boolean check(@NotNull Activity activity) {
        try {
            if (activity.update()) {
                return true;
            }
        } catch (Exception e) {
            log.error("Error executing scheduled activity: {}", activity.getClass().getName(), e);
        }

        return false;
    }

    public static ActivityScheduler.Builder builder() {
        return new ActivityScheduler.Builder();
    }

    public static final class Builder {
        private int origin = 30;
        private int bound = 60;

        private final ImmutableSet.Builder<Activity> activitiesBuilder = ImmutableSet.builder();

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
                    activitiesBuilder.build(),
                    origin,
                    bound
            );
        }
    }
}
