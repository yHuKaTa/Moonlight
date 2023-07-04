package com.aacdemy.moonlight.dto.room;

import com.aacdemy.moonlight.entity.hotel.RoomFacility;
import com.aacdemy.moonlight.entity.hotel.RoomType;
import com.aacdemy.moonlight.entity.hotel.RoomView;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoomRequestDto {

    private int roomNumber;
    private RoomType type;
    private RoomView roomView;
    private BigDecimal price;
    private int people;
    private List<RoomFacility> facilities = new ArrayList<>();
}
