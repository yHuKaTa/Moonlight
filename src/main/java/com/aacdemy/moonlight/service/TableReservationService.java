package com.aacdemy.moonlight.service;

import com.aacdemy.moonlight.dto.restaurant.TableReservationRequestDto;
import com.aacdemy.moonlight.dto.restaurant.TableReservationResponseDto;

import org.springframework.stereotype.Service;
import com.aacdemy.moonlight.entity.restaurant.TableReservation;
import com.aacdemy.moonlight.entity.restaurant.TableRestaurant;
import com.aacdemy.moonlight.entity.restaurant.TableZone;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public interface TableReservationService {

    List<TableReservation> findByCurrentlyLoggedUser(String token);

    List<TableReservation> findByUserId(Long id);

    List<TableReservation> findAll();

    List<TableRestaurant> findAvailableTableByDateTimeZoneIsSmokingTableIdPeople(LocalDate date,
                                                                                 Optional<LocalTime> hour,
                                                                                 Optional<TableZone> tableZone,
                                                                                 Optional<Boolean> isSmoking,
                                                                                 Optional<Long> tableId,
                                                                                 Optional<Integer> seats);

    TableReservationResponseDto saveReservation(TableReservationRequestDto reservationRequest, String token);
}


