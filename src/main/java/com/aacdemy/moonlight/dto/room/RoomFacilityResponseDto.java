package com.aacdemy.moonlight.dto.room;

import com.aacdemy.moonlight.entity.hotel.RoomFacility;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomFacilityResponseDto {

    private List<RoomFacility> facilities;
}
