package me.re4erka.api.bot.activity.scheduler;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.re4erka.api.bot.activity.Activity;

import java.time.Duration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ActivityScheduler {
    private final Timer timer = new Timer("activity");
    private final Set<Activity> activities = new LinkedHashSet<>();

    public void updateNow(Class<? extends Activity> excludeActivity) {
        for (Activity activity : activities) {
            if (activity.getClass() == excludeActivity) {
                continue;
            }

            if (activity.update()) {
                break;
            }
        }
    }

    public void stop() {
        timer.cancel();
    }

    private long randomPeriod(int origin, int bound) {
        return Duration.ofMinutes(
                ThreadLocalRandom.current().nextInt(origin, bound)
        ).toMillis();
    }

    public static ActivityScheduler.Builder newBuilder() {
        return new ActivityScheduler().new Builder();
    }

    public class Builder {
        private int origin = 30;
        private int bound = 60;

        public Builder setPeriod(int origin, int bound) {
            this.origin = origin;
            this.bound = bound;

            return this;
        }

        public Builder add(Activity activity) {
            activities.add(activity);

            return this;
        }

        public ActivityScheduler build() {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    for (Activity activity : activities) {
                        if (activity.update()) {
                            break;
                        }
                    }
                }
            }, 0, randomPeriod(origin, bound));

            return ActivityScheduler.this;
        }
    }
}
