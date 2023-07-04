
package com.aacdemy.moonlight.dto.roomReservation;

import com.aacdemy.moonlight.util.annotation.UppercaseEnum;
import com.aacdemy.moonlight.entity.hotel.RoomBedType;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoomReservationRequestDto {
    @NotNull(message = "The start date for room reservation shouldn't be blank!")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "The start date for room reservation must be present or future date")
    private LocalDate startDate;

    @NotNull(message = "The end date for room reservation shouldn't be blank!")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "The end date for room reservation must be present or future date")
    private LocalDate endDate;

    @NotNull(message = "The room ID shouldn't be blank!")
    @Digits(integer = 20, fraction = 0, message = "The room ID should be filled with digits")
    @Positive(message = "Room ID should be positive value")
    private Long roomId;

    @NotNull(message = "The number of adults shouldn't be blank!")
    @Digits(integer = 2, fraction = 0, message = "Adults field must have digits")
    @Positive(message = "Adults value should be positive value")
    private int adults;

    @NotNull(message = "The number of children shouldn't be blank!")
    @Digits(integer = 2, fraction = 0, message = "Children field must have digits")
    @PositiveOrZero(message = "Children value should be positive or zero value")
    private int children;

//    @Pattern(regexp = "(TWIN_BEDS)|(BEDROOM)", message = "Input valid bed type: TWIN_BEDS or BEDROOM")
    @UppercaseEnum(enumClass = RoomBedType.class)
    private String bedType;
}
