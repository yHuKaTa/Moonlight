package com.aacdemy.moonlight.service;

import com.aacdemy.moonlight.config.security.JwtService;
import com.aacdemy.moonlight.dto.restaurant.TableReservationRequestDto;
import com.aacdemy.moonlight.dto.restaurant.TableReservationResponseDto;
import com.aacdemy.moonlight.entity.restaurant.TableReservation;
import com.aacdemy.moonlight.entity.restaurant.TableRestaurant;
import com.aacdemy.moonlight.entity.user.User;
import com.aacdemy.moonlight.exception.EntityNotFoundException;
import com.aacdemy.moonlight.repository.restaurant.TableReservationRepository;
import com.aacdemy.moonlight.repository.restaurant.TableRestaurantRepository;
import com.aacdemy.moonlight.repository.user.UserRepository;
import com.aacdemy.moonlight.service.impl.TableReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TableReservationServiceImplTest {

    @Mock
    private TableReservationRepository tableReservationRepository;
    @Mock
    private TableRestaurantRepository tableRestaurantRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private TableReservationServiceImpl tableReservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveReservation() {

        TableReservationServiceImpl reservationService = new TableReservationServiceImpl(
                tableReservationRepository, tableRestaurantRepository, userRepository,
                jwtService, authenticationManager);

        String token = "valid_token";
        TableReservationRequestDto reservationRequest = new TableReservationRequestDto();
        reservationRequest.setDate(LocalDate.now().plusDays(1));
        reservationRequest.setTime(LocalTime.parse("18:00"));
        reservationRequest.setPeople(4);
        reservationRequest.setTableId(1L);

        String userEmail = "test@example.com";
        User existingUser = new User();
        existingUser.setEmail(userEmail);

        when(jwtService.extractUserName(token)).thenReturn(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(existingUser));

        when(tableReservationRepository.findByUserAndDate(existingUser, reservationRequest.getDate()))
                .thenReturn(Collections.emptyList());

        TableRestaurant tableRestaurant = new TableRestaurant();
        tableRestaurant.setSeats(4);

        when(tableRestaurantRepository.findById(reservationRequest.getTableId()))
                .thenReturn(Optional.of(tableRestaurant));

        TableReservation savedReservation = new TableReservation();
        savedReservation.setTable(tableRestaurant);
        savedReservation.setId(1L);

        when(tableReservationRepository.save(any(TableReservation.class))).thenReturn(savedReservation);

        TableReservationResponseDto response = reservationService.saveReservation(reservationRequest, token);

        assertNotNull(response);

    }

    @Test
    void testSaveReservation_InvalidToken() {

        TableReservationServiceImpl reservationService = new TableReservationServiceImpl(
                tableReservationRepository, tableRestaurantRepository, userRepository,
                jwtService, authenticationManager);

        String invalidToken = "invalid_token";
        TableReservationRequestDto reservationRequest = new TableReservationRequestDto();

        when(jwtService.extractUserName(invalidToken)).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> reservationService.saveReservation(reservationRequest, invalidToken));
    }

    @Test
    void testSaveReservation_UserNotFound() {

        TableReservationServiceImpl reservationService = new TableReservationServiceImpl(
                tableReservationRepository, tableRestaurantRepository, userRepository,
                jwtService, authenticationManager);

        String token = "valid_token";
        TableReservationRequestDto reservationRequest = new TableReservationRequestDto();

        String userEmail = "userNotFound@example.com";

        when(jwtService.extractUserName(token)).thenReturn(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> reservationService.saveReservation(reservationRequest, token));
    }

    @Test
    void testConvertToResponseDto_InvalidReservation() {

        TableReservationServiceImpl reservationService = new TableReservationServiceImpl(
                tableReservationRepository, tableRestaurantRepository, userRepository,
                jwtService, authenticationManager);

        assertThrows(IllegalArgumentException.class,
                () -> reservationService.convertToResponseDto(null));
    }
    @Test
    void testSaveReservation_UserAlreadyHasReservation_ThrowsException() {
        TableReservationServiceImpl reservationService = new TableReservationServiceImpl(
                tableReservationRepository, tableRestaurantRepository, userRepository,
                jwtService, authenticationManager);

        String token = "valid_token";
        TableReservationRequestDto reservationRequest = new TableReservationRequestDto();
        reservationRequest.setDate(LocalDate.now().plusDays(1));
        reservationRequest.setTime(LocalTime.parse("18:00"));
        reservationRequest.setPeople(4);
        reservationRequest.setTableId(1L);

        String userEmail = "test@example.com";
        User existingUser = new User();
        existingUser.setEmail(userEmail);

        when(jwtService.extractUserName(token)).thenReturn(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(existingUser));

        List<TableReservation> existingReservations = new ArrayList<>();
        existingReservations.add(new TableReservation());
        when(tableReservationRepository.findByUserAndDate(existingUser, reservationRequest.getDate()))
                .thenReturn(existingReservations);

        assertThrows(IllegalArgumentException.class,
                () -> reservationService.saveReservation(reservationRequest, token));
    }
    @Test
    void testSaveReservation_ReservingPastDate_ThrowsException() {
        TableReservationServiceImpl reservationService = new TableReservationServiceImpl(
                tableReservationRepository, tableRestaurantRepository, userRepository,
                jwtService, authenticationManager);

        String token = "valid_token";
        TableReservationRequestDto reservationRequest = new TableReservationRequestDto();
        reservationRequest.setDate(LocalDate.now().minusDays(1));
        reservationRequest.setTime(LocalTime.parse("18:00"));
        reservationRequest.setPeople(4);
        reservationRequest.setTableId(1L);

        String userEmail = "test@example.com";
        User existingUser = new User();
        existingUser.setEmail(userEmail);

        when(jwtService.extractUserName(token)).thenReturn(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(existingUser));

        when(tableReservationRepository.findByUserAndDate(existingUser, reservationRequest.getDate()))
                .thenReturn(Collections.emptyList());

        TableRestaurant tableRestaurant = new TableRestaurant();
        tableRestaurant.setSeats(4);

        when(tableRestaurantRepository.findById(reservationRequest.getTableId()))
                .thenReturn(Optional.of(tableRestaurant));

        assertThrows(IllegalArgumentException.class,
                () -> reservationService.saveReservation(reservationRequest, token));
    }

    @Test
    void testSaveReservation_InvalidNumberOfPeople_ThrowsException() {
        TableReservationServiceImpl reservationService = new TableReservationServiceImpl(
                tableReservationRepository, tableRestaurantRepository, userRepository,
                jwtService, authenticationManager);

        String token = "valid_token";
        TableReservationRequestDto reservationRequest = new TableReservationRequestDto();
        reservationRequest.setDate(LocalDate.now().plusDays(1));
        reservationRequest.setTime(LocalTime.parse("18:00"));
        reservationRequest.setPeople(0); // Set an invalid number of people
        reservationRequest.setTableId(1L);

        String userEmail = "test@example.com";
        User existingUser = new User();
        existingUser.setEmail(userEmail);

        when(jwtService.extractUserName(token)).thenReturn(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(existingUser));

        when(tableReservationRepository.findByUserAndDate(existingUser, reservationRequest.getDate()))
                .thenReturn(Collections.emptyList());

        TableRestaurant tableRestaurant = new TableRestaurant();
        tableRestaurant.setSeats(4);

        when(tableRestaurantRepository.findById(reservationRequest.getTableId()))
                .thenReturn(Optional.of(tableRestaurant));

        assertThrows(IllegalArgumentException.class,
                () -> reservationService.saveReservation(reservationRequest, token));
    }
    @Test
    void testSaveReservation_TableDoesNotHaveEnoughSeats() {

        TableReservationServiceImpl reservationService = new TableReservationServiceImpl(
                tableReservationRepository, tableRestaurantRepository, userRepository,
                jwtService, authenticationManager);

        String token = "valid_token";
        TableReservationRequestDto reservationRequest = new TableReservationRequestDto();
        reservationRequest.setDate(LocalDate.now().plusDays(1));

        String userEmail = "test@example.com";
        User existingUser = new User();
        existingUser.setEmail(userEmail);

        when(jwtService.extractUserName(token)).thenReturn(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(existingUser));

        when(tableReservationRepository.findByUserAndDate(existingUser, reservationRequest.getDate()))
                .thenReturn(Collections.emptyList());

        TableRestaurant tableRestaurant = new TableRestaurant();
        tableRestaurant.setSeats(3);

        when(tableRestaurantRepository.findById(reservationRequest.getTableId()))
                .thenReturn(Optional.of(tableRestaurant));

        assertThrows(NullPointerException.class,
                () -> reservationService.saveReservation(reservationRequest, token));
    }
    @Test
    void testSaveReservation_NotEnoughSeatsInTable() {

        TableReservationServiceImpl reservationService = new TableReservationServiceImpl(
                tableReservationRepository, tableRestaurantRepository, userRepository,
                jwtService, authenticationManager);

        String token = "valid_token";
        TableReservationRequestDto reservationRequest = new TableReservationRequestDto();
        reservationRequest.setDate(LocalDate.now().plusDays(1));
        reservationRequest.setTime(LocalTime.parse("18:00"));
        reservationRequest.setPeople(4);
        reservationRequest.setTableId(1L);

        String userEmail = "test@example.com";
        User existingUser = new User();
        existingUser.setEmail(userEmail);

        when(jwtService.extractUserName(token)).thenReturn(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(existingUser));

        when(tableReservationRepository.findByUserAndDate(existingUser, reservationRequest.getDate()))
                .thenReturn(Collections.emptyList());

        TableRestaurant tableRestaurant = new TableRestaurant();
        tableRestaurant.setSeats(2);

        when(tableRestaurantRepository.findById(reservationRequest.getTableId()))
                .thenReturn(Optional.of(tableRestaurant));

        assertThrows(IllegalArgumentException.class,
                () -> reservationService.saveReservation(reservationRequest, token));
    }
    @Test
    void testSaveReservation_InvalidTime_ThrowsException() {
        TableReservationServiceImpl reservationService = new TableReservationServiceImpl(
                tableReservationRepository, tableRestaurantRepository, userRepository,
                jwtService, authenticationManager);

        String token = "valid_token";
        TableReservationRequestDto reservationRequest = new TableReservationRequestDto();
        reservationRequest.setDate(LocalDate.now().plusDays(1));
        reservationRequest.setTime(LocalTime.parse("23:59").plusMinutes(1));
        reservationRequest.setPeople(4);
        reservationRequest.setTableId(1L);

        String userEmail = "test@example.com";
        User existingUser = new User();
        existingUser.setEmail(userEmail);

        when(jwtService.extractUserName(token)).thenReturn(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(existingUser));

        when(tableReservationRepository.findByUserAndDate(existingUser, reservationRequest.getDate()))
                .thenReturn(Collections.emptyList());

        TableRestaurant tableRestaurant = new TableRestaurant();
        tableRestaurant.setSeats(4);

        when(tableRestaurantRepository.findById(reservationRequest.getTableId()))
                .thenReturn(Optional.of(tableRestaurant));

        assertThrows(IllegalArgumentException.class,
                () -> reservationService.saveReservation(reservationRequest, token));
    }
}