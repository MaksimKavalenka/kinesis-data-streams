package org.learning.kds.utils;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class DateConverter {

    public static LocalDateTime convert(String date) {
        return ZonedDateTime.parse(date).toLocalDateTime();
    }

}
