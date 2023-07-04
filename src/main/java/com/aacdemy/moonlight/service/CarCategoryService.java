package com.aacdemy.moonlight.service;

import com.aacdemy.moonlight.dto.car.CarCategoryRequestDto;
import com.aacdemy.moonlight.dto.car.CarCategoryResponseDto;
import com.aacdemy.moonlight.entity.car.CarCategory;
import com.aacdemy.moonlight.entity.car.enums.CarType;

import java.util.List;

public interface CarCategoryService {
    CarCategory findById(Long id);
    CarCategoryResponseDto addCategory(CarCategoryRequestDto carCategoryRequest);
    List<CarCategory> getAllCarCategories();
    List<CarCategory> getCarCategoriesByType(CarType type);
    List<CarCategory> getCarCategoriesBySeats(int seats);

    List<CarCategory> getCarCategoriesByPricePerDay(double pricePerDay);
}