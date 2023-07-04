package com.aacdemy.moonlight.service;

import com.aacdemy.moonlight.dto.screen.ScreenReservationRequestDto;
import com.aacdemy.moonlight.dto.screen.ScreenReservationResponseDto;
import com.aacdemy.moonlight.dto.screen.ScreenReservationSearchResponseDto;
import com.aacdemy.moonlight.entity.screen.ScreenSeat;

import java.time.LocalDate;
import java.util.List;

public interface ScreenReservationService {
    ScreenReservationResponseDto addReservation(ScreenReservationRequestDto screenReservationRequestDto, String userName);

    List<ScreenSeat> getFreeSeatsForEvent(Long eventId);

    List<ScreenReservationSearchResponseDto> findAll();
    List<ScreenReservationSearchResponseDto> findByCurrentlyLoggedUser(String token);
    List<ScreenReservationSearchResponseDto> findScreenReservationsByDate(LocalDate date);
}
