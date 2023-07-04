package com.aacdemy.moonlight.service.impl;

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
import com.aacdemy.moonlight.service.ScreenEventService;
import com.aacdemy.moonlight.service.ScreenReservationService;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class ScreenReservationServiceImpl implements ScreenReservationService {
    private final ScreenEventService screenEventService;

    private final ScreenSeatsRepository screenSeatsRepository;

    private final ScreenReservationRepository screenReservationRepository;

    private final UserServiceImpl userService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Autowired
    public ScreenReservationServiceImpl(ScreenEventService screenEventService, ScreenSeatsRepository screenSeatsRepository, ScreenReservationRepository screenReservationRepository, UserServiceImpl userService, JwtService jwtService, UserRepository userRepository) {
        this.screenEventService = screenEventService;
        this.screenSeatsRepository = screenSeatsRepository;
        this.screenReservationRepository = screenReservationRepository;
        this.userService = userService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    public ScreenReservationResponseDto addReservation(ScreenReservationRequestDto screenReservationRequestDto, String userName) {
        User user = userService.getByEmail(userName);
        Set<ScreenSeat> requiredSeats = new HashSet<>(21);
        ScreenEvent requiredEvent;
        List<ScreenEvent> events = new ArrayList<>(screenEventService.findAvailableEventsByDate(screenReservationRequestDto.getDateOfEvent()));

        events.removeIf(screenEvent -> screenEvent.getId().compareTo(screenReservationRequestDto.getEventId()) != 0);

        if (events.isEmpty()) {
            throw new EntityNotFoundException("Event not found");
        } else {
            requiredEvent = events.listIterator().next();
        }

        for (ScreenSeat reservedSeat : findReservedSeats(requiredEvent.getDateEvent(), requiredEvent.getScreen())) {
            for (Integer seat : screenReservationRequestDto.getSeatNumbers()) {
                if (reservedSeat.getSeatPosition() == seat) {
                    throw new ValidationException("Seat number " + seat + " are already reserved!");
                }
            }
        }

        for (Integer requiredSeat : screenReservationRequestDto.getSeatNumbers()) {
            requiredSeats.add(screenSeatsRepository.findBySeatPositionAndScreen(requiredSeat, requiredEvent.getScreen()).orElseThrow(() -> new EntityNotFoundException("Seat with number " + requiredSeat + " isn't found!")));
        }

        ScreenReservation reservation = screenReservationRepository.save(ScreenReservation.builder()
                .date(LocalDate.now())
                .event(requiredEvent)
                .seats(requiredSeats)
                .price(requiredSeats.size() * 5)
                .user(user)
                .status(PaymentStatus.UNPAID)
                .build());


        return ScreenReservationResponseDto.builder()
                .event(requiredEvent)
                .reservedSeats(requiredSeats)
                .build();
    }

    public Set<ScreenSeat> findReservedSeats(LocalDate date, Screen screen) {
        List<ScreenReservation> reservations = screenReservationRepository.findByDateAndSeatsScreen(date, screen);
        Set<ScreenSeat> reservedSeats = new HashSet<>(21);
        for (ScreenReservation reservation : reservations) {
            reservedSeats.addAll(reservation.getSeats());
        }
        return reservedSeats;
    }

    @Override
    public List<ScreenSeat> getFreeSeatsForEvent(Long eventId) {
        ScreenEvent event = screenEventService.findById(eventId);
        Set<ScreenSeat> reservedSeats = findReservedSeats(event.getDateEvent(), event.getScreen());
        List<ScreenSeat> allSeats = screenSeatsRepository.findByScreen(event.getScreen());

        List<ScreenSeat> freeSeats = new ArrayList<>();
        for (ScreenSeat seat : allSeats) {
            if (!reservedSeats.contains(seat)) {
                freeSeats.add(seat);
            }
        }
        return freeSeats;
    }

    @Override
    public List<ScreenReservationSearchResponseDto> findAll() {
        List<ScreenReservationSearchResponseDto> result = new ArrayList<>();
        List<ScreenReservation> screenReservationList = screenReservationRepository.findAll();

        for (ScreenReservation sr : screenReservationList) {
            result.add(new ScreenReservationSearchResponseDto(sr.getId(), sr.getDate(), sr.getEvent().getEvent(), sr.getEvent().getDateEvent(), sr.getEvent().getScreen().getName(), sr.getStatus(), sr.getUser().getId()));
        }
        return result;
    }
    @Override
    public List<ScreenReservationSearchResponseDto> findByCurrentlyLoggedUser(String token)
    {
        User user = userRepository.findByEmail(jwtService.extractUserName(token.substring(7))).get();

        List<ScreenReservationSearchResponseDto> result = new ArrayList<>();
        List<ScreenReservation> screenReservationList = screenReservationRepository.findByUserId(user.getId());

        for (ScreenReservation sr:screenReservationList) {
            result.add(new ScreenReservationSearchResponseDto(sr.getId(), sr.getDate(), sr.getEvent().getEvent(), sr.getEvent().getDateEvent(), sr.getEvent().getScreen().getName(), sr.getStatus(), sr.getUser().getId()));
        }
        return result;
    }
    @Override
    public List<ScreenReservationSearchResponseDto> findScreenReservationsByDate(LocalDate date)
    {
        List<ScreenReservationSearchResponseDto> result = new ArrayList<>();
        List<ScreenReservation> screenReservationList = screenReservationRepository.findScreenReservationsByDate(date);

        for (ScreenReservation sr:screenReservationList) {
            result.add(new ScreenReservationSearchResponseDto(sr.getId(), sr.getDate(), sr.getEvent().getEvent(), sr.getEvent().getDateEvent(), sr.getEvent().getScreen().getName(), sr.getStatus(), sr.getUser().getId()));
        }
        return result;
    }
}
