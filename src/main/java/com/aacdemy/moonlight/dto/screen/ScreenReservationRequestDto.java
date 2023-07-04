package com.aacdemy.moonlight.dto.screen;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ScreenReservationRequestDto {
    @NotNull(message = "The date for event reservation shouldn't be blank!")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "The date for event reservation must be present or future date")
    private LocalDate dateOfEvent;

    @NotNull(message = "The event ID shouldn't be blank!")
    @Digits(integer = 20, fraction = 0, message = "The event ID should be filled with digits")
    @Positive(message = "Event ID should be positive value")
    private Long eventId;

    @NotEmpty(message = "Seat's numbers shouldn't be blank!")
    private List<@Digits(integer = 2, fraction = 0, message = "The seat's numbers should be filled with digits")
    @Range(min = 1, max = 21, message = "The seat's numbers should be between 1 and 21") Integer> seatNumbers;
}
