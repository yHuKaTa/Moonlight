package com.aacademy.moonlight.repository;

import com.aacdemy.moonlight.Application;
import com.aacdemy.moonlight.entity.car.CarCategory;
import com.aacdemy.moonlight.entity.car.enums.CarType;
import com.aacdemy.moonlight.repository.car.CarCategoryRepository;
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
public class CarCategoryRepositoryTest {

    @Autowired
    private CarCategoryRepository carCategoryRepository;

    private CarCategory carCategory1;
    private CarCategory carCategory2;
    private CarType carType;
    private CarType carType2;

    @BeforeEach
    public void init() {
        carType = CarType.SEDAN;
        carType2 = CarType.VAN;

        carCategory1 = CarCategory.builder()
                .pricePerDay(15.0)
                .seats(4)
                .type(carType)
                .build();
        carCategory2 = CarCategory.builder()
                .pricePerDay(20.0)
                .seats(5)
                .type(carType2)
                .build();

        carCategoryRepository.saveAll(List.of(carCategory1, carCategory2));
    }
    @AfterEach
    public void cleanUp() {
        carCategoryRepository.deleteAll();
    }

    @Test
    public void testFindBySeats() {
        List<CarCategory> result = carCategoryRepository.findBySeats(4);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(carCategory1, result.get(0));
    }

    @Test
    public void testFindByPricePerDay() {
        List<CarCategory> categories = carCategoryRepository.findByPricePerDay(20.0);
        Assertions.assertEquals(1, categories.size());
    }

    @Test
    public void testFindByType() {
        List<CarCategory> categories = carCategoryRepository.findByType(CarType.SEDAN);
        Assertions.assertEquals(1, categories.size());
    }
}