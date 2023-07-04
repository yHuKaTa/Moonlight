package com.aacdemy.moonlight.dto.roomReservation;

import com.aacdemy.moonlight.entity.PaymentStatus;
import com.aacdemy.moonlight.entity.hotel.Room;
import com.aacdemy.moonlight.entity.hotel.RoomBedType;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Data
@Getter
public class RoomReservationUpdateResponseDto {

    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private int adults;

    private int children;

    private Room room;

    private PaymentStatus status;

    private RoomBedType type;

}
