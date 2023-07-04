package com.aacdemy.moonlight.service.impl;

import com.aacdemy.moonlight.config.security.JwtService;
import com.aacdemy.moonlight.dto.restaurant.TableReservationRequestDto;
import com.aacdemy.moonlight.dto.restaurant.TableReservationResponseDto;
import com.aacdemy.moonlight.entity.restaurant.TableReservation;
import com.aacdemy.moonlight.entity.restaurant.TableRestaurant;
import com.aacdemy.moonlight.entity.restaurant.TableZone;
import com.aacdemy.moonlight.entity.user.User;
import com.aacdemy.moonlight.exception.EntityNotFoundException;
import com.aacdemy.moonlight.repository.restaurant.TableReservationRepository;
import com.aacdemy.moonlight.repository.restaurant.TableRestaurantRepository;
import com.aacdemy.moonlight.repository.user.UserRepository;
import com.aacdemy.moonlight.service.TableReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class TableReservationServiceImpl implements TableReservationService {

    private final TableReservationRepository tableReservationRepository;
    private final TableRestaurantRepository tableRestaurantRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public TableReservationServiceImpl(TableReservationRepository tableReservationRepository, TableRestaurantRepository tableRestaurantRepository, UserRepository userRepository, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.tableReservationRepository = tableReservationRepository;
        this.tableRestaurantRepository = tableRestaurantRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public List<TableReservation> findByCurrentlyLoggedUser(String token) {
        User user = userRepository.findByEmail(jwtService.extractUserName(token.substring(7))).get();
        return tableReservationRepository.findByUserId(user.getId());
    }
    @Override
    public List<TableReservation> findByUserId(Long id) {
        return tableReservationRepository.findByUserId(id);
    }

    @Override
    public List<TableReservation> findAll() {
        return tableReservationRepository.findAll();
    }

    @Override
    public List<TableRestaurant> findAvailableTableByDateTimeZoneIsSmokingTableIdPeople(
            LocalDate date,
            Optional<LocalTime> hour,
            Optional<TableZone> tableZone,
            Optional<Boolean> isSmoking,
            Optional<Long> tableId,
            Optional<Integer> seats) {

        if (hour.get().isBefore(LocalTime.parse("11:00")) || hour.get().isAfter(LocalTime.parse("22:59"))) {
            throw new IllegalArgumentException("Reservations can only be made between 11 AM and 11 PM");
        }

        return tableRestaurantRepository.findAvailableTableByDateTimeZoneIsSmokingTableIdPeople(
                date, hour, tableZone, isSmoking, tableId, seats
        );
    }

    public TableReservationResponseDto saveReservation (TableReservationRequestDto reservationRequest, String token) {

        String userEmail = jwtService.extractUserName(token);

        if (userEmail == null) {
            throw new IllegalArgumentException("Invalid token");
        }
        User existingUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        TableReservation reservation = new TableReservation();

        LocalDate currentDate = LocalDate.now();
        LocalDate reservationDate = reservationRequest.getDate();

        List<TableReservation> existingReservations = tableReservationRepository.findByUserAndDate(existingUser, reservationDate);

        LocalTime reservationTime = reservationRequest.getTime();

        if (reservationTime.isBefore(LocalTime.parse("11:00")) || reservationTime.isAfter(LocalTime.parse("22:59"))) {
            throw new IllegalArgumentException("Reservations can only be made between 11 AM and 11 PM");
        }

        if (!existingReservations.isEmpty()) {
            throw new IllegalArgumentException("User already has a reservation for the given date");
        }

        if (reservationRequest.getDate().isBefore(currentDate)) {
            throw new IllegalArgumentException("Cannot make a reservation for a past date");
        }
        if (reservationRequest.getPeople() <= 0) {
            throw new IllegalArgumentException("Invalid number of people for the reservation");
        }
        reservation.setDate(reservationRequest.getDate());
        reservation.setHour(reservationRequest.getTime());
        reservation.setCountPeople(reservationRequest.getPeople());

        TableRestaurant tableRestaurant = tableRestaurantRepository.findById(reservationRequest.getTableId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid tableId"));

        if (tableRestaurant.getSeats() < reservationRequest.getPeople()) {
            throw new IllegalArgumentException("The table does not have enough seats for the requested number of people");
        }

        reservation.setTable(tableRestaurant);
        reservation.setUser(existingUser);

        TableReservation savedReservation = tableReservationRepository.save(reservation);

        return convertToResponseDto(savedReservation);
    }

    public TableReservationResponseDto convertToResponseDto(TableReservation reservation) {

        if (reservation == null) {
            throw new IllegalArgumentException("Invalid reservation");
        }

        return TableReservationResponseDto.builder()
                .date(reservation.getDate())
                .hour(reservation.getHour())
                .tableRestaurant(reservation.getTable())
                .tableNumber(reservation.getTable().getTableNumber())
                .people(reservation.getCountPeople())
                .build();
    }
}
