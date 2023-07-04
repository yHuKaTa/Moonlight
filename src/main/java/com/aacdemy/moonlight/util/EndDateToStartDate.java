package com.aacdemy.moonlight.util;

import jakarta.validation.ValidationException;

import java.time.LocalDate;

public class EndDateToStartDate {
    public static void isEndDateBeforeStartDate(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new ValidationException("Please enter a valid end date that is after start date!");
        }
    }
}
