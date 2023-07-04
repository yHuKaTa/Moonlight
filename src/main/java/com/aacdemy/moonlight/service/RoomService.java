package com.aacdemy.moonlight.service;

import com.aacdemy.moonlight.dto.room.RoomFacilityRequestDto;
import com.aacdemy.moonlight.dto.room.RoomFacilityResponseDto;
import com.aacdemy.moonlight.entity.hotel.Room;
import com.aacdemy.moonlight.entity.hotel.RoomType;
import com.aacdemy.moonlight.entity.hotel.RoomView;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface RoomService {
    Room findById(Long id);

    Room findByRoomNumber(int roomNumber);

    List<Room> findByRoomType(RoomType roomType);

    List<Room> findByRoomView(RoomView roomView);

    List<Room> findByPrice(BigDecimal price);

    List<Room> findByPeople(int people);

    List<Room> findRoomsByFacility(String text);

    RoomFacilityResponseDto addFacilityToRoom(Long roomId, RoomFacilityRequestDto roomFacilityRequestDto);

    RoomFacilityResponseDto deleteFacilityFromRoom(Long roomId, RoomFacilityRequestDto roomFacilityRequestDto);
}