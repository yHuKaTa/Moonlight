package com.aacdemy.moonlight.service.impl;

import com.aacdemy.moonlight.convertor.RoomReservationConvertor;
import com.aacdemy.moonlight.dto.roomReservation.RoomReservationRequestDto;
import com.aacdemy.moonlight.dto.roomReservation.RoomReservationResponseDto;
import com.aacdemy.moonlight.dto.roomReservation.RoomReservationUpdateRequestDto;
import com.aacdemy.moonlight.dto.roomReservation.RoomReservationUpdateResponseDto;
import com.aacdemy.moonlight.entity.PaymentStatus;
import com.aacdemy.moonlight.entity.hotel.*;
import com.aacdemy.moonlight.entity.user.User;
import com.aacdemy.moonlight.exception.EntityNotFoundException;
import com.aacdemy.moonlight.repository.hotel.ReservationRepository;
import com.aacdemy.moonlight.repository.hotel.RoomRepository;
import com.aacdemy.moonlight.repository.user.UserRepository;
import com.aacdemy.moonlight.service.RoomReservationService;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.aacdemy.moonlight.util.EndDateToStartDate.isEndDateBeforeStartDate;


@Service
public class RoomReservationServiceImpl implements RoomReservationService {
    private final RoomRepository roomRepository;
    private final ReservationRepository roomReservationRepository;
    private final RoomReservationConvertor reservationConvertor;

    private final UserRepository userRepository;

    @Autowired
    public RoomReservationServiceImpl(RoomRepository roomRepository, ReservationRepository reservationRepository, RoomReservationConvertor reservationConvertor, UserRepository userRepository) {
        this.roomRepository = roomRepository;
        this.roomReservationRepository = reservationRepository;
        this.reservationConvertor = reservationConvertor;
        this.userRepository = userRepository;
    }

    @Override
    public RoomReservationResponseDto saveReservation(RoomReservationRequestDto reservation, String userEmail) throws ValidationException {
        Room room = roomRepository.findById(reservation.getRoomId())
                .orElseThrow(() -> new EntityNotFoundException("Room for reservation not found"));

        Long daysOfStay = ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate());

        if (reservation.getEndDate().isBefore(reservation.getStartDate())) {
            throw new ValidationException("End date of reservation must be after start date!");
        }

        if (daysOfStay <= 0) {
            throw new EntityNotFoundException("Room reservation is available for at least one day.");
        }

        if (isRoomAvailable(room, reservation.getEndDate(), reservation.getStartDate())) {
            throw new EntityNotFoundException("Room is not available for the requested dates.");
        }

        if (reservation.getAdults() > room.getPeople()) {
            throw new EntityNotFoundException("Room is not suitable for " + reservation.getAdults() + " adults");
        }

        RoomReservation reservationToBeSaved = reservationConvertor.toRoomReservation(reservation, userEmail);

        if (Objects.isNull(reservation.getBedType())) {
            reservationToBeSaved.setBedType(RoomBedType.TWIN_BEDS);
        } else if (reservation.getBedType().isEmpty()) {
            reservationToBeSaved.setBedType(RoomBedType.TWIN_BEDS);
        } else {
            reservationToBeSaved.setBedType(RoomBedType.BEDROOM);
        }

        reservationToBeSaved.setFullPrice(setFullPrice(
                reservationToBeSaved.getStartDate(),
                reservationToBeSaved.getEndDate(),
                reservationToBeSaved.getRoom().getId()));

        reservationToBeSaved = roomReservationRepository.save(reservationToBeSaved);

