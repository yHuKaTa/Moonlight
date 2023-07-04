package com.aacdemy.moonlight.service.impl;

import com.aacdemy.moonlight.dto.car.CarCategoryRequestDto;
import com.aacdemy.moonlight.dto.car.CarCategoryResponseDto;
import com.aacdemy.moonlight.entity.car.CarCategory;
import com.aacdemy.moonlight.entity.car.enums.CarType;
import com.aacdemy.moonlight.exception.EntityNotFoundException;
import com.aacdemy.moonlight.repository.car.CarCategoryRepository;
import com.aacdemy.moonlight.repository.car.CarRepository;
import com.aacdemy.moonlight.service.CarCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarCategoryServiceImpl implements CarCategoryService {
    private final CarCategoryRepository carCategoryRepository;
    private final CarRepository carRepository;

    @Override
    public CarCategory findById(Long id) {
        return carCategoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Car category with ID " + id + " isn't found!"));
    }

    @Override
    public CarCategoryResponseDto addCategory(CarCategoryRequestDto carCategoryRequest) {
        CarCategory carCategory = CarCategory.builder()
                .type(CarType.valueOf(carCategoryRequest.getCarType().toUpperCase()))
                .seats(Integer.parseInt(carCategoryRequest.getSeats()))
                .pricePerDay(Double.parseDouble(carCategoryRequest.getPricePerDay()))
                .build();
        carCategoryRepository.save(carCategory);
        return CarCategoryResponseDto.builder()
                .id(String.valueOf(carCategory.getId()))
                .type(carCategory.getType().getCarType())
                .seats(String.valueOf(carCategory.getSeats()))
                .pricePerDay(String.valueOf(carCategory.getPricePerDay()))
                .build();
    }

    @Override
    public List<CarCategory> getAllCarCategories() {
        return carCategoryRepository.findAll();
    }

    @Override
    public List<CarCategory> getCarCategoriesByType(CarType type) {
        return carCategoryRepository.findByType(type);
    }

    @Override
    public List<CarCategory> getCarCategoriesBySeats(int seats) {
        return carCategoryRepository.findBySeats(seats);
    }

    @Override
    public List<CarCategory> getCarCategoriesByPricePerDay(double pricePerDay) {
        return carCategoryRepository.findByPricePerDay(pricePerDay);
    }
}
