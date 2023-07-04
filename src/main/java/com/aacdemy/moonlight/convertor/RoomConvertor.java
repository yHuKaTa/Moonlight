package com.aacdemy.moonlight.convertor;

import com.aacdemy.moonlight.dto.room.RoomRequestDto;
import com.aacdemy.moonlight.dto.room.RoomResponseDto;
import com.aacdemy.moonlight.entity.hotel.Room;
import com.aacdemy.moonlight.entity.hotel.RoomFacility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoomConvertor {

    public static Room toRoomRequest(RoomRequestDto roomRequestDto) {

        return Room.builder()
                .roomNumber(roomRequestDto.getRoomNumber())
                .people(roomRequestDto.getPeople())
                .price(roomRequestDto.getPrice())
                .type(roomRequestDto.getType())
                .view(roomRequestDto.getRoomView())
                .facilities(roomRequestDto.getFacilities())
                .build();
    }
    public RoomResponseDto roomResponseDto(Room roomResponse){
        return RoomResponseDto.builder()
                .roomView(roomResponse.getView())
                .roomNumber(roomResponse.getRoomNumber())
                .facilities(
                        List.copyOf(
                                roomResponse.getFacilities().stream()
                                        .map(RoomFacility::getFacility)
                                        .toList()))
                .people(roomResponse.getPeople())
                .price(roomResponse.getPrice())
                .type(roomResponse.getType())
                .build();
    }

}