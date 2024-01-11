package com.deft.watchman.service.impl;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * @author Sergey Golitsyn
 * created on 11.01.2024
 */
public class TimeHelper {

    public static Instant getStartOfWeekInstant() {
        LocalDate localDate = LocalDate.now().with(DayOfWeek.MONDAY);
        // Specify the timezone
        ZoneId zoneId = ZoneId.systemDefault();

        return localDate.atStartOfDay(zoneId).toInstant();
    }
}
