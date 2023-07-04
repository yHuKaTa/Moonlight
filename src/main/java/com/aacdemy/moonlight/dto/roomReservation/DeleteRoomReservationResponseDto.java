package com.aacdemy.moonlight.dto.roomReservation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteRoomReservationResponseDto {
    private String errorMessage;
    private String status;

    public DeleteRoomReservationResponseDto(String status) {
        this.status = status;
    }
}
