package com.aacdemy.moonlight.dto.roomReservation;

import com.aacdemy.moonlight.util.annotation.UppercaseEnum;
import com.aacdemy.moonlight.entity.hotel.RoomBedType;
import com.aacdemy.moonlight.entity.hotel.RoomType;
import com.aacdemy.moonlight.entity.hotel.RoomView;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RoomReservationUpdateRequestDto {
    @NotNull(message = "Room reservation ID is required parameter")
    @Digits(integer = 20, fraction = 0, message = "The room reservation ID should be filled with digits")
    @Positive(message = "Room reservation ID should be positive value")
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "The start date for room reservation must be present or future date")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "The end date for room reservation must be present or future date")
    private LocalDate endDate;

    // @Pattern(regexp = "(SEA)|(GARDEN)|(POOL)", message = "Input valid room view: SEA, GARDEN or POOL")
    @UppercaseEnum(enumClass = RoomView.class)
    private String roomView;

    //   @Pattern(regexp = "(STANDARD)|(STUDIO)|(APARTMENT)", message = "Input valid room type: STANDARD, STUDIO or APARTMENT")
    @UppercaseEnum(enumClass = RoomType.class)
    private String roomType;

    @Digits(integer = 20, fraction = 0, message = "The adults value should be filled with digits")
    @Positive(message = "Adults value should be positive value")
    private Integer adults;

    @Digits(integer = 20, fraction = 0, message = "The children value should be filled with digits")
    @PositiveOrZero(message = "Children value should be positive value or zero")
    private Integer children;

    @Pattern(regexp = "(PAID)|(UNPAID)", message = "Input valid payment status: PAID or UNPAID")
    private String status;

    //    @Pattern(regexp = "(TWIN_BEDS)|(BEDROOM)|(TWIN BEDS)", message = "Input valid bed type: TWIN_BEDS or BEDROOM")
    @UppercaseEnum(enumClass = RoomBedType.class)
    private String bedType;

}
