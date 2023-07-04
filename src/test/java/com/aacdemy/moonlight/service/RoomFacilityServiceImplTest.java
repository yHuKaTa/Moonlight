package com.aacdemy.moonlight.service;

import com.aacdemy.moonlight.dto.room.RoomFacilityRequestDto;
import com.aacdemy.moonlight.dto.room.RoomFacilityUpdRequestDto;
import com.aacdemy.moonlight.entity.hotel.Room;
import com.aacdemy.moonlight.entity.hotel.RoomFacility;
import com.aacdemy.moonlight.exception.EntityNotFoundException;
import com.aacdemy.moonlight.repository.hotel.RoomFacilityRepository;
import com.aacdemy.moonlight.service.impl.RoomFacilityServiceImpl;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomFacilityServiceImplTest {
    @Mock
    RoomFacilityRepository roomFacilityRepository;
    @InjectMocks
    RoomFacilityServiceImpl roomFacilityServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddFacility() {
        RoomFacilityRequestDto requestDto = RoomFacilityRequestDto.builder().facility("WiFi").build();
        RoomFacility facility = RoomFacility.builder().facility("WiFi").room(List.of(Room.builder().build())).build();
        when(roomFacilityRepository.existsByFacility(requestDto.getFacility())).thenReturn(false);
        when(roomFacilityRepository.save(facility)).thenReturn(facility);
//
//        assertEquals(facility, roomFacilityServiceImpl.addFacility(requestDto));
//        verify(roomFacilityRepository, atLeastOnce()).existsByFacility(requestDto.getFacility());
    }

    @Test
    void testAddFacilityThrows() {
        when(roomFacilityRepository.existsByFacility(anyString())).thenReturn(true);

        assertThrows(ValidationException.class, () -> roomFacilityServiceImpl.addFacility(RoomFacilityRequestDto.builder().facility("WiFi").build()));
        verify(roomFacilityRepository, atLeastOnce()).existsByFacility("WiFi");
    }

    @Test
    void testGetFacility() {
        RoomFacility facility = RoomFacility.builder()
                .id(1L)
                .facility("WiFi")
                .room(List.of(Room.builder().build())).build();

        when(roomFacilityRepository.findByFacility(anyString())).thenReturn(Optional.of(facility));
        assertEquals(facility, roomFacilityServiceImpl.getFacility("facility"));
        verify(roomFacilityRepository, atLeastOnce()).findByFacility("facility");
    }

    @Test
    void testGetFacilityThrows() {
        RoomFacility facility = RoomFacility.builder()
                .id(1L)
                .facility("WiFi")
                .room(List.of(Room.builder().build())).build();

        when(roomFacilityRepository.findByFacility(anyString())).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> roomFacilityServiceImpl.getFacility("facility"));
    }

    @Test
    void testGetAllFacilities() {
        RoomFacility facility = RoomFacility.builder()
                .id(1L)
                .facility("WiFi")
                .room(List.of(Room.builder().build())).build();
        when(roomFacilityRepository.findAll()).thenReturn(List.of(facility));

        assertEquals(List.of(facility), roomFacilityServiceImpl.getAllFacilities());
    }

    @Test
    void testDeleteFacility() {
        RoomFacility facility = RoomFacility.builder()
                .id(1L)
                .facility("WiFi")
                .room(List.of(Room.builder().build())).build();
        when(roomFacilityRepository.findByFacility(anyString())).thenReturn(Optional.of(facility));

        roomFacilityServiceImpl.deleteFacility(RoomFacilityRequestDto.builder().facility("WiFi").build());

        assertFalse(roomFacilityRepository.existsByFacility("WiFi"));
    }

    @Test
    void testDeleteFacilityThrows() {
        RoomFacility facility = RoomFacility.builder()
                .id(1L)
                .facility("WiFi")
                .room(List.of(Room.builder().build())).build();
        when(roomFacilityRepository.findByFacility(anyString())).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> roomFacilityServiceImpl.deleteFacility(RoomFacilityRequestDto.builder().facility("WiFi").build()));
    }

    @Test
    void testUpdateFacility() {
        RoomFacility facility = RoomFacility.builder()
                .id(1L)
                .facility("WiFi")
                .room(List.of(Room.builder().build())).build();

        when(roomFacilityRepository.findByFacility(anyString())).thenReturn(Optional.of(facility));
        when(roomFacilityRepository.existsByFacility(anyString())).thenReturn(false);

        assertEquals(facility, roomFacilityServiceImpl.updateFacility(RoomFacilityUpdRequestDto.builder().currentFacility("WiFi").newFacility("CableTV").build()));
    }

    @Test
    void testUpdateFacilityThrows() {
        when(roomFacilityRepository.existsByFacility(anyString())).thenReturn(true);
        assertThrows(ValidationException.class, () -> roomFacilityServiceImpl.updateFacility(RoomFacilityUpdRequestDto.builder().currentFacility("WiFi").newFacility("CableTV").build()));
    }

    @Test
    void testAddFacilityToRoom() {
        RoomFacility facility = RoomFacility.builder()
                .id(1L)
                .facility("CableTV")
                .build();

        Room room = Room.builder()
                .id(1L)
                .facilities(List.of(facility))
                .build();
        facility.setRoom(List.of(room));

        when(roomFacilityRepository.findByFacility("CableTV")).thenReturn(Optional.of(facility));
        when(roomFacilityServiceImpl.addFacilityToRoom(room, facility)).thenReturn(facility);
        assertTrue(roomFacilityServiceImpl.addFacilityToRoom(room, facility).getRoom().listIterator().next().getFacilities().contains(facility)); // null pointer
    }

    @Test
    void testRemoveFacilityFromRoom() {
        RoomFacility facility = RoomFacility.builder()
                .id(1L)
                .facility("CableTV")
                .build();

        Room room = Room.builder()
                .id(1L)
                .facilities(List.of(facility))
                .build();
        facility.setRoom(List.of(room));

        when(roomFacilityRepository.findByFacility("CableTV")).thenReturn(Optional.of(facility));
        assertFalse(roomFacilityServiceImpl.removeFacilityFromRoom(room, facility).getRoom().contains(facility));
    }
}