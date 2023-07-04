package com.aacdemy.moonlight.service;

import com.aacdemy.moonlight.dto.room.RoomFacilityRequestDto;
import com.aacdemy.moonlight.dto.room.RoomFacilityResponseDto;
import com.aacdemy.moonlight.entity.hotel.Room;
import com.aacdemy.moonlight.entity.hotel.RoomFacility;
import com.aacdemy.moonlight.entity.hotel.RoomType;
import com.aacdemy.moonlight.entity.hotel.RoomView;
import com.aacdemy.moonlight.exception.EntityNotFoundException;
import com.aacdemy.moonlight.repository.hotel.RoomRepository;
import com.aacdemy.moonlight.service.impl.RoomServiceImpl;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith({MockitoExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
public class RoomServiceImplTest {
    @Mock
    private RoomServiceImpl roomService;
    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomFacilityService roomFacilityService;

    @BeforeEach
    void setUp() {
        final AutoCloseable autoCloseable = MockitoAnnotations.openMocks(this);
        roomService = new RoomServiceImpl(roomRepository, roomFacilityService);
    }

    @Test
    void testFindById() {
        Room room = new Room();
        room.setId(1L);
        room.setRoomNumber(50);
        room.setPrice(BigDecimal.valueOf(200.00));
        room.setType(RoomType.STANDARD);
        room.setView(RoomView.SEA);
        room.setPeople(2);
        room.setFacilities(new ArrayList<>());

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        Room result = roomService.findById(1L);

        assertThat(result).isEqualTo(room);

    }

    @Test
    void testFindByRoomNumber() {
        Room room = new Room();
        room.setId(1L);
        room.setRoomNumber(20);
        room.setPrice(BigDecimal.valueOf(300.00));
        room.setType(RoomType.STANDARD);
        room.setView(RoomView.POOL);
        room.setPeople(2);
        room.setFacilities(new ArrayList<>());

        when(roomRepository.findByRoomNumber(20)).thenReturn(Optional.of(room));

        Room result = roomService.findByRoomNumber(20);

        assertThat(result.getRoomNumber()).isEqualTo(room.getRoomNumber());
        verify(roomRepository, times(1)).findByRoomNumber(20);

    }

    @Test
    void testFindByRoomType() {
        Room room = new Room();
        room.setId(1L);
        room.setRoomNumber(30);
        room.setPrice(BigDecimal.valueOf(250.00));
        room.setType(RoomType.STANDARD);
        room.setView(RoomView.GARDEN);
        room.setPeople(2);
        room.setFacilities(new ArrayList<>());

        when(roomRepository.findByType(RoomType.STANDARD)).thenReturn(List.of(room));

        List<Room> result = roomService.findByRoomType(RoomType.STANDARD);
        //Room roomForTest = result.stream()

        assertThat(result.get(0).getType()).isEqualTo(room.getType());
        verify(roomRepository, times(1)).findByType(RoomType.STANDARD);

    }

    @Test
    void testFindByRoomView() {
        Room room = new Room();
        room.setId(1L);
        room.setRoomNumber(35);
        room.setPrice(BigDecimal.valueOf(150.00));
        room.setType(RoomType.STANDARD);
        room.setView(RoomView.SEA);
        room.setPeople(2);
        room.setFacilities(new ArrayList<>());

        when(roomRepository.findByView(RoomView.SEA)).thenReturn(Collections.singletonList(room));

        List<Room> result = roomService.findByRoomView(RoomView.SEA);

        assertThat(result.get(0).getView()).isEqualTo(room.getView());
        verify(roomRepository, times(1)).findByView(RoomView.SEA);

    }

    @Test
    void testFindByPrice() {
        // Arrange
        BigDecimal price = BigDecimal.valueOf(200.00);
        List<Room> expectedRooms = Arrays.asList(new Room(), new Room());

        when(roomRepository.findByPrice(price)).thenReturn(expectedRooms);
        // Act
        List<Room> actualRooms = roomService.findByPrice(price);
        // Assert
        assertEquals(expectedRooms, actualRooms);
        verify(roomRepository, times(1)).findByPrice(price);
    }

    @Test
    void testFindByPeople() {
        int people = 2;
        List<Room> expectedRooms = Arrays.asList(new Room(), new Room());

        when(roomRepository.findByPeople(people)).thenReturn(expectedRooms);

        List<Room> actualRooms = roomService.findByPeople(people);

        assertEquals(expectedRooms, actualRooms);
        verify(roomRepository, times(1)).findByPeople(people);
    }

    @Test
    void testFindRoomsByFacility() {
        String text = "WiFi";
        List<Room> expectedRooms = Arrays.asList(new Room(), new Room());

        when(roomRepository.findByFacilitiesContaining(text)).thenReturn(expectedRooms);

        List<Room> actualRooms = roomService.findRoomsByFacility(text);

        assertEquals(expectedRooms, actualRooms);
        verify(roomRepository, times(1)).   findByFacilitiesContaining(text);
    }

    @Test
    public void testAddFacilityToRoom() {
        String facilityName = "WiFi";

        RoomFacilityRequestDto requestDto = RoomFacilityRequestDto.builder()
                .facility(facilityName)
                .build();

        Room room = Room.builder()
                .id(1L)
                .facilities(List.of(RoomFacility.builder()
                        .id(1L)
                        .facility("WiFi")
                        .build()))
                .build();

        RoomFacility facility = RoomFacility.builder()
                .id(1L)
                .facility("WiFi")
                .room(List.of(room))
                .build();

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(roomFacilityService.getFacility(facilityName)).thenReturn(facility);

        RoomFacilityResponseDto responseDto = roomService.addFacilityToRoom(1L, requestDto);

        assertTrue(responseDto.getFacilities().contains(facility));

        verify(roomRepository, times(1)).findById(1L);
        verify(roomFacilityService, atLeastOnce()).getFacility(facilityName);
    }

    @Test
    public void testAddFacilityToRoomAlreadyExist() {
        String facilityName = "WiFi";

        RoomFacilityRequestDto requestDto = RoomFacilityRequestDto.builder()
                .facility(facilityName)
                .build();

        RoomFacility facility = RoomFacility.builder()
                .id(1L)
                .facility("WiFi")
                .build();
        facility.setRoom(List.of(Room.builder()
                .id(1L)
                .facilities(List.of(facility))
                .build()));

        Room room = facility.getRoom().get(0);

        when(roomRepository.findById(1L)).thenReturn(Optional.ofNullable(room));
        when(roomFacilityService.getFacility(facilityName)).thenReturn(facility);
        assertTrue(room.getFacilities().contains(facility));
        assertThrows(ValidationException.class, () -> roomService.addFacilityToRoom(1L, requestDto));

        verify(roomRepository, times(1)).findById(1L);
        verify(roomFacilityService, atLeastOnce()).getFacility(facilityName);
    }

    @Test
    public void testAddFacility_FacilityDoesNotExist() {
        String searchedFacility = "NonExistingFacility";

        Room room = Room.builder()
                .id(1L)
                .facilities(List.of(RoomFacility.builder()
                        .id(1L)
                        .facility("WiFi")
                        .build()))
                .build();

        RoomFacilityRequestDto requestDto = RoomFacilityRequestDto.builder().facility(searchedFacility).build();

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(roomFacilityService.getFacility(searchedFacility)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> roomService.addFacilityToRoom(1L, requestDto));
    }

    @Test
    public void testDeleteFacility() {
        Long roomId = 1L;
        String facility = "WiFi";

        Room room = Room.builder().id(1L).facilities(new ArrayList<>()).build();

        room.getFacilities().listIterator().add(RoomFacility.builder()
                .facility(facility).room(List.of(room)).build());


        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(roomFacilityService.getFacility(facility)).thenReturn(room.getFacilities().listIterator().next());

        RoomFacilityRequestDto requestDto = RoomFacilityRequestDto.builder()
                .facility(facility)
                .build();

        RoomFacilityResponseDto responseDto = roomService.deleteFacilityFromRoom(roomId, requestDto);

        assertFalse(responseDto.getFacilities().contains(room.getFacilities().listIterator().next()));
        verify(roomRepository, times(1)).findById(roomId);
    }

    @Test
    public void testDeleteFacilityThrows() {
        Long roomId = 1L;
        String facility = "WiFi";

        Room room = Room.builder()
                .id(1L).facilities(new ArrayList<>()).build();
        room.getFacilities().add(RoomFacility.builder().facility("GSM").room(List.of(room)).build());

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(roomFacilityService.getFacility(facility)).thenThrow(EntityNotFoundException.class);

        RoomFacilityRequestDto requestDto = RoomFacilityRequestDto.builder()
                .facility(facility)
                .build();

        assertThrows(EntityNotFoundException.class, () -> roomService.deleteFacilityFromRoom(roomId, requestDto));
        verify(roomRepository, times(1)).findById(roomId);
    }

    @Test
    public void testDeleteFacilityFromRoomDoesNotExist() {
        String facilityName = "WiFi";

        RoomFacilityRequestDto requestDto = RoomFacilityRequestDto.builder()
                .facility(facilityName)
                .build();

        RoomFacility facility = RoomFacility.builder().id(2L).facility("WiFi").build();

        RoomFacility existingFacility = RoomFacility.builder()
                .id(1L)
                .facility("CableTV")
                .build();
        facility.setRoom(List.of(Room.builder()
                .id(1L)
                .facilities(List.of(existingFacility))
                .build()));

        Room room = facility.getRoom().get(0);

        when(roomRepository.findById(1L)).thenReturn(Optional.ofNullable(room));
        when(roomFacilityService.getFacility(facilityName)).thenReturn(facility);
        assertFalse(room.getFacilities().contains(facility));
        assertThrows(ValidationException.class, () -> roomService.deleteFacilityFromRoom(1L, requestDto));

        verify(roomRepository, times(1)).findById(1L);
        verify(roomFacilityService, atLeastOnce()).getFacility(facilityName);
    }
}