        return RoomReservationResponseDto.builder()
                .id(reservationToBeSaved.getId())
                .roomType(reservationToBeSaved.getRoom().getType())
                .adults(reservationToBeSaved.getAdults())
                .children(reservationToBeSaved.getChildren())
                .startDate(reservationToBeSaved.getStartDate())
                .endDate(reservationToBeSaved.getEndDate())
                .daysStaying(daysOfStay.intValue())
                .price(reservationToBeSaved.getRoom().getPrice())
                .fullPrice(reservationToBeSaved.getFullPrice())
                .build();
    }

    @Override
    public boolean isRoomAvailable(Room room, LocalDate startDate, LocalDate endDate) {
        return roomReservationRepository.existsByRoomAndStartDateLessThanEqualAndEndDateGreaterThanEqual(room, endDate, startDate);
    }
    public RoomReservationUpdateResponseDto updateRoomReservationPaymentStatus(RoomReservationUpdateRequestDto roomUpdate) {
        RoomReservation reservation = roomReservationRepository.findById(roomUpdate.getId()).orElseThrow(() -> new EntityNotFoundException("No existing reservation with provided ID"));
        if (roomUpdate.getStatus().equalsIgnoreCase(PaymentStatus.PAID.label)) {
            reservation.setPaymentStatus(PaymentStatus.PAID);
            roomReservationRepository.updatePaymentStatus(PaymentStatus.PAID, roomUpdate.getId());
        } else if (roomUpdate.getStatus().equalsIgnoreCase(PaymentStatus.UNPAID.label)) {
            reservation.setPaymentStatus(PaymentStatus.UNPAID);
            roomReservationRepository.updatePaymentStatus(PaymentStatus.UNPAID, roomUpdate.getId());
        }

        return RoomReservationUpdateResponseDto.builder()
                .id(reservation.getId())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .adults(reservation.getAdults())
                .children(reservation.getChildren())
                .room(reservation.getRoom())
                .status(reservation.getPaymentStatus())
                .type(reservation.getBedType())
                .build();
    }

    @Override
    public RoomReservationUpdateResponseDto updateRoomReservationParameters(RoomReservationUpdateRequestDto roomUpdate) {
        Room requiredRoom = null;
        RoomReservation reservation = roomReservationRepository.findById(roomUpdate.getId()).orElseThrow(() -> new EntityNotFoundException("No existing reservation with provided ID"));

        if (Objects.nonNull(roomUpdate.getStartDate())) {
            if (roomUpdate.getStartDate().isAfter(reservation.getEndDate()) ||
                    roomUpdate.getStartDate().isEqual(reservation.getEndDate())) {
                throw new ValidationException("Please provide valid start date that is before end date!");
            }
            if (isRoomNotOccupied(reservation.getId(), reservation.getRoom().getId(), roomUpdate.getStartDate(), reservation.getEndDate())) {
                reservation.setStartDate(roomUpdate.getStartDate());
                roomReservationRepository.updateStartDate(reservation.getStartDate(), reservation.getId());
            } else {
                throw new EntityNotFoundException("Sorry! We can't change start date, because the room is occupied for this date.");
            }
        }

        if (Objects.nonNull(roomUpdate.getEndDate())) {
            if (roomUpdate.getEndDate().isBefore(reservation.getStartDate()) ||
                    roomUpdate.getEndDate().isEqual(reservation.getStartDate())) {
                throw new ValidationException("Please provide valid end date that is after start date!");
            }
            if (isRoomNotOccupied(reservation.getId(), reservation.getRoom().getId(), roomUpdate.getStartDate(), reservation.getEndDate())) {
                reservation.setEndDate(roomUpdate.getEndDate());
                roomReservationRepository.updateEndDate(reservation.getEndDate(), reservation.getId());
            } else {
                throw new EntityNotFoundException("Sorry! We can't change end date, because the room is occupied for this date.");
            }
        }

        if (Objects.nonNull(roomUpdate.getRoomView()) && !(roomUpdate.getRoomView().isEmpty())) {
            if (roomUpdate.getRoomView().equalsIgnoreCase(RoomView.POOL.getName())) {
                List<Room> rooms = roomRepository.findByView(RoomView.POOL);
                for (Room room : rooms) {
                    if (room.getType().equals(reservation.getRoom().getType())
                            && room.getPeople() >= reservation.getAdults()
                            && isRoomNotOccupied(reservation.getId(), room.getId(), reservation.getStartDate(), reservation.getEndDate())) {
                        requiredRoom = room;
                        break;
                    }
                }
                if (Objects.isNull(requiredRoom) || Objects.isNull(requiredRoom.getId())) {
                    throw new EntityNotFoundException("Sorry! We can't change room, because all rooms with requested view are occupied.");
                }
                reservation.setRoom(requiredRoom);
                roomReservationRepository.updateRoomView(requiredRoom, reservation.getId());
            } else if (roomUpdate.getRoomView().equalsIgnoreCase(RoomView.GARDEN.getName())) {
                List<Room> rooms = roomRepository.findByView(RoomView.GARDEN);
                for (Room room : rooms) {
                    if (room.getType().equals(reservation.getRoom().getType())
                            && room.getPeople() >= reservation.getAdults()
                            && isRoomNotOccupied(reservation.getId(), room.getId(), reservation.getStartDate(), reservation.getEndDate())) {
                        requiredRoom = room;
                        break;
                    }
                }
                if (Objects.isNull(requiredRoom) || Objects.isNull(requiredRoom.getId())) {
                    throw new EntityNotFoundException("Sorry! We can't change room, because all rooms with requested view are occupied.");
                }
                reservation.setRoom(requiredRoom);
                roomReservationRepository.updateRoomView(requiredRoom, reservation.getId());
            } else if (roomUpdate.getRoomView().equalsIgnoreCase(RoomView.SEA.getName())) {
                List<Room> rooms = roomRepository.findByView(RoomView.SEA);
                for (Room room : rooms) {
                    if (room.getType().equals(reservation.getRoom().getType())
                            && room.getPeople() >= reservation.getAdults()
                            && isRoomNotOccupied(reservation.getId(), room.getId(), reservation.getStartDate(), reservation.getEndDate())) {
                        requiredRoom = room;
                        break;
                    }
                }
                if (Objects.isNull(requiredRoom) || Objects.isNull(requiredRoom.getId())) {
                    throw new EntityNotFoundException("Sorry! We can't change room, because all rooms with requested view are occupied.");
                }
                reservation.setRoom(requiredRoom);
                roomReservationRepository.updateRoomView(requiredRoom, reservation.getId());
            }
        }


        if (Objects.nonNull(roomUpdate.getRoomType()) && !(roomUpdate.getRoomType().isEmpty())) {
            if (roomUpdate.getRoomType().equalsIgnoreCase(RoomType.STANDARD.getName())) {
                List<Room> rooms = roomRepository.findByType(RoomType.STANDARD);
                for (Room room : rooms) {
                    if (isRoomNotOccupied(reservation.getId(), room.getId(), reservation.getStartDate(), reservation.getEndDate()) &&
                            room.getPeople() >= reservation.getAdults() &&
                            room.getView().getName().equalsIgnoreCase(reservation.getRoom().getView().getName())) {
                        requiredRoom = room;
                        reservation.setRoom(requiredRoom);
                        roomReservationRepository.updateRoomType(requiredRoom, reservation.getId());
                    }
                }
                if (Objects.isNull(requiredRoom)) {
                    throw new EntityNotFoundException("Sorry! We can't change room type, because all rooms with requested type are occupied.");
                }
            } else if (roomUpdate.getRoomType().equalsIgnoreCase(RoomType.STUDIO.getName())) {
                List<Room> rooms = roomRepository.findByType(RoomType.STUDIO);
                for (Room room : rooms) {
                    if (isRoomNotOccupied(reservation.getId(), room.getId(), reservation.getStartDate(), reservation.getEndDate()) &&
                            room.getPeople() >= reservation.getAdults() &&
                            room.getView().getName().equalsIgnoreCase(reservation.getRoom().getView().getName())) {
                        requiredRoom = room;
                        reservation.setRoom(requiredRoom);
                        roomReservationRepository.updateRoomType(requiredRoom, reservation.getId());
                    }
                }
                if (Objects.isNull(requiredRoom)) {
                    throw new EntityNotFoundException("Sorry! We can't change room type, because all rooms with requested type are occupied.");
                }
            } else if (roomUpdate.getRoomType().equalsIgnoreCase(RoomType.APARTMENT.getName())) {
                List<Room> rooms = roomRepository.findByType(RoomType.APARTMENT);
                for (Room room : rooms) {
                    if (isRoomNotOccupied(reservation.getId(), room.getId(), reservation.getStartDate(), reservation.getEndDate()) &&
                            room.getPeople() >= reservation.getAdults() &&
                            room.getView().getName().equalsIgnoreCase(reservation.getRoom().getView().getName())) {
                        requiredRoom = room;
                        reservation.setRoom(requiredRoom);
                        roomReservationRepository.updateRoomType(requiredRoom, reservation.getId());
                    }
                }
                if (Objects.isNull(requiredRoom)) {
                    throw new EntityNotFoundException("Sorry! We can't change room type, because all rooms with requested type are occupied.");
                }
            }
        }

        if (Objects.nonNull(roomUpdate.getAdults()) && roomUpdate.getAdults() != 0) {
            if (reservation.getRoom().getPeople() > roomUpdate.getAdults()) {
                reservation.setAdults(roomUpdate.getAdults());
                roomReservationRepository.updateAdults(reservation.getAdults(), reservation.getId());
            } else {
                List<Room> rooms = roomRepository.findByPeople(roomUpdate.getAdults());
                if (rooms.isEmpty()) {
                    throw new EntityNotFoundException("Sorry! We can't change adults, because there are no available rooms with requested value.");
                }
                for (Room room : rooms) {
                    if (isRoomNotOccupied(reservation.getId(), room.getId(), reservation.getStartDate(), reservation.getEndDate()) &&
                            room.getView().getName().equalsIgnoreCase(reservation.getRoom().getView().getName()) &&
                            room.getType().getName().equalsIgnoreCase(reservation.getRoom().getType().getName())) {
                        reservation.setAdults(roomUpdate.getAdults());
                        requiredRoom = room;
                        reservation.setRoom(room);
                        roomReservationRepository.updateRoomAndAdults(requiredRoom, reservation.getAdults(), reservation.getId());
                        break;
                    }
                }
                if (Objects.isNull(requiredRoom) || Objects.isNull(requiredRoom.getId()) || requiredRoom.getPeople() != roomUpdate.getAdults()) {
                    throw new EntityNotFoundException("Sorry! We can't change adults, because there are no available rooms with requested value.");
                }
            }
        }

        if (Objects.nonNull(roomUpdate.getChildren()) && roomUpdate.getChildren() != 0) {
            reservation.setChildren(roomUpdate.getChildren());
            roomReservationRepository.updateChildren(reservation.getChildren(), reservation.getId());
        }

        if (Objects.nonNull(roomUpdate.getBedType()) && !(roomUpdate.getBedType().isEmpty())) {
            if (roomUpdate.getBedType().equalsIgnoreCase(RoomBedType.BEDROOM.label)) {
                reservation.setBedType(RoomBedType.BEDROOM);
                roomReservationRepository.updateBedType(RoomBedType.BEDROOM, reservation.getId());
            } else if (roomUpdate.getBedType().equalsIgnoreCase("TWIN_BEDS") ||
                    roomUpdate.getBedType().equalsIgnoreCase(RoomBedType.TWIN_BEDS.label)) {
                reservation.setBedType(RoomBedType.TWIN_BEDS);
                roomReservationRepository.updateBedType(RoomBedType.TWIN_BEDS, reservation.getId());
            }
        }

//        if (Objects.isNull(roomUpdate.getStartDate())) {
//            throw new ValidationException("Please provide valid start date!");
//        } else if (Objects.isNull(roomUpdate.getEndDate())) {
//            throw new ValidationException("Please provide valid end date!");
//        } else if (Objects.isNull(roomUpdate.getAdults())) {
//            throw new ValidationException("Please provide positive value of adults!");
//        } else if (Objects.isNull(roomUpdate.getChildren())) {
//            throw new ValidationException("Please provide positive value of children or zero!");
//        }

        return RoomReservationUpdateResponseDto.builder()
                .id(reservation.getId())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .adults(reservation.getAdults())
                .children(reservation.getChildren())
                .room(reservation.getRoom())
                .status(reservation.getPaymentStatus())
                .type(reservation.getBedType())
                .build();
    }

    @Override
    public List<Room> getAvailableRooms(LocalDate startDate, LocalDate endDate, int adults, int children) {
        isEndDateBeforeStartDate(startDate, endDate);

        List<Room> rooms = new ArrayList<>(roomRepository.findAll());
        List<Room> reservedRooms = roomReservationRepository.getRoomByStartDateAndEndDate(startDate, endDate);

        rooms.removeIf(room -> room.getPeople() < adults);

        for (Room reserved : reservedRooms) {
            rooms.removeIf(room -> room.getRoomNumber() == reserved.getRoomNumber());
        }

        if (rooms.isEmpty()) {
            throw new EntityNotFoundException("There are no rooms available according to specify start date, end date and adults value!");
        }

        return rooms;
    }

    @Override
    public List<Room> getAlternativeRooms(LocalDate startDate, LocalDate endDate, int adults, int children) {
        if (adults > 46) {
            throw new EntityNotFoundException("There are not enough rooms for " + adults + " persons! Hotel is available for max 46 persons.");
        }
        isEndDateBeforeStartDate(startDate, endDate);

        int people = adults;
        List<Room> reservations = new ArrayList<>(roomReservationRepository.getRoomByStartDateAndEndDate(startDate, endDate));
        List<Room> requiredRooms = new ArrayList<>();
        List<Room> availableRooms = new ArrayList<>(roomRepository.findAll());


        for (Room reservation : reservations) {
            availableRooms.removeIf(room -> room.getRoomNumber() == reservation.getRoomNumber());
        }

        if (availableRooms.isEmpty()) {
            throw new EntityNotFoundException("There are no rooms available according to specify start and end date!");
        }

        for (Room room : availableRooms) {
            requiredRooms.add(room);
            people -= room.getPeople();
            if (people <= 0) {
                break;
            }
        }
        if (people > 0) {
            throw new EntityNotFoundException("There are no rooms available according to specify people value!");
        }

        return requiredRooms;
    }

    @Override
    public BigDecimal setFullPrice(LocalDate firstDate, LocalDate lastDate, Long roomId) {

        Room room = roomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Room with id " + roomId + " not found"));

        BigDecimal dailyPrice = room.getPrice();

        long daysBetween = ChronoUnit.DAYS.between(firstDate, lastDate);

        if (daysBetween <= 0) {
            daysBetween = 1;
        }

        return dailyPrice.multiply(BigDecimal.valueOf(daysBetween));
    }

    @Override
    public List<RoomReservationResponseDto> findByCurrentlyLoggedUser(String email) {
        User currentUser = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return roomReservationRepository.findByUser(currentUser)
                .stream()
                .map(reservationConvertor::toRoomReservationResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomReservationResponseDto> findByUserId(Long id) {
        User currentUser = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return roomReservationRepository.findByUser(currentUser)
                .stream()
                .map(reservationConvertor::toRoomReservationResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomReservationResponseDto> findAll() {
        return roomReservationRepository.findAll()
                .stream()
                .map(reservationConvertor::toRoomReservationResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomReservationResponseDto> findByStartDateAndEndDate(LocalDate startDate, LocalDate endDate) {
        return roomReservationRepository.getRoomReservationsByStartDateAndEndDate(startDate, endDate)
                .stream()
                .map(reservationConvertor::toRoomReservationResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomReservationResponseDto> findByStartDateAndEndDateForUser(LocalDate startDate, LocalDate endDate, String email) {
        User user = userRepository.findByEmail(email).get();
        return roomReservationRepository.getRoomReservationsByStartDateAndEndDateAndUser(startDate, endDate, user)
                .stream()
                .map(reservationConvertor::toRoomReservationResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public RoomReservationResponseDto findById(Long id) {
        return reservationConvertor.toRoomReservationResponseDto(
                roomReservationRepository.findById(id)
                        .orElseThrow(()-> new EntityNotFoundException("Room reservation not found")));
    }

    @Override
    public void deleteRoomReservation(Long id) throws EntityNotFoundException {
        Optional<RoomReservation> roomReservation = this.roomReservationRepository.findById(id);
        if (!roomReservation.isPresent()) {
            throw new EntityNotFoundException("Reservation not found");
        }
        this.roomReservationRepository.deleteById(id);
    }

    public boolean isRoomNotOccupied(Long id, Long roomId, LocalDate startDate, LocalDate endDate) {
        return roomReservationRepository.isRoomNotOccupied(startDate, endDate, roomId, id);
    }
}