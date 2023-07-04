package com.aacdemy.moonlight.service;

import com.aacdemy.moonlight.dto.roomReservation.RoomReservationRequestDto;
import com.aacdemy.moonlight.dto.roomReservation.RoomReservationResponseDto;
import com.aacdemy.moonlight.dto.roomReservation.RoomReservationUpdateRequestDto;
import com.aacdemy.moonlight.dto.roomReservation.RoomReservationUpdateResponseDto;
import com.aacdemy.moonlight.entity.hotel.Room;
import com.aacdemy.moonlight.exception.EntityNotFoundException;
import jakarta.xml.bind.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface RoomReservationService {

    List<RoomReservationResponseDto> findByCurrentlyLoggedUser(String token);

    List<RoomReservationResponseDto> findByUserId(Long id);

    List<RoomReservationResponseDto> findAll();

    List<RoomReservationResponseDto> findByStartDateAndEndDate(LocalDate startDate, LocalDate endDate);

    List<RoomReservationResponseDto> findByStartDateAndEndDateForUser(LocalDate startDate, LocalDate endDate, String email);

    RoomReservationResponseDto findById(Long id);

    void deleteRoomReservation(Long id) throws EntityNotFoundException;

    RoomReservationResponseDto saveReservation(RoomReservationRequestDto reservation, String userEmail) throws ValidationException;

    BigDecimal setFullPrice(LocalDate firstDate, LocalDate lastDate, Long roomId);

    boolean isRoomAvailable(Room room, LocalDate startDate, LocalDate endDate);

    List<Room> getAvailableRooms(LocalDate startDate, LocalDate endDate, int adults, int children);

    List<Room> getAlternativeRooms(LocalDate startDate, LocalDate endDate, int adults, int children);

    RoomReservationUpdateResponseDto updateRoomReservationPaymentStatus(RoomReservationUpdateRequestDto roomUpdate);

    RoomReservationUpdateResponseDto updateRoomReservationParameters(RoomReservationUpdateRequestDto roomUpdate);
}