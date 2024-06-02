package me.re4erka.botyara.api.util.time;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Pluralizer {
    public String formatDays(long days) {
        if (days % 10 == 1 && days % 100 != 11) {
            return "день";
        } else if (days % 10 >= 2 && days % 10 <= 4 && (days % 100 < 10 || days % 100 >= 20)) {
            return "дня";
        } else {
            return "дней";
        }
    }

    public String formatHours(long hours) {
        if (hours % 10 == 1 && hours % 100 != 11) {
            return "час";
        } else if (hours % 10 >= 2 && hours % 10 <= 4 && (hours % 100 < 10 || hours % 100 >= 20)) {
            return "часа";
        } else {
            return "часов";
        }
    }

    public String formatMinutes(long minutes) {
        if (minutes % 10 == 1 && minutes % 100 != 11) {
            return "минута";
        } else if (minutes % 10 >= 2 && minutes % 10 <= 4 && (minutes % 100 < 10 || minutes % 100 >= 20)) {
            return "минуты";
        } else {
            return "минут";
        }
    }

    public String formatSeconds(long seconds) {
        if (seconds % 10 == 1 && seconds % 100 != 11) {
            return "секунда";
        } else if (seconds % 10 >= 2 && seconds % 10 <= 4 && (seconds % 100 < 10 || seconds % 100 >= 20)) {
            return "секунды";
        } else {
            return "секунд";
        }
    }
}
