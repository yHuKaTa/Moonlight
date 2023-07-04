package com.aacdemy.moonlight.dto.screen;

import com.aacdemy.moonlight.entity.screen.ScreenEvent;
import com.aacdemy.moonlight.entity.screen.ScreenSeat;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ScreenReservationResponseDto {
    private ScreenEvent event;
    private Set<ScreenSeat> reservedSeats;
}
