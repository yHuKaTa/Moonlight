package com.aacdemy.moonlight.repository.car;

import com.aacdemy.moonlight.entity.car.Car;
import com.aacdemy.moonlight.entity.car.CarCategory;
import com.aacdemy.moonlight.entity.car.enums.CarType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByYear(int year);

    List<Car> findByMake(String make);

    List<Car> findByModel(String model);

    List<Car> findByCarCategoryType(CarType carType);

    List<Car> findByCarCategorySeats(int carSeats);


    List<Car> findByCarCategory(CarCategory carCategory);
}
