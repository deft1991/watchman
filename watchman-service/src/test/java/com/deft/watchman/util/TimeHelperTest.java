package com.deft.watchman.util;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author Sergey Golitsyn
 * created on 05.02.2024
 */
class TimeHelperTest {

    @Test
    void getStartOfWeekInstant_ReturnsStartOfWeek() {
        // Set a specific date for testing purposes
        LocalDate testDate = LocalDate.now();

        // Set the expected start of the week instant based on the test date
        Instant expectedInstant = testDate.atStartOfDay(ZoneId.systemDefault()).toInstant();

        // Call the method to get the start of the week instant
        Instant actualInstant = TimeHelper.getStartOfWeekInstant();

        // Verify that the actual instant matches the expected instant
        assertEquals(expectedInstant, actualInstant);
    }
}
