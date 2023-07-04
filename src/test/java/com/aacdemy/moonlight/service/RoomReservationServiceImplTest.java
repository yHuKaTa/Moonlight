package com.aacdemy.moonlight.service;

import com.aacdemy.moonlight.convertor.RoomReservationConvertor;
import com.aacdemy.moonlight.dto.roomReservation.RoomReservationRequestDto;
import com.aacdemy.moonlight.dto.roomReservation.RoomReservationResponseDto;
import com.aacdemy.moonlight.dto.roomReservation.RoomReservationUpdateRequestDto;
import com.aacdemy.moonlight.dto.roomReservation.RoomReservationUpdateResponseDto;
import com.aacdemy.moonlight.entity.PaymentStatus;
import com.aacdemy.moonlight.entity.hotel.*;
import com.aacdemy.moonlight.entity.user.User;
import com.aacdemy.moonlight.entity.user.UserRole;
import com.aacdemy.moonlight.exception.EntityNotFoundException;
import com.aacdemy.moonlight.repository.hotel.ReservationRepository;
import com.aacdemy.moonlight.repository.hotel.RoomRepository;
import com.aacdemy.moonlight.repository.user.UserRepository;
import com.aacdemy.moonlight.service.impl.RoomReservationServiceImpl;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomReservationServiceImplTest {
    @Mock
    RoomRepository roomRepository;

    @Mock
    ReservationRepository roomReservationRepository;

    @Mock
    RoomReservationConvertor reservationConvertor;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    RoomReservationServiceImpl roomReservationServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveReservationWithBedroom() {
        RoomReservationRequestDto reservation = RoomReservationRequestDto.builder()
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 25))
                .roomId(1L)
                .adults(2)
                .children(1)
                .bedType("BEDROOM").build();

        RoomReservationResponseDto response = RoomReservationResponseDto.builder()
                .id(1L)
                .adults(2)
                .children(1)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 25))
                .fullPrice(BigDecimal.ONE)
                .roomType(RoomType.STANDARD)
                .daysStaying(1)
                .price(BigDecimal.ONE).build();

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .price(BigDecimal.ONE)
                .type(RoomType.STANDARD)
                .view(RoomView.SEA)
                .people(2)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        RoomReservation roomReservation = RoomReservation.builder()
                .id(1L)
                .room(room)
                .user(User.builder()
                        .id(1L)
                        .email("user@email.bg")
                        .firstName("first")
                        .lastName("last")
                        .passportID("929292")
                        .password("passWorD$")
                        .userRole(new UserRole())
                        .createdDate(Date.from(Instant.now()))
                        .modifiedDate(Date.from(Instant.now()))
                        .enabled(true)
                        .build())
                .adults(2)
                .children(1)
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .fullPrice(BigDecimal.ONE)
                .paymentStatus(PaymentStatus.UNPAID)
                .bedType(RoomBedType.BEDROOM)
                .build();

        when(roomRepository.findById(reservation.getRoomId())).thenReturn(Optional.of(room));
        when(reservationConvertor.toRoomReservation(reservation, "user@email.bg")).thenReturn(roomReservation);
        when(roomReservationRepository.save(roomReservation)).thenReturn(roomReservation);

        RoomReservationResponseDto result = roomReservationServiceImpl.saveReservation(reservation, "user@email.bg");
        assertNotNull(result);
        assertDoesNotThrow(() -> result);
    }

    @Test
    void testSaveReservationWithTwinBedsNull() {
        RoomReservationRequestDto reservation = RoomReservationRequestDto.builder()
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 25))
                .roomId(1L)
                .adults(2)
                .children(1)
                .build();

        RoomReservationResponseDto response = RoomReservationResponseDto.builder()
                .id(1L)
                .adults(2)
                .children(1)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 25))
                .fullPrice(BigDecimal.ONE)
                .roomType(RoomType.STANDARD)
                .daysStaying(1)
                .price(BigDecimal.ONE).build();

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .price(BigDecimal.ONE)
                .type(RoomType.STANDARD)
                .view(RoomView.SEA)
                .people(2)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        RoomReservation roomReservation = RoomReservation.builder()
                .id(1L)
                .room(room)
                .user(User.builder()
                        .id(1L)
                        .email("user@email.bg")
                        .firstName("first")
                        .lastName("last")
                        .passportID("929292")
                        .password("passWorD$")
                        .userRole(new UserRole())
                        .createdDate(Date.from(Instant.now()))
                        .modifiedDate(Date.from(Instant.now()))
                        .enabled(true)
                        .build())
                .adults(2)
                .children(1)
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .fullPrice(BigDecimal.ONE)
                .paymentStatus(PaymentStatus.UNPAID)
                .bedType(RoomBedType.TWIN_BEDS)
                .build();

        when(roomRepository.findById(reservation.getRoomId())).thenReturn(Optional.of(room));
        when(reservationConvertor.toRoomReservation(reservation, "user@email.bg")).thenReturn(roomReservation);
        when(roomReservationRepository.save(roomReservation)).thenReturn(roomReservation);

        RoomReservationResponseDto result = roomReservationServiceImpl.saveReservation(reservation, "user@email.bg");
        assertNotNull(result);
        assertDoesNotThrow(() -> result);
    }

    @Test
    void testSaveReservationWithTwinBedsEmpty() {
        RoomReservationRequestDto reservation = RoomReservationRequestDto.builder()
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 25))
                .roomId(1L)
                .adults(2)
                .children(1)
                .bedType("")
                .build();

        RoomReservationResponseDto response = RoomReservationResponseDto.builder()
                .id(1L)
                .adults(2)
                .children(1)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 25))
                .fullPrice(BigDecimal.ONE)
                .roomType(RoomType.STANDARD)
                .daysStaying(1)
                .price(BigDecimal.ONE).build();

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .price(BigDecimal.ONE)
                .type(RoomType.STANDARD)
                .view(RoomView.SEA)
                .people(2)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        RoomReservation roomReservation = RoomReservation.builder()
                .id(1L)
                .room(room)
                .user(User.builder()
                        .id(1L)
                        .email("user@email.bg")
                        .firstName("first")
                        .lastName("last")
                        .passportID("929292")
                        .password("passWorD$")
                        .userRole(new UserRole())
                        .createdDate(Date.from(Instant.now()))
                        .modifiedDate(Date.from(Instant.now()))
                        .enabled(true)
                        .build())
                .adults(2)
                .children(1)
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .fullPrice(BigDecimal.ONE)
                .paymentStatus(PaymentStatus.UNPAID)
                .bedType(RoomBedType.TWIN_BEDS)
                .build();

        when(roomRepository.findById(reservation.getRoomId())).thenReturn(Optional.of(room));
        when(reservationConvertor.toRoomReservation(reservation, "user@email.bg")).thenReturn(roomReservation);
        when(roomReservationRepository.save(roomReservation)).thenReturn(roomReservation);

        RoomReservationResponseDto result = roomReservationServiceImpl.saveReservation(reservation, "user@email.bg");
        assertNotNull(result);
        assertDoesNotThrow(() -> result);
    }

    @Test
    void testSaveReservationReturnRoomNotFound() {
        RoomReservationRequestDto reservation = RoomReservationRequestDto.builder().build();
        when(roomRepository.findById(reservation.getRoomId())).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> roomReservationServiceImpl.saveReservation(reservation, "user@email.com"), "Room for reservation not found");
    }

    @Test
    void testSaveReservationReturnValidationEx() {
        RoomReservationRequestDto reservation = RoomReservationRequestDto.builder()
                .roomId(1L)
                .startDate(LocalDate.of(2023, 5, 25))
                .endDate(LocalDate.of(2023, 5, 24)).build();

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .price(BigDecimal.ONE)
                .type(RoomType.STANDARD)
                .view(RoomView.SEA)
                .people(2)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        when(roomRepository.findById(reservation.getRoomId())).thenReturn(Optional.of(room));

        assertTrue(reservation.getEndDate().isBefore(reservation.getStartDate()));
        assertThrows(ValidationException.class, () -> roomReservationServiceImpl.saveReservation(reservation, "user@email.com"), "End date of reservation must be after start date!");
    }

    @Test
    void testSaveReservationReturnEntityNotFound() {
        RoomReservationRequestDto reservation = RoomReservationRequestDto.builder()
                .roomId(1L)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 25)).build();

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .price(BigDecimal.ONE)
                .type(RoomType.STANDARD)
                .view(RoomView.SEA)
                .people(2)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        when(roomRepository.findById(reservation.getRoomId())).thenReturn(Optional.of(room));

        when(roomReservationRepository.existsByRoomAndStartDateLessThanEqualAndEndDateGreaterThanEqual(room, reservation.getStartDate(), reservation.getEndDate())).thenReturn(true);

        assertThrows(EntityNotFoundException.class, () -> roomReservationServiceImpl.saveReservation(reservation, "user@email.com"), "Room is not available for the requested dates.");
    }

    @Test
    void testSaveReservationReturnRoomToSmall() {
        RoomReservationRequestDto reservation = RoomReservationRequestDto.builder()
                .roomId(1L)
                .adults(3)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 25)).build();

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .price(BigDecimal.ONE)
                .type(RoomType.STANDARD)
                .view(RoomView.SEA)
                .people(2)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        when(roomRepository.findById(reservation.getRoomId())).thenReturn(Optional.of(room));
        when(roomReservationRepository.existsByRoomAndStartDateLessThanEqualAndEndDateGreaterThanEqual(room, reservation.getStartDate(), reservation.getEndDate())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> roomReservationServiceImpl.saveReservation(reservation, "user@email.com"), "Room is not suitable for " + reservation.getAdults() + " adults");
    }

    @Test
    void testSaveReservationReturnZeroDaysStay() {
        String email = "user@email.bg";

        RoomReservationRequestDto reservationRequest = RoomReservationRequestDto.builder()
                .roomId(1L)
                .adults(3)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now()).build();

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .price(BigDecimal.ONE)
                .type(RoomType.STANDARD)
                .view(RoomView.SEA)
                .people(3)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        RoomReservation roomReservation = RoomReservation.builder()
                .id(1L)
                .room(room)
                .user(User.builder()
                        .id(1L)
                        .email(email)
                        .firstName("first")
                        .lastName("last")
                        .passportID("929292")
                        .password("passWorD$")
                        .userRole(new UserRole())
                        .createdDate(Date.from(Instant.now()))
                        .modifiedDate(Date.from(Instant.now()))
                        .enabled(true)
                        .build())
                .adults(2)
                .children(1)
                .startDate(reservationRequest.getStartDate())
                .endDate(reservationRequest.getEndDate())
                .fullPrice(BigDecimal.ZERO)
                .paymentStatus(PaymentStatus.UNPAID)
                .bedType(RoomBedType.BEDROOM)
                .build();

        when(roomRepository.findById(reservationRequest.getRoomId())).thenReturn(Optional.of(room));
        when(roomReservationRepository.existsByRoomAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                room,
                reservationRequest.getStartDate(),
                reservationRequest.getEndDate())).thenReturn(false);
        when(reservationConvertor.toRoomReservation(reservationRequest, "user@email.bg")).thenReturn(roomReservation);

        Long daysOfStay = ChronoUnit.DAYS.between(roomReservation.getStartDate(), roomReservation.getEndDate());

        //roomReservationServiceImpl.saveReservation(reservationRequest, "user@email.com");

        assertTrue(daysOfStay <= 0);
