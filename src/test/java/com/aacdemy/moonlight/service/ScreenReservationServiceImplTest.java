package com.aacdemy.moonlight.service;

import com.aacdemy.moonlight.config.security.JwtService;
import com.aacdemy.moonlight.dto.screen.ScreenReservationRequestDto;
import com.aacdemy.moonlight.dto.screen.ScreenReservationResponseDto;
import com.aacdemy.moonlight.dto.screen.ScreenReservationSearchResponseDto;
import com.aacdemy.moonlight.entity.PaymentStatus;
import com.aacdemy.moonlight.entity.screen.Screen;
import com.aacdemy.moonlight.entity.screen.ScreenEvent;
import com.aacdemy.moonlight.entity.screen.ScreenReservation;
import com.aacdemy.moonlight.entity.screen.ScreenSeat;
import com.aacdemy.moonlight.entity.user.User;
import com.aacdemy.moonlight.exception.EntityNotFoundException;
import com.aacdemy.moonlight.repository.screen.ScreenReservationRepository;
import com.aacdemy.moonlight.repository.screen.ScreenSeatsRepository;
import com.aacdemy.moonlight.repository.user.UserRepository;
import com.aacdemy.moonlight.service.impl.ScreenReservationServiceImpl;
import com.aacdemy.moonlight.service.impl.UserServiceImpl;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScreenReservationServiceImplTest {
    @Mock
    ScreenEventService screenEventService;
    @Mock
    ScreenSeatsRepository screenSeatsRepository;
    @Mock
    ScreenReservationRepository screenReservationRepository;
    @Mock
    UserServiceImpl userService;
    @Mock
    UserRepository userRepository;
    @Mock
    JwtService jwtService;
    @InjectMocks
    ScreenReservationServiceImpl screenReservationServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddReservation() {
        ScreenReservationRequestDto requestDto = ScreenReservationRequestDto.builder().eventId(1L).dateOfEvent(LocalDate.of(2023, 6, 1)).seatNumbers(List.of(1)).build();

        User user = User.builder().email("admin@admin.com").build();
        LocalDate date = LocalDate.of(2023, 6, 1);
        Screen screen = Screen.builder().id(1L).name("screen").build();
        ScreenSeat seat = ScreenSeat.builder().id(1L).screen(screen).seatPosition(1).build();
        ScreenEvent event = ScreenEvent.builder().id(1L).dateEvent(date).screen(screen).event("Music").build();


        ScreenReservationResponseDto responseDto = ScreenReservationResponseDto.builder().event(event).reservedSeats(Set.of(seat)).build();

        when(screenEventService.findAvailableEventsByDate(any())).thenReturn(List.of(event));
        when(screenSeatsRepository.findBySeatPositionAndScreen(anyInt(), any())).thenReturn(Optional.of(seat));
        when(screenReservationRepository.findByDateAndSeatsScreen(any(), any())).thenReturn(List.of(ScreenReservation.builder().id(2L).event(event).seats(Set.of(ScreenSeat.builder().build())).build()));
        when(userService.getByEmail(anyString())).thenReturn(user);

        ScreenReservationResponseDto result = screenReservationServiceImpl.addReservation(requestDto, "admin@admin.com");

        assertDoesNotThrow(() -> new EntityNotFoundException("Event not found"));
        assertDoesNotThrow(() -> new ValidationException());
    }

    @Test
    void testAddReservationThrowsEntityNotFound() {
        ScreenReservationRequestDto requestDto = ScreenReservationRequestDto.builder().eventId(1L).dateOfEvent(LocalDate.of(2023, 6, 1)).seatNumbers(List.of(1)).build();

        User user = User.builder().email("admin@admin.com").build();

        when(screenReservationRepository.findByDateAndSeatsScreen(any(), any())).thenReturn(List.of());
        assertThrows(EntityNotFoundException.class, () -> screenReservationServiceImpl.addReservation(requestDto, user.getEmail()));
    }

    @Test
    void testAddReservationThrowsValidationEx() {
        ScreenReservationRequestDto requestDto = ScreenReservationRequestDto.builder().eventId(1L).dateOfEvent(LocalDate.of(2023, 6, 1)).seatNumbers(List.of(1)).build();

        User user = User.builder().email("admin@admin.com").build();
        LocalDate date = LocalDate.of(2023, 6, 1);
        Screen screen = Screen.builder().id(1L).build();
        ScreenSeat seat = ScreenSeat.builder().screen(screen).seatPosition(1).build();
        ScreenEvent event = ScreenEvent.builder().id(1L).dateEvent(date).screen(screen).event("Music").build();

        when(screenEventService.findAvailableEventsByDate(any())).thenReturn(List.of(event));
        when(screenSeatsRepository.findBySeatPositionAndScreen(anyInt(), any())).thenReturn(Optional.of(ScreenSeat.builder().build()));
        when(screenReservationRepository.findByDateAndSeatsScreen(any(), any())).thenReturn(List.of(ScreenReservation.builder().id(1L).event(event).seats(Set.of(seat)).price(5.0).build()));

        assertThrows(ValidationException.class, () -> screenReservationServiceImpl.addReservation(requestDto, user.getEmail()));
    }


    @Test
    void testFindReservedSeats() {
        User user = User.builder().email("admin@admin.com").build();
        LocalDate date = LocalDate.of(2023, 6, 1);
        Screen screen = Screen.builder().id(1L).name("screen").build();
        ScreenSeat seat = ScreenSeat.builder().screen(screen).seatPosition(1).build();
        ScreenEvent event = ScreenEvent.builder().id(1L).dateEvent(date).screen(screen).event("Music").build();
        ScreenReservation reservation = ScreenReservation.builder().id(1L).event(event).seats(Set.of(seat)).price(5.00).status(PaymentStatus.UNPAID).user(user).build();

        when(screenReservationRepository.findByDateAndSeatsScreen(any(), any())).thenReturn(List.of(reservation));

        Set<ScreenSeat> result = screenReservationServiceImpl.findReservedSeats(date, screen);
        assertDoesNotThrow(() -> new RuntimeException());
        assertEquals(Set.of(seat), result);
    }

    @Test
    public void testGetFreeSeatsForEvent() {

        ScreenEvent event = new ScreenEvent();
        event.setId(1L);
        event.setDateEvent(LocalDate.now());
        event.setScreen(new Screen());

        Set<ScreenSeat> reservedSeats = new HashSet<>();

        ScreenSeat reservedSeat1 = new ScreenSeat();
        reservedSeat1.setId(1L);
        reservedSeats.add(reservedSeat1);

        ScreenSeat reservedSeat2 = new ScreenSeat();
        reservedSeat2.setId(2L);
        reservedSeats.add(reservedSeat2);

        List<ScreenSeat> allSeats = new ArrayList<>();
        ScreenSeat seat1 = new ScreenSeat();
        seat1.setId(1L);
        allSeats.add(seat1);

        ScreenSeat seat2 = new ScreenSeat();
        seat2.setId(2L);
        allSeats.add(seat2);

        when(screenEventService.findById(1L)).thenReturn(event);
        when(screenReservationRepository.findByDateAndSeatsScreen(event.getDateEvent(), event.getScreen())).thenReturn(new ArrayList<>());
        when(screenSeatsRepository.findByScreen(event.getScreen())).thenReturn(allSeats);

        List<ScreenSeat> freeSeats = screenReservationServiceImpl.getFreeSeatsForEvent(1L);

        verify(screenEventService).findById(1L);
        verify(screenReservationRepository).findByDateAndSeatsScreen(event.getDateEvent(), event.getScreen());
        verify(screenSeatsRepository).findByScreen(event.getScreen());
        assertEquals(2, freeSeats.size());
        assertEquals(1L, freeSeats.get(0).getId());
    }

    @Test
    public void findAll_whenExists_returnExpected() {
        //        arrange
        LocalDate reservationDate = LocalDate.of(2023, 5, 31);
        ScreenEvent event = new ScreenEvent(1L, "Event", LocalDate.now(), new Screen(1L, "Screen"));
        User reservationUser = new User();
        List<ScreenReservation> screenReservationList = new ArrayList<>();
        screenReservationList.add(new ScreenReservation(1L, reservationDate, event, null, 10, reservationUser, PaymentStatus.PAID));

        when(screenReservationRepository.findAll()).thenReturn(screenReservationList);

        //        act
        List<ScreenReservationSearchResponseDto> result = screenReservationServiceImpl.findAll();
        ScreenReservationSearchResponseDto expected = result.get(0);

        //        assert
        assertEquals(1, result.size());
        assertEquals(1, expected.getId());
        assertEquals(PaymentStatus.PAID, expected.getStatus());
        verify(screenReservationRepository, times(1)).findAll();
    }
    @Test
    public void findByCurrentlyLoggedUser_whenExists_returnExpected() {
        // arrange
        String token = "ssssssssssssssss";
        String email = "test@test.bg";
        User user = new User();
        user.setEmail(email);
        user.setId(1L);

        ScreenEvent event = new ScreenEvent(1L, "Event", LocalDate.now(), new Screen(1L, "Screen"));

        List<ScreenReservation> screenReservationList = new ArrayList<>();
        screenReservationList.add(new ScreenReservation(1L, null, event, null, 10, user, PaymentStatus.PAID));

        when(jwtService.extractUserName(anyString())).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(screenReservationRepository.findByUserId(user.getId())).thenReturn(screenReservationList);

        // act
        List<ScreenReservationSearchResponseDto> result = screenReservationServiceImpl.findByCurrentlyLoggedUser(token);
        ScreenReservationSearchResponseDto expected = result.get(0);
        //        assert
        assertEquals(1, result.size());
        assertEquals(1, expected.getId());
        assertEquals(PaymentStatus.PAID, expected.getStatus());
        verify(screenReservationRepository, times(1)).findByUserId(user.getId());
        verify(jwtService,times(1)).extractUserName(token.substring(7));
        verify(userRepository,times(1)).findByEmail(email);

    }
    @Test
    public void findScreenReservationsByDate_whenExists_returnExpected() {
        //        arrange
        LocalDate reservationDate = LocalDate.of(2023, 5, 31);

        ScreenEvent event = new ScreenEvent(1L, "Event", LocalDate.now(), new Screen(1L, "Screen"));

        User reservationUser = new User();
        List<ScreenReservation> screenReservationList = new ArrayList<>();
        screenReservationList.add(new ScreenReservation(1L, reservationDate, event, null, 10, reservationUser, PaymentStatus.PAID));

        when(screenReservationRepository.findScreenReservationsByDate(reservationDate)).thenReturn(screenReservationList);

        //        act
        List<ScreenReservationSearchResponseDto> result = screenReservationServiceImpl.findScreenReservationsByDate(reservationDate);
        ScreenReservationSearchResponseDto expected = result.get(0);

        //        assert
        assertEquals(1, result.size());
        assertEquals(1, expected.getId());
        assertEquals(PaymentStatus.PAID, expected.getStatus());
        verify(screenReservationRepository, times(1)).findScreenReservationsByDate(reservationDate);
    }
    @Test
    public void findScreenReservationsByDate_whenNoExist_returnEmptyList() {
        //        arrange
        LocalDate reservationDate = LocalDate.of(2023, 5, 31);

        User reservationUser = new User();
        List<ScreenReservation> screenReservationList = new ArrayList<>();

        when(screenReservationRepository.findScreenReservationsByDate(reservationDate)).thenReturn(screenReservationList);

        //        act
        List<ScreenReservationSearchResponseDto> result = screenReservationServiceImpl.findScreenReservationsByDate(reservationDate);

        //        assert
        assertEquals(0, result.size());
        verify(screenReservationRepository, times(1)).findScreenReservationsByDate(reservationDate);
    }

}