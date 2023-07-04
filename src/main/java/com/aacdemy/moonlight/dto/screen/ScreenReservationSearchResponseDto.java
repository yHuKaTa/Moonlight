package com.aacdemy.moonlight.dto.screen;

import com.aacdemy.moonlight.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScreenReservationSearchResponseDto {

    private Long id;
    private LocalDate date;
    private String eventName;
    private LocalDate eventDate;
    private String screenNumber;
    private PaymentStatus status;
    private Long userId;
}
