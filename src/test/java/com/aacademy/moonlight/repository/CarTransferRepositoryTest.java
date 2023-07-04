package com.aacademy.moonlight.repository;

import com.aacdemy.moonlight.Application;
import com.aacdemy.moonlight.entity.PaymentStatus;
import com.aacdemy.moonlight.entity.car.Car;
import com.aacdemy.moonlight.entity.car.CarCategory;
import com.aacdemy.moonlight.entity.car.CarTransfer;
import com.aacdemy.moonlight.entity.car.FileResource;
import com.aacdemy.moonlight.entity.car.enums.CarType;
import com.aacdemy.moonlight.entity.user.User;
import com.aacdemy.moonlight.entity.user.UserRole;
import com.aacdemy.moonlight.repository.car.CarCategoryRepository;
import com.aacdemy.moonlight.repository.car.CarRepository;
import com.aacdemy.moonlight.repository.car.CarTransferRepository;
import com.aacdemy.moonlight.repository.user.RoleRepository;
import com.aacdemy.moonlight.repository.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ContextConfiguration(classes = Application.class)
@DirtiesContext
@ActiveProfiles("test")
public class CarTransferRepositoryTest {

    @Autowired
    private CarTransferRepository carTransferRepository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private CarCategoryRepository carCategoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private UserRole userRole;
    private User user;
    private CarCategory carCategory;
    private Car car;

    @BeforeEach
    public void setup() {

        userRole = new UserRole();
        userRole.setUserRole("ROLE");
        roleRepository.save(userRole);

        user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@example.com")
                .phoneNumber("0843984398923")
                .password("Password1123!")
                .userRole(userRole)
                .passportID("1234567899")
                .modifiedDate(new Date())
                .createdDate(new Date())
                .enabled(true)
                .build();
        userRepository.save(user);

        carCategory = CarCategory.builder()
                .seats(4)
                .type(CarType.VAN)
                .pricePerDay(20.0)
                .build();
        carCategoryRepository.save(carCategory);

        car = Car.builder()
                .make("Toyota")
                .model("Camry")
                .year(2022)
                .carCategory(carCategory)
                .fileResources(new ArrayList<>())
                .build();
        carRepository.save(car);
    }

    @AfterEach
    public void cleanup() {
        carTransferRepository.deleteAll();
        carRepository.deleteAll();
        carCategoryRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    public void testFindAvailableCarsByDate() {
        CarTransfer carTransfer = CarTransfer.builder()
                .date(new Date())
                .price(BigDecimal.valueOf(1000))
                .car(car)
                .status(PaymentStatus.PAID)
                .user(user)
                .build();
        carTransferRepository.save(carTransfer);

        List<Car> availableCars = carTransferRepository.findAvailableCarsByDate(new Date());

        Assertions.assertTrue(availableCars.isEmpty());
    }

//    @Test
//    public void testFindAvailableCarsByDateSeatsCarCategoryIdMakeModel() {
//        CarTransfer carTransfer = CarTransfer.builder()
//                .date(new Date())
//                .price(BigDecimal.valueOf(1000))
//                .car(car)
//                .status(PaymentStatus.PAID)
//                .user(user)
//                .build();
//        carTransferRepository.save(carTransfer);
//
//        Date date = new Date();
//        Optional<Integer> seats = Optional.of(4);
//        Optional<Long> carCategoryId = Optional.of(1L);
//        Optional<String> make = Optional.of("Toyota");
//        Optional<String> model = Optional.of("Camry");
//
//        List<Car> availableCars = carTransferRepository.findAvailableCarsByDateSeatsCarCategoryIdMakeModel(
//                date, seats, carCategoryId, make, model);
//
//        Assertions.assertEquals(1, availableCars.size());
//        Assertions.assertEquals(car.getMake(), availableCars.get(0).getMake());
//        Assertions.assertEquals(car.getModel(), availableCars.get(0).getModel());
//    }

    @Test
    public void testFindByUser() {
        CarTransfer carTransfer = CarTransfer.builder()
                .date(new Date())
                .price(BigDecimal.valueOf(1000))
                .car(car)
                .status(PaymentStatus.PAID)
                .user(user)
                .build();
        carTransferRepository.save(carTransfer);

        List<CarTransfer> carTransfers = carTransferRepository.findByUser(user);

        Assertions.assertFalse(carTransfers.isEmpty());
    }

    @Test
    public void testFindByCar() {
        CarTransfer carTransfer = CarTransfer.builder()
                .date(new Date())
                .price(BigDecimal.valueOf(1000))
                .car(car)
                .status(PaymentStatus.PAID)
                .user(user)
                .build();
        carTransferRepository.save(carTransfer);

        List<CarTransfer> carTransfers = carTransferRepository.findByCar(car);

        Assertions.assertFalse(carTransfers.isEmpty());
    }
}


