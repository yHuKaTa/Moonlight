package com.aacdemy.moonlight.service;

import com.aacdemy.moonlight.dto.room.RoomFacilityRequestDto;
import com.aacdemy.moonlight.dto.room.RoomFacilityUpdRequestDto;
import com.aacdemy.moonlight.entity.hotel.Room;
import com.aacdemy.moonlight.entity.hotel.RoomFacility;

import java.util.List;

public interface RoomFacilityService {
    RoomFacility addFacility(RoomFacilityRequestDto roomFacilityRequestDto);

    RoomFacility getFacility(String facility);

    List<RoomFacility> getAllFacilities();

    void deleteFacility(RoomFacilityRequestDto roomFacilityRequestDto);

    RoomFacility updateFacility(RoomFacilityUpdRequestDto roomUpdFacilityRequestDto);

    RoomFacility addFacilityToRoom(Room room, RoomFacility roomFacility);

    RoomFacility removeFacilityFromRoom(Room room, RoomFacility roomFacility);
}
