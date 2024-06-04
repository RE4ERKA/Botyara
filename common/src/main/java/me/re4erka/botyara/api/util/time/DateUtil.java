package me.re4erka.botyara.api.util.time;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;

@UtilityClass
public class DateUtil {
    public LocalDate parse(String date) {
        final String[] value = StringUtils.split(date, '-');

        return LocalDate.of(
                Integer.parseUnsignedInt(value[0]),
                Integer.parseUnsignedInt(value[1]),
                Integer.parseUnsignedInt(value[2])
        );
    }
}
