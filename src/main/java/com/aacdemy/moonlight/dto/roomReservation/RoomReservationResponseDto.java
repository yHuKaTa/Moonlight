package com.aacdemy.moonlight.dto.roomReservation;

import com.aacdemy.moonlight.entity.hotel.RoomReservation;
import com.aacdemy.moonlight.entity.hotel.RoomType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoomReservationResponseDto {

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private RoomType roomType;
    private int adults;
    private int children;
    private BigDecimal price;
    private BigDecimal fullPrice;
    private int daysStaying;

    public RoomReservationResponseDto(RoomReservationRequestDto reservationRequestDto, RoomReservation savedReservation) {
        this.startDate = reservationRequestDto.getStartDate();
        this.endDate = reservationRequestDto.getEndDate();
        this.roomType = savedReservation.getRoom().getType();
        this.adults = reservationRequestDto.getAdults();
        this.children = reservationRequestDto.getChildren();
        this.price = savedReservation.getRoom().getPrice();
        this.fullPrice = savedReservation.getFullPrice();
        this.daysStaying = (int) ChronoUnit.DAYS.between(startDate, endDate);
    }
}