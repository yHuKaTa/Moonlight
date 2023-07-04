package com.aacdemy.moonlight.service.impl;

import com.aacdemy.moonlight.dto.room.RoomFacilityRequestDto;
import com.aacdemy.moonlight.dto.room.RoomFacilityUpdRequestDto;
import com.aacdemy.moonlight.entity.hotel.Room;
import com.aacdemy.moonlight.entity.hotel.RoomFacility;
import com.aacdemy.moonlight.exception.EntityNotFoundException;
import com.aacdemy.moonlight.repository.hotel.RoomFacilityRepository;
import com.aacdemy.moonlight.service.RoomFacilityService;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomFacilityServiceImpl implements RoomFacilityService {

    private final RoomFacilityRepository roomFacilityRepository;

    @Autowired
    public RoomFacilityServiceImpl(RoomFacilityRepository roomFacilityRepository) {
        this.roomFacilityRepository = roomFacilityRepository;
    }

    @Override
    public RoomFacility addFacility(RoomFacilityRequestDto roomFacilityRequestDto) {
        if (!roomFacilityRepository.existsByFacility(roomFacilityRequestDto.getFacility())) {
            RoomFacility facility = new RoomFacility();
            facility.setFacility(roomFacilityRequestDto.getFacility());
            facility = roomFacilityRepository.save(facility);
            return facility;
        } else {
            throw new ValidationException("Room facility already exist");
        }
    }

    @Override
    public RoomFacility getFacility(String facility) {
        return roomFacilityRepository.findByFacility(facility)
                .orElseThrow(() -> new EntityNotFoundException("Room facility doesn't exist!"));
    }

    @Override
    public List<RoomFacility> getAllFacilities() {
        return roomFacilityRepository.findAll();
    }

    @Override
    public void deleteFacility(RoomFacilityRequestDto roomFacilityRequestDto) {
        RoomFacility facility = getFacility(roomFacilityRequestDto.getFacility());
        roomFacilityRepository.delete(facility);
    }

    @Override
    public RoomFacility updateFacility(RoomFacilityUpdRequestDto roomUpdFacilityRequestDto) {
        if (!roomFacilityRepository.existsByFacility(roomUpdFacilityRequestDto.getNewFacility())) {
            RoomFacility facility = getFacility(roomUpdFacilityRequestDto.getCurrentFacility());
            facility.setFacility(roomUpdFacilityRequestDto.getNewFacility());
            roomFacilityRepository.updateFacility(roomUpdFacilityRequestDto.getNewFacility(), facility.getId());
            return facility;
        } else {
            throw new ValidationException("Room facility already exist");
        }
    }

    @Override
    public RoomFacility addFacilityToRoom(Room room, RoomFacility roomFacility) {
        List<Room> rooms = new ArrayList<>(getFacility(roomFacility.getFacility()).getRoom());
        rooms.add(room);
        roomFacility.setRoom(rooms);
        return roomFacilityRepository.save(roomFacility);
    }

    @Override
    public RoomFacility removeFacilityFromRoom(Room room, RoomFacility roomFacility) {
        List<Room> rooms = new ArrayList<>(getFacility(roomFacility.getFacility()).getRoom());
        rooms.remove(room);
        roomFacility.setRoom(rooms);
        roomFacilityRepository.save(roomFacility);
        return roomFacility;
    }
}
