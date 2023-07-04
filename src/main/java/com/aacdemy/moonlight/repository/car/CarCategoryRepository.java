package com.aacdemy.moonlight.repository.car;

import com.aacdemy.moonlight.entity.car.CarCategory;
import com.aacdemy.moonlight.entity.car.enums.CarType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarCategoryRepository extends JpaRepository<CarCategory, Long> {

    List<CarCategory> findBySeats(int seats);
    List<CarCategory> findByPricePerDay(double pricePerDay);
    List<CarCategory> findByType(CarType type);
}
