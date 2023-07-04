package com.aacdemy.moonlight.service.impl;

import com.aacdemy.moonlight.dto.room.RoomFacilityRequestDto;
import com.aacdemy.moonlight.dto.room.RoomFacilityResponseDto;
import com.aacdemy.moonlight.entity.hotel.Room;
import com.aacdemy.moonlight.entity.hotel.RoomFacility;
import com.aacdemy.moonlight.entity.hotel.RoomType;
import com.aacdemy.moonlight.entity.hotel.RoomView;
import com.aacdemy.moonlight.exception.EntityNotFoundException;
import com.aacdemy.moonlight.repository.hotel.RoomRepository;
import com.aacdemy.moonlight.service.RoomFacilityService;
import com.aacdemy.moonlight.service.RoomService;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    private final RoomFacilityService roomFacilityService;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository, RoomFacilityService roomFacilityService) {
        this.roomRepository = roomRepository;
        this.roomFacilityService = roomFacilityService;
    }

    @Override
    public Room findById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));
    }

    @Override
    public Room findByRoomNumber(int roomNumber) {
        return roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));
    }

    @Override
    public List<Room> findByRoomType(RoomType roomType) {
        return roomRepository.findByType(roomType);
    }

    @Override
    public List<Room> findByRoomView(RoomView roomView) {
        return roomRepository.findByView(roomView);
    }

    @Override
    public List<Room> findByPrice(BigDecimal price) {
        return roomRepository.findByPrice(price);
    }

    @Override
    public List<Room> findByPeople(int people) {
        return roomRepository.findByPeople(people);
    }

    public List<Room> findRoomsByFacility(String text) {
        return roomRepository.findByFacilitiesContaining(text);
    }

    @Override
    @Transactional
    public RoomFacilityResponseDto addFacilityToRoom(Long roomId, RoomFacilityRequestDto roomFacilityRequestDto) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));

        List<RoomFacility> facilities = new ArrayList<>(room.getFacilities());

        RoomFacility facility = roomFacilityService.getFacility(roomFacilityRequestDto.getFacility());

        if (!facilities.contains(facility)) {
            facilities.add(facility);
            roomRepository.save(room);
            roomFacilityService.addFacilityToRoom(room, facility);
        } else {
            throw new ValidationException("Room facility already exists in room!");
        }

        return (RoomFacilityResponseDto
                .builder()
                .facilities(facilities)
                .build());
    }

    @Override
    @Transactional
    public RoomFacilityResponseDto deleteFacilityFromRoom(Long roomId, RoomFacilityRequestDto roomFacilityRequestDto) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));

        List<RoomFacility> facilities = new ArrayList<>(room.getFacilities());

        RoomFacility facility = roomFacilityService.getFacility(roomFacilityRequestDto.getFacility());

        if (facilities.contains(facility)) {
            facilities.remove(facility);
            roomRepository.save(room);
            roomFacilityService.removeFacilityFromRoom(room, facility);
        } else {
            throw new ValidationException("Room facility doesn't exist in room!");
        }

        return (RoomFacilityResponseDto
                .builder()
                .facilities(facilities)
                .build());
    }
}