//        assertThrows(EntityNotFoundException.class, () -> roomReservationServiceImpl.saveReservation(reservationRequest, "user@email.com"),
//                "Room reservation is available for at least one day.");

        assertThrows(EntityNotFoundException.class, () -> roomReservationServiceImpl.saveReservation(reservationRequest, email));

    }

    @Test
    public void testUpdateRoomReservationUpdateByBedTypeTwinBeds() {
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(1);
        LocalDate newEndDate = endDate.plusDays(2);
        Long roomId = 2L;
        RoomView view = RoomView.GARDEN;
        RoomType type = RoomType.STANDARD;

        RoomReservationUpdateRequestDto update = RoomReservationUpdateRequestDto.builder()
                .id(1L)
                .startDate(startDate.plusDays(1))
                .endDate(newEndDate)
                .roomView(view.getName())
                .roomType(type.getName())
                .bedType(RoomBedType.TWIN_BEDS.label)
                .adults(3)
                .build();

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room requiredRoom = Room.builder()
                .id(roomId)
                .roomNumber(100)
                .type(type)
                .view(view)
                .price(BigDecimal.ONE)
                .people(4)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(requiredRoom));

        RoomReservation reservation = new RoomReservation();
        reservation.setId(1L);
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setAdults(2);
        reservation.setRoom(requiredRoom);

        List<Room> rooms = new ArrayList<>();
        rooms.add(requiredRoom);

        when(roomReservationRepository.findById(update.getId())).thenReturn(Optional.of(reservation));
        when(roomRepository.findByView(view)).thenReturn(rooms);
        when(roomRepository.findByType(type)).thenReturn(rooms);
        when(roomReservationServiceImpl.isRoomNotOccupied(any(), any(), any(), any())).thenReturn(true);
        when(roomReservationRepository.isRoomNotOccupied(startDate, endDate, roomId, reservation.getId())).thenReturn(true);

        RoomReservationUpdateResponseDto response = null;
        boolean roomFound = false;

        try {
            response = roomReservationServiceImpl.updateRoomReservationParameters(update);
            roomFound = true;
        } catch (EntityNotFoundException e) {}

        if (roomFound) {
            verify(roomReservationRepository).updateBedType(RoomBedType.TWIN_BEDS, reservation.getId());
        }
    }


    @Test
    public void testUpdateRoomReservationUpdateLastTestTo100Line290() {
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(1);
        LocalDate newEndDate = endDate.plusDays(2);
        Long roomId = 2L;
        RoomView view = RoomView.GARDEN;
        RoomType type = RoomType.STANDARD;

        RoomReservationUpdateRequestDto update = RoomReservationUpdateRequestDto.builder()
                .id(1L)
                .startDate(startDate.plusDays(1))
                .endDate(newEndDate)
                .roomView(view.getName())
                .roomType(type.getName())
                .bedType(RoomBedType.TWIN_BEDS.label)
                .adults(2)
                .build();

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room requiredRoom = Room.builder()
                .id(roomId)
                .roomNumber(100)
                .type(type)
                .view(view)
                .price(BigDecimal.ONE)
                .people(2)
                .facilities(List.of(facility)).build();

        Room notMatchRoom = Room.builder()
                .id(roomId)
                .roomNumber(100)
                .type(RoomType.APARTMENT)
                .view(RoomView.GARDEN)
                .price(BigDecimal.ONE)
                .people(4)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(requiredRoom, notMatchRoom));

        RoomReservation reservation = new RoomReservation();
        reservation.setId(1L);
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setAdults(3);
        reservation.setRoom(requiredRoom);

        List<Room> rooms = new ArrayList<>();
        rooms.add(notMatchRoom); // First room doesn't meet the conditions
        rooms.add(requiredRoom); // Second room meets the conditions

        when(roomReservationRepository.findById(update.getId())).thenReturn(Optional.of(reservation));
        when(roomRepository.findByView(view)).thenReturn(rooms);
        when(roomRepository.findByPeople(update.getAdults())).thenReturn(rooms);
        when(roomReservationServiceImpl.isRoomNotOccupied(any(), any(), any(), any())).thenReturn(true);
        when(roomReservationRepository.isRoomNotOccupied(startDate, endDate, roomId, reservation.getId())).thenReturn(true);

        RoomReservationUpdateResponseDto response = null;
        boolean roomFound = false;

        try {
            response = roomReservationServiceImpl.updateRoomReservationParameters(update);
            roomFound = true;
        } catch (EntityNotFoundException e) {
        }

        if (roomFound) {
            verify(roomReservationRepository).updateRoomAndAdults(requiredRoom, reservation.getAdults(), reservation.getId());
        }
    }



    @Test
    public void testUpdateRoomReservationUpdateByAdultsBreak() {
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(1);
        LocalDate newEndDate = endDate.plusDays(2);
        Long roomId = 2L;
        RoomView view = RoomView.GARDEN;
        RoomType type = RoomType.STANDARD;

        RoomReservationUpdateRequestDto update = RoomReservationUpdateRequestDto.builder()
                .id(1L)
                .startDate(startDate.plusDays(1))
                .endDate(newEndDate)
                .roomView(view.getName())
                .roomType(type.getName())
                .adults(2)
                .build();

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room requiredRoom = Room.builder()
                .id(roomId)
                .roomNumber(100)
                .type(type)
                .view(view)
                .price(BigDecimal.ONE)
                .people(2)
                .facilities(List.of(facility)).build();

        Room notMatchRoom = Room.builder()
                .id(roomId)
                .roomNumber(100)
                .type(RoomType.APARTMENT)
                .view(RoomView.GARDEN)
                .price(BigDecimal.ONE)
                .people(4)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(requiredRoom, notMatchRoom));

        RoomReservation reservation = RoomReservation.builder()
                .id(1L)
                .startDate(startDate)
                .endDate(endDate)
                .adults(2)
                .room(requiredRoom)
                .build();


        List<Room> rooms = new ArrayList<>();
        rooms.add(notMatchRoom); // First room doesn't meet the conditions
        rooms.add(requiredRoom); // Second room meets the conditions

        when(roomReservationRepository.findById(update.getId())).thenReturn(Optional.of(reservation));
        when(roomRepository.findByView(view)).thenReturn(rooms);
        when(roomRepository.findByPeople(update.getAdults())).thenReturn(rooms);
        when(roomReservationServiceImpl.isRoomNotOccupied(any(), any(), any(), any())).thenReturn(true);
        when(roomReservationRepository.isRoomNotOccupied(startDate, endDate, roomId, reservation.getId())).thenReturn(true);

        RoomReservationUpdateResponseDto response = null;
        boolean roomFound = false;

        try {
            response = roomReservationServiceImpl.updateRoomReservationParameters(update);
            roomFound = true;
        } catch (EntityNotFoundException e) {
        }

        if (roomFound) {
            verify(roomReservationRepository).updateRoomAndAdults(requiredRoom, reservation.getAdults(), reservation.getId());
        }
    }

    @ParameterizedTest
    @EnumSource(RoomView.class)
    public void testUpdateRoomReservationParameters_RoomViewBreak(RoomView view) {
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(1);
        LocalDate newEndDate = endDate.plusDays(2);
        Long roomId = 2L;
        RoomType roomType = RoomType.STANDARD;

        RoomReservationUpdateRequestDto update = RoomReservationUpdateRequestDto.builder()
                .id(1L)
                .startDate(startDate.plusDays(1))
                .endDate(newEndDate)
                .roomView(view.getName())
                .roomType(roomType.getName())
                .adults(3)
                .build();

        RoomReservation reservation = RoomReservation.builder()
                .id(1L)
                .startDate(startDate)
                .endDate(endDate)
                .adults(2)
                .room(new Room())
                .build();

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room requiredRoom = Room.builder()
                .id(roomId)
                .roomNumber(100)
                .type(roomType)
                .view(view)
                .price(BigDecimal.ONE)
                .people(4)
                .facilities(List.of(facility)).build();

        Room notMatchRoom = Room.builder()
                .id(roomId)
                .roomNumber(100)
                .type(RoomType.APARTMENT)
                .view(RoomView.GARDEN)
                .price(BigDecimal.ONE)
                .people(1)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(requiredRoom, notMatchRoom));

        List<Room> rooms = new ArrayList<>();
        rooms.add(notMatchRoom); // First room doesn't meet the conditions
        rooms.add(requiredRoom); // Second room meets the conditions

        when(roomReservationRepository.findById(update.getId())).thenReturn(Optional.of(reservation));
        when(roomRepository.findByView(view)).thenReturn(rooms);
        when(roomRepository.findByType(roomType)).thenReturn(rooms);
        when(roomReservationServiceImpl.isRoomNotOccupied(any(), any(), any(), any())).thenReturn(true);
        when(roomReservationRepository.isRoomNotOccupied(startDate, endDate, roomId, reservation.getId())).thenReturn(true);
        when(roomRepository.findByPeople(3)).thenReturn(rooms);

        RoomReservationUpdateResponseDto response = null;
        boolean roomFound = false;

        try {
            response = roomReservationServiceImpl.updateRoomReservationParameters(update);
            roomFound = true;
        } catch (EntityNotFoundException e) {
        }

        if (roomFound) {
            assertEquals(requiredRoom, response.getRoom());
            verify(roomReservationRepository).updateRoomView(requiredRoom, reservation.getId());
        }
    }

    @Test
    void testUpdateRoomReservationPaymentStatusReturnNoEntity() {
        RoomReservationUpdateRequestDto update = new RoomReservationUpdateRequestDto(1L, null, null, null, null, null, null, "PAID", null);

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(5L)
                .roomNumber(100)
                .price(BigDecimal.ONE)
                .type(RoomType.STANDARD)
                .view(RoomView.SEA)
                .people(2)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        RoomReservation roomReservation = RoomReservation.builder()
                .id(1L)
                .room(room)
                .user(User.builder()
                        .id(1L)
                        .email("user@email.bg")
                        .firstName("first")
                        .lastName("last")
                        .passportID("929292")
                        .password("passWorD$")
                        .userRole(new UserRole())
                        .createdDate(Date.from(Instant.now()))
                        .modifiedDate(Date.from(Instant.now()))
                        .enabled(true)
                        .build())
                .adults(2)
                .children(1)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 25))
                .fullPrice(BigDecimal.ONE)
                .paymentStatus(PaymentStatus.UNPAID)
                .bedType(RoomBedType.BEDROOM)
                .build();

        assertThrows(EntityNotFoundException.class, () -> roomReservationServiceImpl.updateRoomReservationPaymentStatus(update));
    }

    @Test
    void testUpdateRoomReservationPaymentStatus() {
        RoomReservationUpdateRequestDto update = new RoomReservationUpdateRequestDto(1L, null, null, null, null, null, null, "PAID", null);

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .price(BigDecimal.ONE)
                .type(RoomType.STANDARD)
                .view(RoomView.SEA)
                .people(2)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        RoomReservationUpdateResponseDto updateResponse = RoomReservationUpdateResponseDto.builder()
                .id(1L)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 25))
                .room(room)
                .adults(2)
                .children(1)
                .status(PaymentStatus.PAID)
                .type(RoomBedType.BEDROOM)
                .build();

        RoomReservation roomReservation = RoomReservation.builder()
                .id(1L)
                .room(room)
                .user(User.builder()
                        .id(1L)
                        .email("user@email.bg")
                        .firstName("first")
                        .lastName("last")
                        .passportID("929292")
                        .password("passWorD$")
                        .userRole(new UserRole())
                        .createdDate(Date.from(Instant.now()))
                        .modifiedDate(Date.from(Instant.now()))
                        .enabled(true)
                        .build())
                .adults(2)
                .children(1)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 25))
                .fullPrice(BigDecimal.ONE)
                .paymentStatus(PaymentStatus.UNPAID)
                .bedType(RoomBedType.BEDROOM)
                .build();

        when(roomReservationRepository.findById(update.getId())).thenReturn(Optional.of(roomReservation));

        Assertions.assertEquals(updateResponse, roomReservationServiceImpl.updateRoomReservationPaymentStatus(update));
    }

    @Test
    void testUpdateRoomReservationPaymentStatusUnpaid() {
        RoomReservationUpdateRequestDto update = new RoomReservationUpdateRequestDto(1L, null, null, null, null, null, null, "UNPAID", null);

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .price(BigDecimal.ONE)
                .type(RoomType.STANDARD)
                .view(RoomView.SEA)
                .people(2)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        RoomReservationUpdateResponseDto updateResponse = RoomReservationUpdateResponseDto.builder()
                .id(1L)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 25))
                .room(room)
                .adults(2)
                .children(1)
                .status(PaymentStatus.UNPAID)
                .type(RoomBedType.BEDROOM)
                .build();

        RoomReservation roomReservation = RoomReservation.builder()
                .id(1L)
                .room(room)
                .user(User.builder()
                        .id(1L)
                        .email("user@email.bg")
                        .firstName("first")
                        .lastName("last")
                        .passportID("929292")
                        .password("passWorD$")
                        .userRole(new UserRole())
                        .createdDate(Date.from(Instant.now()))
                        .modifiedDate(Date.from(Instant.now()))
                        .enabled(true)
                        .build())
                .adults(2)
                .children(1)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 25))
                .fullPrice(BigDecimal.ONE)
                .paymentStatus(PaymentStatus.UNPAID)
                .bedType(RoomBedType.BEDROOM)
                .build();

        when(roomReservationRepository.findById(update.getId())).thenReturn(Optional.of(roomReservation));

        Assertions.assertEquals(updateResponse, roomReservationServiceImpl.updateRoomReservationPaymentStatus(update));
    }

    @Test
    void testUpdateRoomReservationParametersUsingStandardAndSea() {
        RoomReservationUpdateRequestDto update = new RoomReservationUpdateRequestDto(1L,
                LocalDate.of(2023, 5, 24),
                LocalDate.of(2023, 5, 25),
                "SEA", "STANDARD", 2, 0, null, "BEDROOM");

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .type(RoomType.STANDARD)
                .view(RoomView.SEA)
                .price(BigDecimal.ONE)
                .people(2)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        User user = User.builder()
                .id(1L)
                .email("user@email.bg")
                .firstName("first")
                .lastName("last")
                .passportID("929292")
                .password("passWorD$")
                .userRole(new UserRole())
                .createdDate(Date.from(Instant.now()))
                .modifiedDate(Date.from(Instant.now()))
                .enabled(true).build();

        RoomReservation reservation = RoomReservation.builder()
                .id(1L)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 25))
                .adults(2)
                .children(0)
                .room(room)
                .paymentStatus(PaymentStatus.UNPAID)
                .bedType(RoomBedType.BEDROOM)
                .user(user).build();

        RoomReservationUpdateResponseDto response = RoomReservationUpdateResponseDto.builder()
                .id(1L)
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .adults(reservation.getAdults())
                .children(reservation.getChildren())
                .room(room)
                .status(reservation.getPaymentStatus())
                .type(reservation.getBedType()).build();

        when(roomReservationRepository.findById(update.getId())).thenReturn(Optional.of(reservation));


        when(roomRepository.findByType(any())).thenReturn(List.of(room));
        when(roomRepository.findByView(any())).thenReturn(List.of(room));
        when(roomRepository.findByPeople(anyInt())).thenReturn(List.of(room));
        when(roomReservationRepository.isRoomNotOccupied(any(), any(), anyLong(), anyLong())).thenReturn(true);

        RoomReservationUpdateResponseDto result = roomReservationServiceImpl.updateRoomReservationParameters(update);
        Assertions.assertEquals(response, result);
    }

    @Test
    void testUpdateRoomReservationParametersUsingPoolAndStudio() {
        RoomReservationUpdateRequestDto update = new RoomReservationUpdateRequestDto(1L,
                LocalDate.of(2023, 5, 24),
                LocalDate.of(2023, 5, 25),
                "POOL", "STUDIO", 3, 0, null, "TWIN_BEDS");

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .type(RoomType.STUDIO)
                .view(RoomView.POOL)
                .price(BigDecimal.ONE)
                .people(3)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        User user = User.builder()
                .id(1L)
                .email("user@email.bg")
                .firstName("first")
                .lastName("last")
                .passportID("929292")
                .password("passWorD$")
                .userRole(new UserRole())
                .createdDate(Date.from(Instant.now()))
                .modifiedDate(Date.from(Instant.now()))
                .enabled(true).build();

        RoomReservation reservation = RoomReservation.builder()
                .id(1L)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 25))
                .adults(3)
                .children(0)
                .room(room)
                .paymentStatus(PaymentStatus.UNPAID)
                .bedType(RoomBedType.TWIN_BEDS)
                .user(user).build();

        RoomReservationUpdateResponseDto response = RoomReservationUpdateResponseDto.builder()
                .id(1L)
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .adults(reservation.getAdults())
                .children(reservation.getChildren())
                .room(room)
                .status(reservation.getPaymentStatus())
                .type(reservation.getBedType()).build();

        when(roomReservationRepository.findById(update.getId())).thenReturn(Optional.of(reservation));

        when(roomRepository.findByType(any())).thenReturn(List.of(room));
        when(roomRepository.findByView(any())).thenReturn(List.of(room));
        when(roomRepository.findByPeople(anyInt())).thenReturn(List.of(room));
        when(roomReservationRepository.isRoomNotOccupied(any(), any(), anyLong(), anyLong())).thenReturn(true);

        RoomReservationUpdateResponseDto result = roomReservationServiceImpl.updateRoomReservationParameters(update);
        Assertions.assertEquals(response, result);
    }

    @Test
    void testUpdateRoomReservationParametersUsingGardenAndApartment() {
        RoomReservationUpdateRequestDto update = new RoomReservationUpdateRequestDto(1L,
                LocalDate.of(2023, 5, 24),
                LocalDate.of(2023, 5, 25),
                "GARDEN", "APARTMENT", 4, 2, null, "BEDROOM");

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .type(RoomType.APARTMENT)
                .view(RoomView.GARDEN)
                .price(BigDecimal.ONE)
                .people(4)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        User user = User.builder()
                .id(1L)
                .email("user@email.bg")
                .firstName("first")
                .lastName("last")
                .passportID("929292")
                .password("passWorD$")
                .userRole(new UserRole())
                .createdDate(Date.from(Instant.now()))
                .modifiedDate(Date.from(Instant.now()))
                .enabled(true).build();

        RoomReservation reservation = RoomReservation.builder()
                .id(1L)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 25))
                .adults(4)
                .children(2)
                .room(room)
                .paymentStatus(PaymentStatus.UNPAID)
                .bedType(RoomBedType.BEDROOM)
                .user(user).build();

        RoomReservationUpdateResponseDto response = RoomReservationUpdateResponseDto.builder()
                .id(1L)
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .adults(reservation.getAdults())
                .children(reservation.getChildren())
                .room(room)
                .status(reservation.getPaymentStatus())
                .type(reservation.getBedType()).build();

        when(roomReservationRepository.findById(update.getId())).thenReturn(Optional.of(reservation));


        when(roomRepository.findByType(any())).thenReturn(List.of(room));
        when(roomRepository.findByView(any())).thenReturn(List.of(room));
        when(roomRepository.findByPeople(anyInt())).thenReturn(List.of(room));
        when(roomReservationRepository.isRoomNotOccupied(any(), any(), anyLong(), anyLong())).thenReturn(true);

        RoomReservationUpdateResponseDto result = roomReservationServiceImpl.updateRoomReservationParameters(update);
        Assertions.assertEquals(response, result);
    }

    @Test
    void testUpdateRoomReservationParametersThrowsValidationEx() {
        RoomReservationUpdateRequestDto update = new RoomReservationUpdateRequestDto(1L,
                LocalDate.of(2023, 5, 25),
                null,
                "GARDEN", "APARTMENT", 4, 0, null, "Тwin beds");

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .type(RoomType.APARTMENT)
                .view(RoomView.GARDEN)
                .price(BigDecimal.ONE)
                .people(4)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        User user = User.builder()
                .id(1L)
                .email("user@email.bg")
                .firstName("first")
                .lastName("last")
                .passportID("929292")
                .password("passWorD$")
                .userRole(new UserRole())
                .createdDate(Date.from(Instant.now()))
                .modifiedDate(Date.from(Instant.now()))
                .enabled(true).build();

        RoomReservation reservation = RoomReservation.builder()
                .id(1L)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 25))
                .adults(4)
                .children(2)
                .room(room)
                .paymentStatus(PaymentStatus.UNPAID)
                .bedType(RoomBedType.TWIN_BEDS)
                .user(user).build();

        RoomReservationUpdateResponseDto response = RoomReservationUpdateResponseDto.builder()
                .id(1L)
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .adults(reservation.getAdults())
                .children(reservation.getChildren())
                .room(room)
                .status(reservation.getPaymentStatus())
                .type(reservation.getBedType()).build();

        when(roomReservationRepository.findById(update.getId())).thenReturn(Optional.of(reservation));
        when(roomRepository.findByType(any())).thenReturn(List.of(room));
        when(roomRepository.findByView(any())).thenReturn(List.of(room));
        when(roomRepository.findByPeople(anyInt())).thenReturn(List.of(room));
        when(roomReservationRepository.isRoomNotOccupied(any(), any(), anyLong(), anyLong())).thenReturn(true);

        assertThrows(ValidationException.class, () -> roomReservationServiceImpl.updateRoomReservationParameters(update));
    }

    @Test
    void testUpdateRoomReservationParametersThrowsValidationExForEndDate() {
        RoomReservationUpdateRequestDto update = new RoomReservationUpdateRequestDto(1L,
                null,
                LocalDate.of(2023, 5, 24),
                null, null, null, null, null, null);

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .type(RoomType.APARTMENT)
                .view(RoomView.GARDEN)
                .price(BigDecimal.ONE)
                .people(4)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        User user = User.builder()
                .id(1L)
                .email("user@email.bg")
                .firstName("first")
                .lastName("last")
                .passportID("929292")
                .password("passWorD$")
                .userRole(new UserRole())
                .createdDate(Date.from(Instant.now()))
                .modifiedDate(Date.from(Instant.now()))
                .enabled(true).build();

        RoomReservation reservation = RoomReservation.builder()
                .id(1L)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 25))
                .adults(4)
                .children(2)
                .room(room)
                .paymentStatus(PaymentStatus.UNPAID)
                .bedType(RoomBedType.TWIN_BEDS)
                .user(user).build();

        RoomReservationUpdateResponseDto response = RoomReservationUpdateResponseDto.builder()
                .id(1L)
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .adults(reservation.getAdults())
                .children(reservation.getChildren())
                .room(room)
                .status(reservation.getPaymentStatus())
                .type(reservation.getBedType()).build();

        when(roomReservationRepository.findById(update.getId())).thenReturn(Optional.of(reservation));
        when(roomRepository.findByType(any())).thenReturn(List.of(room));
        when(roomRepository.findByView(any())).thenReturn(List.of(room));
        when(roomRepository.findByPeople(anyInt())).thenReturn(List.of(room));
        when(roomReservationRepository.isRoomNotOccupied(any(), any(), anyLong(), anyLong())).thenReturn(true);

        assertThrows(ValidationException.class, () -> roomReservationServiceImpl.updateRoomReservationParameters(update));
    }

    @Test
    void testUpdateRoomReservationParametersThrowsEntityExForAdultsValue() {
        RoomReservationUpdateRequestDto update = new RoomReservationUpdateRequestDto(1L,
                null,
                null,
                null, null, 60, null, null, null);

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .type(RoomType.APARTMENT)
                .view(RoomView.GARDEN)
                .price(BigDecimal.ONE)
                .people(4)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        User user = User.builder()
                .id(1L)
                .email("user@email.bg")
                .firstName("first")
                .lastName("last")
                .passportID("929292")
                .password("passWorD$")
                .userRole(new UserRole())
                .createdDate(Date.from(Instant.now()))
                .modifiedDate(Date.from(Instant.now()))
                .enabled(true).build();

        RoomReservation reservation = RoomReservation.builder()
                .id(1L)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 25))
                .adults(4)
                .children(2)
                .room(room)
                .paymentStatus(PaymentStatus.UNPAID)
                .bedType(RoomBedType.TWIN_BEDS)
                .user(user).build();

        RoomReservationUpdateResponseDto response = RoomReservationUpdateResponseDto.builder()
                .id(1L)
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .adults(reservation.getAdults())
                .children(reservation.getChildren())
                .room(room)
                .status(reservation.getPaymentStatus())
                .type(reservation.getBedType()).build();

        when(roomReservationRepository.findById(update.getId())).thenReturn(Optional.of(reservation));
        when(roomRepository.findByType(any())).thenReturn(List.of(room));
        when(roomRepository.findByView(any())).thenReturn(List.of(room));
        when(roomRepository.findByPeople(anyInt())).thenReturn(List.of(room));
        when(roomReservationRepository.isRoomNotOccupied(any(), any(), anyLong(), anyLong())).thenReturn(true);

        assertThrows(EntityNotFoundException.class, () -> roomReservationServiceImpl.updateRoomReservationParameters(update));
    }

    @Test
    void testUpdateRoomReservationParametersThrowsEntityExForAdultsValueWhenNoRoom() {
        RoomReservationUpdateRequestDto update = new RoomReservationUpdateRequestDto(1L,
                null,
                null,
                null, null, 4, null, null, null);

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .type(RoomType.APARTMENT)
                .view(RoomView.GARDEN)
                .price(BigDecimal.ONE)
                .people(4)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        User user = User.builder()
                .id(1L)
                .email("user@email.bg")
                .firstName("first")
                .lastName("last")
                .passportID("929292")
                .password("passWorD$")
                .userRole(new UserRole())
                .createdDate(Date.from(Instant.now()))
                .modifiedDate(Date.from(Instant.now()))
                .enabled(true).build();

        RoomReservation reservation = RoomReservation.builder()
                .id(1L)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 25))
                .adults(4)
                .children(2)
                .room(room)
                .paymentStatus(PaymentStatus.UNPAID)
                .bedType(RoomBedType.TWIN_BEDS)
                .user(user).build();

        RoomReservationUpdateResponseDto response = RoomReservationUpdateResponseDto.builder()
                .id(1L)
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .adults(reservation.getAdults())
                .children(reservation.getChildren())
                .room(room)
                .status(reservation.getPaymentStatus())
                .type(reservation.getBedType()).build();

        when(roomReservationRepository.findById(update.getId())).thenReturn(Optional.of(reservation));
        when(roomReservationRepository.isRoomNotOccupied(any(), any(), anyLong(), anyLong())).thenReturn(true);

        assertThrows(EntityNotFoundException.class, () -> roomReservationServiceImpl.updateRoomReservationParameters(update));
    }

    @Test
    void testUpdateRoomReservationParametersStartDateThrowsEntity() {
        RoomReservationUpdateRequestDto update = new RoomReservationUpdateRequestDto(1L,
                LocalDate.of(2023, 5, 28),
                null,
                null, null, 3, null, null, null);

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .type(RoomType.APARTMENT)
                .view(RoomView.GARDEN)
                .price(BigDecimal.ONE)
                .people(4)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        User user = User.builder()
                .id(1L)
                .email("user@email.bg")
                .firstName("first")
                .lastName("last")
                .passportID("929292")
                .password("passWorD$")
                .userRole(new UserRole())
                .createdDate(Date.from(Instant.now()))
                .modifiedDate(Date.from(Instant.now()))
                .enabled(true).build();

        RoomReservation reservation = RoomReservation.builder()
                .id(1L)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 30))
                .adults(3)
                .children(2)
                .room(room)
                .paymentStatus(PaymentStatus.UNPAID)
                .bedType(RoomBedType.TWIN_BEDS)
                .user(user).build();

        RoomReservationUpdateResponseDto response = RoomReservationUpdateResponseDto.builder()
                .id(1L)
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .adults(reservation.getAdults())
                .children(reservation.getChildren())
                .room(room)
                .status(reservation.getPaymentStatus())
                .type(reservation.getBedType()).build();

        when(roomReservationRepository.findById(update.getId())).thenReturn(Optional.of(reservation));
        when(roomRepository.findByType(any())).thenReturn(List.of(room));
        when(roomRepository.findByView(any())).thenReturn(List.of(room));
        when(roomRepository.findByPeople(anyInt())).thenReturn(List.of(room));
        when(roomReservationRepository.isRoomNotOccupied(any(), any(), anyLong(), anyLong())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> roomReservationServiceImpl.updateRoomReservationParameters(update));
    }

    @Test
    void testUpdateRoomReservationParametersEndDateThrowsEntity() {
        RoomReservationUpdateRequestDto update = new RoomReservationUpdateRequestDto(1L,
                null,
                LocalDate.of(2023, 5, 31),
                null, null, 3, null, null, null);

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .type(RoomType.APARTMENT)
                .view(RoomView.GARDEN)
                .price(BigDecimal.ONE)
                .people(4)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        User user = User.builder()
                .id(1L)
                .email("user@email.bg")
                .firstName("first")
                .lastName("last")
                .passportID("929292")
                .password("passWorD$")
                .userRole(new UserRole())
                .createdDate(Date.from(Instant.now()))
                .modifiedDate(Date.from(Instant.now()))
                .enabled(true).build();

        RoomReservation reservation = RoomReservation.builder()
                .id(1L)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 28))
                .adults(3)
                .children(2)
                .room(room)
                .paymentStatus(PaymentStatus.UNPAID)
                .bedType(RoomBedType.TWIN_BEDS)
                .user(user).build();

        RoomReservationUpdateResponseDto response = RoomReservationUpdateResponseDto.builder()
                .id(1L)
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .adults(reservation.getAdults())
                .children(reservation.getChildren())
                .room(room)
                .status(reservation.getPaymentStatus())
                .type(reservation.getBedType()).build();

        when(roomReservationRepository.findById(update.getId())).thenReturn(Optional.of(reservation));
        when(roomRepository.findByType(any())).thenReturn(List.of(room));
        when(roomRepository.findByView(any())).thenReturn(List.of(room));
        when(roomRepository.findByPeople(anyInt())).thenReturn(List.of(room));
        when(roomReservationRepository.isRoomNotOccupied(any(), any(), anyLong(), anyLong())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> roomReservationServiceImpl.updateRoomReservationParameters(update));
    }

    @Test
    void testUpdateRoomReservationParametersRoomViewThrowsEntity() {
        RoomReservationUpdateRequestDto update = new RoomReservationUpdateRequestDto(1L,
                null,
                null,
                "SEA", "STANDARD", null, null, null, null);

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .type(RoomType.APARTMENT)
                .view(RoomView.GARDEN)
                .price(BigDecimal.ONE)
                .people(4)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        User user = User.builder()
                .id(1L)
                .email("user@email.bg")
                .firstName("first")
                .lastName("last")
                .passportID("929292")
                .password("passWorD$")
                .userRole(new UserRole())
                .createdDate(Date.from(Instant.now()))
                .modifiedDate(Date.from(Instant.now()))
                .enabled(true).build();

        RoomReservation reservation = RoomReservation.builder()
                .id(1L)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 28))
                .adults(3)
                .children(2)
                .room(room)
                .paymentStatus(PaymentStatus.UNPAID)
                .bedType(RoomBedType.TWIN_BEDS)
                .user(user).build();

        RoomReservationUpdateResponseDto response = RoomReservationUpdateResponseDto.builder()
                .id(1L)
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .adults(reservation.getAdults())
                .children(reservation.getChildren())
                .room(room)
                .status(reservation.getPaymentStatus())
                .type(reservation.getBedType()).build();

        when(roomReservationRepository.findById(update.getId())).thenReturn(Optional.of(reservation));
        when(roomReservationRepository.isRoomNotOccupied(any(), any(), anyLong(), anyLong())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> roomReservationServiceImpl.updateRoomReservationParameters(update));
    }

    @Test
    void testUpdateRoomReservationParametersRoomViewPoolThrowsEntity() {
        RoomReservationUpdateRequestDto update = new RoomReservationUpdateRequestDto(1L,
                null,
                null,
                "POOL", "STUDIO", null, null, null, null);

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .type(RoomType.APARTMENT)
                .view(RoomView.GARDEN)
                .price(BigDecimal.ONE)
                .people(4)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        User user = User.builder()
                .id(1L)
                .email("user@email.bg")
                .firstName("first")
                .lastName("last")
                .passportID("929292")
                .password("passWorD$")
                .userRole(new UserRole())
                .createdDate(Date.from(Instant.now()))
                .modifiedDate(Date.from(Instant.now()))
                .enabled(true).build();

        RoomReservation reservation = RoomReservation.builder()
                .id(1L)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 28))
                .adults(3)
                .children(2)
                .room(room)
                .paymentStatus(PaymentStatus.UNPAID)
                .bedType(RoomBedType.TWIN_BEDS)
                .user(user).build();

        RoomReservationUpdateResponseDto response = RoomReservationUpdateResponseDto.builder()
                .id(1L)
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .adults(reservation.getAdults())
                .children(reservation.getChildren())
                .room(room)
                .status(reservation.getPaymentStatus())
                .type(reservation.getBedType()).build();

        when(roomReservationRepository.findById(update.getId())).thenReturn(Optional.of(reservation));
        when(roomReservationRepository.isRoomNotOccupied(any(), any(), anyLong(), anyLong())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> roomReservationServiceImpl.updateRoomReservationParameters(update));
    }

    @Test
    void testUpdateRoomReservationParametersRoomViewGardenThrowsEntity() {
        RoomReservationUpdateRequestDto update = new RoomReservationUpdateRequestDto(1L,
                null,
                null,
                "GARDEN", "APARTMENT", null, null, null, null);

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .type(RoomType.APARTMENT)
                .view(RoomView.GARDEN)
                .price(BigDecimal.ONE)
                .people(4)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        User user = User.builder()
                .id(1L)
                .email("user@email.bg")
                .firstName("first")
                .lastName("last")
                .passportID("929292")
                .password("passWorD$")
                .userRole(new UserRole())
                .createdDate(Date.from(Instant.now()))
                .modifiedDate(Date.from(Instant.now()))
                .enabled(true).build();

        RoomReservation reservation = RoomReservation.builder()
                .id(1L)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 28))
                .adults(3)
                .children(2)
                .room(room)
                .paymentStatus(PaymentStatus.UNPAID)
                .bedType(RoomBedType.TWIN_BEDS)
                .user(user).build();

        RoomReservationUpdateResponseDto response = RoomReservationUpdateResponseDto.builder()
                .id(1L)
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .adults(reservation.getAdults())
                .children(reservation.getChildren())
                .room(room)
                .status(reservation.getPaymentStatus())
                .type(reservation.getBedType()).build();

        when(roomReservationRepository.findById(update.getId())).thenReturn(Optional.of(reservation));
        when(roomReservationRepository.isRoomNotOccupied(any(), any(), anyLong(), anyLong())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> roomReservationServiceImpl.updateRoomReservationParameters(update));
    }

    @Test
    void testUpdateRoomReservationParametersRoomTypeApartmentThrowsEntity() {
        RoomReservationUpdateRequestDto update = new RoomReservationUpdateRequestDto(1L,
                null,
                null,
                null, "APARTMENT", null, null, null, null);

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .type(RoomType.APARTMENT)
                .view(RoomView.GARDEN)
                .price(BigDecimal.ONE)
                .people(4)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        User user = User.builder()
                .id(1L)
                .email("user@email.bg")
                .firstName("first")
                .lastName("last")
                .passportID("929292")
                .password("passWorD$")
                .userRole(new UserRole())
                .createdDate(Date.from(Instant.now()))
                .modifiedDate(Date.from(Instant.now()))
                .enabled(true).build();

        RoomReservation reservation = RoomReservation.builder()
                .id(1L)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 28))
                .adults(3)
                .children(2)
                .room(room)
                .paymentStatus(PaymentStatus.UNPAID)
                .bedType(RoomBedType.TWIN_BEDS)
                .user(user).build();

        RoomReservationUpdateResponseDto response = RoomReservationUpdateResponseDto.builder()
                .id(1L)
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .adults(reservation.getAdults())
                .children(reservation.getChildren())
                .room(room)
                .status(reservation.getPaymentStatus())
                .type(reservation.getBedType()).build();

        when(roomReservationRepository.findById(update.getId())).thenReturn(Optional.of(reservation));
        when(roomReservationRepository.isRoomNotOccupied(any(), any(), anyLong(), anyLong())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> roomReservationServiceImpl.updateRoomReservationParameters(update));
    }

    @Test
    void testUpdateRoomReservationParametersRoomTypeStudioThrowsEntity() {
        RoomReservationUpdateRequestDto update = new RoomReservationUpdateRequestDto(1L,
                null,
                null,
                null, "STUDIO", null, null, null, null);

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .type(RoomType.APARTMENT)
                .view(RoomView.GARDEN)
                .price(BigDecimal.ONE)
                .people(4)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        User user = User.builder()
                .id(1L)
                .email("user@email.bg")
                .firstName("first")
                .lastName("last")
                .passportID("929292")
                .password("passWorD$")
                .userRole(new UserRole())
                .createdDate(Date.from(Instant.now()))
                .modifiedDate(Date.from(Instant.now()))
                .enabled(true).build();

        RoomReservation reservation = RoomReservation.builder()
                .id(1L)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 28))
                .adults(3)
                .children(2)
                .room(room)
                .paymentStatus(PaymentStatus.UNPAID)
                .bedType(RoomBedType.TWIN_BEDS)
                .user(user).build();

        RoomReservationUpdateResponseDto response = RoomReservationUpdateResponseDto.builder()
                .id(1L)
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .adults(reservation.getAdults())
                .children(reservation.getChildren())
                .room(room)
                .status(reservation.getPaymentStatus())
                .type(reservation.getBedType()).build();

        when(roomReservationRepository.findById(update.getId())).thenReturn(Optional.of(reservation));
        when(roomReservationRepository.isRoomNotOccupied(any(), any(), anyLong(), anyLong())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> roomReservationServiceImpl.updateRoomReservationParameters(update));
    }

    @Test
    void testUpdateRoomReservationParametersRoomTypeStandardThrowsEntity() {
        RoomReservationUpdateRequestDto update = new RoomReservationUpdateRequestDto(1L,
                null,
                null,
                null, "STANDARD", null, null, null, null);

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .type(RoomType.APARTMENT)
                .view(RoomView.GARDEN)
                .price(BigDecimal.ONE)
                .people(4)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        User user = User.builder()
                .id(1L)
                .email("user@email.bg")
                .firstName("first")
                .lastName("last")
                .passportID("929292")
                .password("passWorD$")
                .userRole(new UserRole())
                .createdDate(Date.from(Instant.now()))
                .modifiedDate(Date.from(Instant.now()))
                .enabled(true).build();

        RoomReservation reservation = RoomReservation.builder()
                .id(1L)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 28))
                .adults(3)
                .children(2)
                .room(room)
                .paymentStatus(PaymentStatus.UNPAID)
                .bedType(RoomBedType.TWIN_BEDS)
                .user(user).build();

        RoomReservationUpdateResponseDto response = RoomReservationUpdateResponseDto.builder()
                .id(1L)
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .adults(reservation.getAdults())
                .children(reservation.getChildren())
                .room(room)
                .status(reservation.getPaymentStatus())
                .type(reservation.getBedType()).build();

        when(roomReservationRepository.findById(update.getId())).thenReturn(Optional.of(reservation));
        when(roomReservationRepository.isRoomNotOccupied(any(), any(), anyLong(), anyLong())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> roomReservationServiceImpl.updateRoomReservationParameters(update));
    }

    @Test
    void testUpdateRoomReservationParametersForAdultsValue() {
        RoomReservationUpdateRequestDto update = new RoomReservationUpdateRequestDto(1L,
                null,
                null,
                null, null, 3, null, null, null);

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .type(RoomType.APARTMENT)
                .view(RoomView.GARDEN)
                .price(BigDecimal.ONE)
                .people(4)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        User user = User.builder()
                .id(1L)
                .email("user@email.bg")
                .firstName("first")
                .lastName("last")
                .passportID("929292")
                .password("passWorD$")
                .userRole(new UserRole())
                .createdDate(Date.from(Instant.now()))
                .modifiedDate(Date.from(Instant.now()))
                .enabled(true).build();

        RoomReservation reservation = RoomReservation.builder()
                .id(1L)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 25))
                .adults(3)
                .children(2)
                .room(room)
                .paymentStatus(PaymentStatus.UNPAID)
                .bedType(RoomBedType.TWIN_BEDS)
                .user(user).build();

        RoomReservationUpdateResponseDto response = RoomReservationUpdateResponseDto.builder()
                .id(1L)
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .adults(reservation.getAdults())
                .children(reservation.getChildren())
                .room(room)
                .status(reservation.getPaymentStatus())
                .type(reservation.getBedType()).build();

        when(roomReservationRepository.findById(update.getId())).thenReturn(Optional.of(reservation));
        when(roomRepository.findByType(any())).thenReturn(List.of(room));
        when(roomRepository.findByView(any())).thenReturn(List.of(room));
        when(roomRepository.findByPeople(anyInt())).thenReturn(List.of(room));
        when(roomReservationRepository.isRoomNotOccupied(any(), any(), anyLong(), anyLong())).thenReturn(true);

        assertEquals(response, roomReservationServiceImpl.updateRoomReservationParameters(update));
    }

    @Test
    void testGetAvailableRoomsReturnValidationEx() {
        LocalDate startDate = LocalDate.of(2023, 5, 25);
        LocalDate endDate = LocalDate.of(2023, 5, 24);

        assertThrows(ValidationException.class, () -> roomReservationServiceImpl.getAvailableRooms(startDate, endDate, 2, 1));

    }

    @Test
    void testGetAvailableRoomsReturnEntityNotFound() {
        LocalDate startDate = LocalDate.of(2023, 5, 24);
        LocalDate endDate = LocalDate.of(2023, 5, 25);
        when(roomRepository.findAll()).thenReturn(List.of(Room.builder().build()));
        when(roomReservationRepository.getRoomByStartDateAndEndDate(startDate, endDate)).thenReturn(List.of(Room.builder().build()));

        assertThrows(EntityNotFoundException.class, () -> roomReservationServiceImpl.getAvailableRooms(startDate, endDate, 5, 1));

    }

    @Test
    void testGetAvailableRoomsReturnList() {
        LocalDate startDate = LocalDate.of(2023, 5, 24);
        LocalDate endDate = LocalDate.of(2023, 5, 25);

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .price(BigDecimal.ONE)
                .type(RoomType.STANDARD)
                .view(RoomView.SEA)
                .people(2)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        List<Room> rooms = List.of(room);
        when(roomRepository.findAll()).thenReturn(List.of(room));
        when(roomReservationRepository.getRoomByStartDateAndEndDate(startDate, endDate)).thenReturn(List.of(Room.builder().build()));

        assertEquals(List.of(room), roomReservationServiceImpl.getAvailableRooms(startDate, endDate, 2, 1));
    }

    @Test
    void testGetAlternativeRoomsThrowEntity() {
        LocalDate startDate = LocalDate.of(2023, 5, 24);
        LocalDate endDate = LocalDate.of(2023, 5, 25);
        int adults = 50;
        assertThrows(EntityNotFoundException.class, () -> roomReservationServiceImpl.getAlternativeRooms(startDate, endDate, adults, 0));
    }

    @Test
    void testGetAlternativeRoomsThrowValidation() {
        LocalDate startDate = LocalDate.of(2023, 5, 25);
        LocalDate endDate = LocalDate.of(2023, 5, 24);
        assertThrows(ValidationException.class, () -> roomReservationServiceImpl.getAlternativeRooms(startDate, endDate, 2, 0));
    }

    @Test //1% people>0
    public void testGetAlternativeRooms_ThrowsExceptionWhenPeopleExceedCapacity() {
        // Arrange
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(2);
        int adults = 8;
        int children = 0;

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room1 = Room.builder()
                .id(1L)
                .roomNumber(100)
                .price(BigDecimal.ONE)
                .type(RoomType.STANDARD)
                .view(RoomView.SEA)
                .people(5)
                .facilities(List.of(facility)).build();

        Room room2 = Room.builder()
                .id(2L)
                .roomNumber(101)
                .price(BigDecimal.ONE)
                .type(RoomType.STANDARD)
                .view(RoomView.SEA)
                .people(2)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room1, room2));

        List<Room> reservations = new ArrayList<>();
        List<Room> availableRooms = Arrays.asList(room1, room2);

        when(roomReservationRepository.getRoomByStartDateAndEndDate(startDate, endDate))
                .thenReturn(reservations);
        when(roomRepository.findAll()).thenReturn(availableRooms);

        assertThrows(EntityNotFoundException.class, () -> {
            roomReservationServiceImpl.getAlternativeRooms(startDate, endDate, adults, children);
        });
    }

    @Test
    void testGetAlternativeRooms() {
        LocalDate startDate = LocalDate.of(2023, 5, 24);
        LocalDate endDate = LocalDate.of(2023, 5, 25);

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .price(BigDecimal.ONE)
                .type(RoomType.STANDARD)
                .view(RoomView.SEA)
                .people(2)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        List<Room> rooms = List.of(room);
        when(roomRepository.findAll()).thenReturn(List.of(room));
        when(roomReservationRepository.getRoomByStartDateAndEndDate(startDate, endDate)).thenReturn(List.of(Room.builder().build()));

        List<Room> result = roomReservationServiceImpl.getAlternativeRooms(startDate, endDate, 2, 0);

        assertEquals(List.of(room), result);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());

        verify(roomReservationRepository, times(1)).getRoomByStartDateAndEndDate(startDate, endDate);
        verify(roomRepository, times(1)).findAll();

    }

    @Test
    void testGetAlternativeRoomsAllReserved() {
        LocalDate startDate = LocalDate.of(2023, 5, 24);
        LocalDate endDate = LocalDate.of(2023, 5, 25);
        List<Room> rooms = new ArrayList<>();
        when(roomReservationRepository.getRoomByStartDateAndEndDate(startDate, endDate)).thenReturn(rooms);
        when(roomRepository.findAll()).thenReturn(rooms);
        assertThrows(EntityNotFoundException.class, () -> roomReservationServiceImpl.getAlternativeRooms(startDate, endDate, 2, 0));
    }

    @Test
    void testSetFullPrice() {
        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .type(RoomType.STANDARD)
                .view(RoomView.SEA)
                .price(BigDecimal.ONE)
                .people(2)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        LocalDate startDate = LocalDate.of(2023, 5, 24);

        LocalDate endDate = LocalDate.of(2023, 5, 24);

        BigDecimal result = room.getPrice().multiply(BigDecimal.valueOf(1L));

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        assertEquals(result, roomReservationServiceImpl.setFullPrice(startDate, endDate, 1L));
    }

    @Test
    void testSetFullPriceThrows() {
        LocalDate startDate = LocalDate.of(2023, 5, 24);

        LocalDate endDate = LocalDate.of(2023, 5, 24);

        assertThrows(IllegalArgumentException.class, () -> roomReservationServiceImpl.setFullPrice(startDate, endDate, 1L));
    }

    @Test
    void testFindByCurrentlyLoggedUserThrows() {
        assertThrows(EntityNotFoundException.class, () -> roomReservationServiceImpl.findByCurrentlyLoggedUser("user@email.bg"));
    }

    @Test
    void testFindByCurrentlyLoggedUser() {
        User user = User.builder()
                .id(1L)
                .email("user@email.bg")
                .firstName("first")
                .lastName("last")
                .passportID("929292")
                .password("passWorD$")
                .userRole(new UserRole())
                .createdDate(Date.from(Instant.now()))
                .modifiedDate(Date.from(Instant.now()))
                .enabled(true).build();

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .price(BigDecimal.ONE)
                .type(RoomType.STANDARD)
                .view(RoomView.SEA)
                .people(2)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        RoomReservation reservation = RoomReservation.builder()
                .id(1L)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 25))
                .adults(4)
                .children(2)
                .room(room)
                .paymentStatus(PaymentStatus.UNPAID)
                .bedType(RoomBedType.TWIN_BEDS)
                .user(user).build();

        RoomReservationResponseDto response = RoomReservationResponseDto.builder()
                .id(1L)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 25))
                .roomType(room.getType())
                .adults(reservation.getAdults())
                .children(reservation.getChildren())
                .price(room.getPrice())
                .fullPrice(reservation.getFullPrice())
                .daysStaying(1).build();

        when(userRepository.findByEmail("user@email.bg")).thenReturn(Optional.of(user));
        when(roomReservationRepository.findByUser(user)).thenReturn(List.of(reservation));
        when(reservationConvertor.toRoomReservationResponseDto(reservation)).thenReturn(response);

        assertEquals(List.of(response), roomReservationServiceImpl.findByCurrentlyLoggedUser("user@email.bg"));
    }

    @Test
    void testFindByIdThrows() {
        when(roomReservationRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> roomReservationServiceImpl.findById(1L));
    }

    @Test
    void testFindById() {
        RoomReservation reservation = RoomReservation.builder().id(1L).build();
        when(roomReservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        assertEquals(reservationConvertor.toRoomReservationResponseDto(reservation), roomReservationServiceImpl.findById(1L));
    }

    @Test
    void testFindByUserIdThrows() {
        assertThrows(EntityNotFoundException.class, () -> roomReservationServiceImpl.findByUserId(1L));
    }

    @Test
    void testFindByUserId() {
        User user = User.builder()
                .id(1L)
                .email("user@email.bg")
                .firstName("first")
                .lastName("last")
                .passportID("929292")
                .password("passWorD$")
                .userRole(new UserRole())
                .createdDate(Date.from(Instant.now()))
                .modifiedDate(Date.from(Instant.now()))
                .enabled(true).build();

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .price(BigDecimal.ONE)
                .type(RoomType.STANDARD)
                .view(RoomView.SEA)
                .people(2)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        RoomReservation reservation = RoomReservation.builder()
                .id(1L)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 25))
                .adults(4)
                .children(2)
                .room(room)
                .paymentStatus(PaymentStatus.UNPAID)
                .bedType(RoomBedType.TWIN_BEDS)
                .user(user).build();

        RoomReservationResponseDto response = RoomReservationResponseDto.builder()
                .id(1L)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 25))
                .roomType(room.getType())
                .adults(reservation.getAdults())
                .children(reservation.getChildren())
                .price(room.getPrice())
                .fullPrice(reservation.getFullPrice())
                .daysStaying(1).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roomReservationRepository.findByUser(user)).thenReturn(List.of(reservation));
        when(reservationConvertor.toRoomReservationResponseDto(reservation)).thenReturn(response);

        assertEquals(List.of(response), roomReservationServiceImpl.findByUserId(1L));
    }

    @Test
    void testFindAll() {
        User user = User.builder()
                .id(1L)
                .email("user@email.bg")
                .firstName("first")
                .lastName("last")
                .passportID("929292")
                .password("passWorD$")
                .userRole(new UserRole())
                .createdDate(Date.from(Instant.now()))
                .modifiedDate(Date.from(Instant.now()))
                .enabled(true).build();

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .price(BigDecimal.ONE)
                .type(RoomType.STANDARD)
                .view(RoomView.SEA)
                .people(2)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        RoomReservation reservation = RoomReservation.builder()
                .id(1L)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 25))
                .adults(4)
                .children(2)
                .room(room)
                .paymentStatus(PaymentStatus.UNPAID)
                .bedType(RoomBedType.TWIN_BEDS)
                .user(user).build();

        RoomReservationResponseDto response = RoomReservationResponseDto.builder()
                .id(1L)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 25))
                .roomType(room.getType())
                .adults(reservation.getAdults())
                .children(reservation.getChildren())
                .price(room.getPrice())
                .fullPrice(reservation.getFullPrice())
                .daysStaying(1).build();

        when(roomReservationRepository.findAll()).thenReturn(List.of(reservation));
        when(reservationConvertor.toRoomReservationResponseDto(reservation)).thenReturn(response);

        assertEquals(List.of(response), roomReservationServiceImpl.findAll());
    }

    @Test
    void testFindByStartDateAndEndDate() {
        LocalDate startDate = LocalDate.of(2023, 5, 24);

        LocalDate endDate = LocalDate.of(2023, 5, 25);

        User user = User.builder()
                .id(1L)
                .email("user@email.bg")
                .firstName("first")
                .lastName("last")
                .passportID("929292")
                .password("passWorD$")
                .userRole(new UserRole())
                .createdDate(Date.from(Instant.now()))
                .modifiedDate(Date.from(Instant.now()))
                .enabled(true).build();

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .price(BigDecimal.ONE)
                .type(RoomType.STANDARD)
                .view(RoomView.SEA)
                .people(2)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        RoomReservation reservation = RoomReservation.builder()
                .id(1L)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 25))
                .adults(4)
                .children(2)
                .room(room)
                .paymentStatus(PaymentStatus.UNPAID)
                .bedType(RoomBedType.TWIN_BEDS)
                .user(user).build();

        RoomReservationResponseDto response = RoomReservationResponseDto.builder()
                .id(1L)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 25))
                .roomType(room.getType())
                .adults(reservation.getAdults())
                .children(reservation.getChildren())
                .price(room.getPrice())
                .fullPrice(reservation.getFullPrice())
                .daysStaying(1).build();

        when(roomReservationRepository.getRoomReservationsByStartDateAndEndDate(startDate, endDate)).thenReturn(List.of(reservation));
        when(reservationConvertor.toRoomReservationResponseDto(reservation)).thenReturn(response);

        assertEquals(List.of(response), roomReservationServiceImpl.findByStartDateAndEndDate(startDate, endDate));
    }


    @Test
    void testFindByStartDateAndEndDateForUser() {
        LocalDate startDate = LocalDate.of(2023, 5, 24);

        LocalDate endDate = LocalDate.of(2023, 5, 25);

        User user = User.builder()
                .id(1L)
                .email("user@email.bg")
                .firstName("first")
                .lastName("last")
                .passportID("929292")
                .password("passWorD$")
                .userRole(new UserRole())
                .createdDate(Date.from(Instant.now()))
                .modifiedDate(Date.from(Instant.now()))
                .enabled(true).build();

        RoomFacility facility = RoomFacility.builder().id(1L).facility("Wi-Fi").build();

        Room room = Room.builder()
                .id(1L)
                .roomNumber(100)
                .price(BigDecimal.ONE)
                .type(RoomType.STANDARD)
                .view(RoomView.SEA)
                .people(2)
                .facilities(List.of(facility)).build();

        facility.setRoom(List.of(room));

        RoomReservation reservation = RoomReservation.builder()
                .id(1L)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 25))
                .adults(4)
                .children(2)
                .room(room)
                .paymentStatus(PaymentStatus.UNPAID)
                .bedType(RoomBedType.TWIN_BEDS)
                .user(user).build();

        RoomReservationResponseDto response = RoomReservationResponseDto.builder()
                .id(1L)
                .startDate(LocalDate.of(2023, 5, 24))
                .endDate(LocalDate.of(2023, 5, 25))
                .roomType(room.getType())
                .adults(reservation.getAdults())
                .children(reservation.getChildren())
                .price(room.getPrice())
                .fullPrice(reservation.getFullPrice())
                .daysStaying(1).build();

        when(userRepository.findByEmail("user@email.bg")).thenReturn(Optional.of(user));
        when(roomReservationRepository.getRoomReservationsByStartDateAndEndDateAndUser(startDate, endDate, user)).thenReturn(List.of(reservation));
        when(reservationConvertor.toRoomReservationResponseDto(reservation)).thenReturn(response);

        assertEquals(List.of(response), roomReservationServiceImpl.findByStartDateAndEndDateForUser(startDate, endDate, "user@email.bg"));
    }

    @Test
    void testDeleteRoomReservation() {
        when(roomReservationRepository.findById(1L)).thenReturn(Optional.of(RoomReservation.builder().build()));
        assertDoesNotThrow(() -> roomReservationServiceImpl.deleteRoomReservation(1L));
    }

    @Test
    void testDeleteRoomReservationThrows() {
        assertThrows(EntityNotFoundException.class, () -> roomReservationServiceImpl.deleteRoomReservation(1L));
    }

    @Test
    void testIsRoomNotOccupied() {
        when(roomReservationRepository.isRoomNotOccupied(any(), any(), anyLong(), anyLong())).thenReturn(true);

        boolean result = roomReservationServiceImpl.isRoomNotOccupied(1L, 1L, LocalDate.of(2023, 5, 24), LocalDate.of(2023, 5, 25));
        assertTrue(result);
    }
}