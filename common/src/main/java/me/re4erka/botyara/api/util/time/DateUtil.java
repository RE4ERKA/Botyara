package me.re4erka.botyara.api.util.time;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public LocalDate parse(String date) {
        return LocalDate.parse(date, DATE_TIME_FORMATTER);
    }
}
