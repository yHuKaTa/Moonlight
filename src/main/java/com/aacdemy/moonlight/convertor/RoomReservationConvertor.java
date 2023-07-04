package com.aacdemy.moonlight.convertor;

import com.aacdemy.moonlight.dto.roomReservation.RoomReservationRequestDto;
import com.aacdemy.moonlight.dto.roomReservation.RoomReservationResponseDto;
import com.aacdemy.moonlight.entity.PaymentStatus;
import com.aacdemy.moonlight.entity.hotel.Room;
import com.aacdemy.moonlight.entity.hotel.RoomBedType;
import com.aacdemy.moonlight.entity.hotel.RoomReservation;
import com.aacdemy.moonlight.entity.user.User;
import com.aacdemy.moonlight.exception.EntityNotFoundException;
import com.aacdemy.moonlight.repository.hotel.RoomRepository;
import com.aacdemy.moonlight.repository.user.UserRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RoomReservationConvertor {

    private final UserRepository userRepository;

    private final RoomRepository roomRepository;

    public RoomReservation toRoomReservation(RoomReservationRequestDto roomReservationRequestDto,  String userEmail) throws ValidationException {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(()-> new EntityNotFoundException("User for reservation not found"));

        Room room = roomRepository.findById(roomReservationRequestDto.getRoomId())
                .orElseThrow(()-> new EntityNotFoundException("Room for reservation not found"));

        RoomReservation reservation = RoomReservation.builder()
                .startDate(roomReservationRequestDto.getStartDate())
                .endDate(roomReservationRequestDto.getEndDate())
                .user(user)
                .room(room)
                .adults(roomReservationRequestDto.getAdults())
                .paymentStatus(PaymentStatus.UNPAID)
                .children(roomReservationRequestDto.getChildren())
                .build();
        if (Objects.nonNull(roomReservationRequestDto.getBedType())) {
            if (roomReservationRequestDto.getBedType().equalsIgnoreCase("twin beds") ||
                    roomReservationRequestDto.getBedType().equalsIgnoreCase("twin_beds")) {
                reservation.setBedType(RoomBedType.TWIN_BEDS);
            } else if (roomReservationRequestDto.getBedType().equalsIgnoreCase("bedroom")) {
                reservation.setBedType(RoomBedType.BEDROOM);
            } else {
                throw new ValidationException("Invalid bed type! Please insert twin beds or bedroom");
            }
        }
        return reservation;
    }

    public RoomReservationResponseDto toRoomReservationResponseDto(RoomReservation reservationResponse){

        return RoomReservationResponseDto.builder()
                .startDate(reservationResponse.getStartDate())
                .endDate(reservationResponse.getEndDate())
                .price(reservationResponse.getRoom().getPrice())
                .fullPrice(reservationResponse.getFullPrice())
                .adults(reservationResponse.getAdults())
                .children(reservationResponse.getChildren())
                .roomType(reservationResponse.getRoom().getType())
                .build();
    }

}
