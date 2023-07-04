package com.aacdemy.moonlight.service.impl;

import com.aacdemy.moonlight.dto.car.CarTransferRequestDto;
import com.aacdemy.moonlight.dto.car.CarTransferResponseDto;
import com.aacdemy.moonlight.entity.PaymentStatus;
import com.aacdemy.moonlight.entity.car.Car;
import com.aacdemy.moonlight.entity.car.CarTransfer;
import com.aacdemy.moonlight.entity.user.User;
import com.aacdemy.moonlight.exception.EntityNotFoundException;
import com.aacdemy.moonlight.repository.car.CarRepository;
import com.aacdemy.moonlight.repository.car.CarTransferRepository;
import com.aacdemy.moonlight.repository.user.UserRepository;
import com.aacdemy.moonlight.service.CarTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class CarTransferServiceImpl implements CarTransferService {

    private final CarTransferRepository carTransferRepository;

    private final UserRepository userRepository;

    private final CarRepository carRepository;

    @Autowired
    public CarTransferServiceImpl(CarTransferRepository carTransferRepository, UserRepository userRepository, CarRepository carRepository) {
        this.carTransferRepository = carTransferRepository;
        this.userRepository = userRepository;
        this.carRepository = carRepository;
    }

    @Override
    public CarTransferResponseDto addCarTransfer(CarTransferRequestDto carTransfer, String username) throws ParseException {
        Car requiredCar = null;
//        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD", Locale.ROOT);
        Date date = Date.from(carTransfer.getDate().atTime(
                LocalTime.now()).toInstant(ZoneOffset.ofHours(3)));

        List<Car> cars = carTransferRepository.findAvailableCarsByDate(date);
        if (cars.isEmpty()) {
            throw new EntityNotFoundException("No available cars for this date!");
        }

            for (Car car : cars) {
                if (car.getId() == Long.parseLong(carTransfer.getCarId())) {
                    requiredCar = car;
                }
            }

        if (Objects.isNull(requiredCar)) {
            throw new EntityNotFoundException("Car with ID " + carTransfer.getCarId() + " isn't found!");
        }

        CarTransfer carTr = CarTransfer.builder()
                .car(requiredCar)
                .user(
                        userRepository.findByEmail(username)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"))
                )
                .date(date)
                .price(BigDecimal.valueOf(Objects.requireNonNull(requiredCar).getCarCategory().getPricePerDay()))
                .status(PaymentStatus.UNPAID)
                .build();

        CarTransfer generatedTransfer = carTransferRepository.save(carTr);

        return CarTransferResponseDto.builder()
                .id(String.valueOf(generatedTransfer.getId()))
                .make(generatedTransfer.getCar().getMake())
                .model(generatedTransfer.getCar().getModel())
                .seats(String.valueOf(generatedTransfer.getCar().getCarCategory().getSeats()))
                .date(carTransfer.getDate())
                .price(String.valueOf(generatedTransfer.getPrice()))
                .build();
    }

    @Override
    public List<CarTransfer> getAllTransfers() {
        return carTransferRepository.findAll();
    }

    @Override
    public CarTransfer getTransferById(Long id) {
        return carTransferRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Car Transfer not found"));
    }

    @Override
    public List<CarTransfer> getTransfersByUserEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return carTransferRepository.findByUser(user);
    }

    @Override
    public List<CarTransfer> getTransfersByCar(String car) {
        List<CarTransfer> transfers = new ArrayList<>();
        List<Car> cars = carRepository.findByMake(car);
        if (cars.isEmpty()) {
            cars = carRepository.findByModel(car);
        }
        for (Car searchedCar : cars) {
            transfers.addAll(carTransferRepository.findByCar(searchedCar));
        }
        return transfers;
    }

    @Override
    public String deleteTransferById(Long id) {
        if (carTransferRepository.existsById(id)) {
            carTransferRepository.deleteById(id);
            return "Successfully deleted car transfer!";
        } else {
            return null;
        }
    }
}