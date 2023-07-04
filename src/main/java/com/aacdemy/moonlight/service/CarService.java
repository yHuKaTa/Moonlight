package com.aacdemy.moonlight.service;

import com.aacdemy.moonlight.dto.car.CarImportRequestDto;
import com.aacdemy.moonlight.dto.car.CarImportResponseDto;
import com.aacdemy.moonlight.entity.car.Car;
import com.aacdemy.moonlight.entity.car.CarCategory;
import com.aacdemy.moonlight.entity.car.enums.CarType;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import com.aacdemy.moonlight.entity.car.enums.CarType;
import io.jsonwebtoken.io.IOException;
import com.aacdemy.moonlight.entity.car.enums.CarType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/*
 * List of all cars
 * Car by name, by type, by make, by number of seats
 * List of all available cars by date
 */

public interface CarService {
    Car getCarById(Long id);

    Car findCarById(Long id);

    CarImportResponseDto addCar(CarImportRequestDto carImportRequest);

    List<Car> getAllCars();

    List<Car> getCarsByMake(String make);

    List<Car> getCarsByModel(String model);

    List<Car> getCarsByYear(int year);

    List<Car> findByCarCategoryCarSeats(int carSeats);

    List<Car> findByCarCategoryCarType(CarType type);

    List<Car> findAvailableCarsByDate(Date date);

    List<Car> findAvailableCarsByDateSeatsCarCategoryModel(Date date,
                                                           Optional<Integer> seatsOpt,
                                                           Optional<Long> carCategoryOpt,
                                                           Optional<String> mmakeOpt,
                                                           Optional<String> modelOpt);
    StreamingResponseBody findImagesByCarId(Long carId);

    void deleteById (Long id);
}
