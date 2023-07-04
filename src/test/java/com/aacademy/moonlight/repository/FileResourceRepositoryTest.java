package com.aacademy.moonlight.repository;

import com.aacdemy.moonlight.Application;
import com.aacdemy.moonlight.entity.car.Car;
import com.aacdemy.moonlight.entity.car.CarCategory;
import com.aacdemy.moonlight.entity.car.FileResource;
import com.aacdemy.moonlight.entity.car.enums.CarType;
import com.aacdemy.moonlight.repository.car.CarCategoryRepository;
import com.aacdemy.moonlight.repository.car.CarRepository;
import com.aacdemy.moonlight.repository.car.FileResourceRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ContextConfiguration(classes = Application.class)
@DirtiesContext
@ActiveProfiles("test")
public class FileResourceRepositoryTest {

    @Autowired
    private FileResourceRepository fileResourceRepository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private CarCategoryRepository carCategoryRepository;

    private Car car;
    private CarCategory carCategory;
    private FileResource fileResource;

    @BeforeEach
    public void setup() {
        CarType carType = CarType.SEDAN;

        carCategory = CarCategory.builder()
                .pricePerDay(15.0)
                .seats(4)
                .type(carType)
                .build();
        carCategoryRepository.save(carCategory);

        car = Car.builder()
                .make("Toyota")
                .model("Camry")
                .fileResources(new ArrayList<>())
                .carCategory(carCategory)
                .year(2022)
                .build();
        carRepository.save(car);

        fileResource = FileResource.builder()
                .imageName("image1.jpg")
                .dataValue(new byte[]{1, 2, 3})
                .car(car)
                .build();
        fileResourceRepository.save(fileResource);
    }

    @AfterEach
    public void cleanUp() {
        carRepository.delete(car);
        carCategoryRepository.delete(carCategory);
        fileResourceRepository.delete(fileResource);
    }

    @Test
    public void testFindByImageName() {

        Optional<FileResource> result = fileResourceRepository.findByImageName("image1.jpg");
        assertTrue(result.isPresent());
        Assertions.assertEquals("image1.jpg", result.get().getImageName());
    }

    @Test
    void testFindByCarId() {

        Car savedCar = carRepository.save(car);
        Long carId = savedCar.getId();

        List<FileResource> allFileResources = fileResourceRepository.findAll();

        List<FileResource> foundFileResources = allFileResources.stream()
                .filter(fileResource -> fileResource.getCar().getId().equals(carId))
                .toList();

        assertEquals(1, foundFileResources.size());
        FileResource foundFileResource = foundFileResources.get(0);
        assertEquals(carId, foundFileResource.getCar().getId());
        assertEquals("image1.jpg", foundFileResource.getImageName());

    }
}
