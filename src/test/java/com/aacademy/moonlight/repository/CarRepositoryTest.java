package com.aacademy.moonlight.repository;

import com.aacdemy.moonlight.Application;
import com.aacdemy.moonlight.entity.car.Car;
import com.aacdemy.moonlight.entity.car.CarCategory;
import com.aacdemy.moonlight.entity.car.enums.CarType;
import com.aacdemy.moonlight.repository.car.CarCategoryRepository;
import com.aacdemy.moonlight.repository.car.CarRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;


@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ContextConfiguration(classes = Application.class)
@DirtiesContext
@ActiveProfiles("test")
public class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;
    @Autowired
    private CarCategoryRepository carCategoryRepository;

    private CarCategory category1;
    private CarCategory category2;
    private CarCategory category3;

    private Car car1;
    private Car car2;
    private Car car3;

    @BeforeEach
    public void init() {
        category1 = buildCarCategory(CarType.SEDAN, 3, 50.0);
        category2 = buildCarCategory(CarType.VAN, 6, 60.0);
        category3 = buildCarCategory(CarType.SPORT, 4, 51.0);

        carCategoryRepository.saveAll(List.of(category1, category2, category3));

        car1 = Car.builder()
                .year(2019)
                .make("Toyota")
                .model("Camry")
                .carCategory(category1)
                .build();
        carRepository.save(car1);

        car2 = Car.builder()
                .year(2019)
                .make("Honda")
                .model("Accord")
                .carCategory(category2)
                .build();
        carRepository.save(car2);

        car3 = Car.builder()
                .year(2020)
                .make("Toyota")
                .model("Camry")
                .carCategory(category3)
                .build();
        carRepository.save(car3);
    }
    @AfterEach
    public void cleanUp() {
        carRepository.deleteAll();
        carCategoryRepository.deleteAll();
    }

    @Test
    public void testFindByYear() {
        List<Car> cars = carRepository.findByYear(2019);
        Assertions.assertEquals(2, cars.size());
    }

    @Test
    public void testFindByMake() {
        List<Car> cars = carRepository.findByMake("Toyota");
        Assertions.assertEquals(2, cars.size());
    }

    @Test
    public void testFindByModel() {
        List<Car> cars = carRepository.findByModel("Camry");
        Assertions.assertEquals(2, cars.size());
    }

    @Test
    public void testFindByCarCategoryType() {
        List<Car> cars = carRepository.findByCarCategoryType(CarType.SEDAN);
        Assertions.assertEquals(1, cars.size());
    }

    @Test
    public void testFindByCarCategorySeats() {
        List<Car> cars = carRepository.findByCarCategorySeats(4);
        Assertions.assertEquals(1, cars.size());
    }

    @Test
    public void testFindByCarCategory() {
        List<Car> cars = carRepository.findByCarCategory(category1);
        Assertions.assertEquals(1, cars.size());
    }

    public static CarCategory buildCarCategory(CarType type, int seats, double pricePerDay) {
        return CarCategory.builder()
                .type(type)
                .seats(seats)
                .pricePerDay(pricePerDay)
                .build();
    }
}
