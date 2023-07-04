package com.aacdemy.moonlight.service;

import com.aacdemy.moonlight.dto.car.CarImportRequestDto;
import com.aacdemy.moonlight.dto.car.CarImportResponseDto;
import com.aacdemy.moonlight.entity.car.Car;
import com.aacdemy.moonlight.entity.car.CarCategory;
import com.aacdemy.moonlight.entity.car.FileResource;
import com.aacdemy.moonlight.entity.car.enums.CarType;
import com.aacdemy.moonlight.exception.EntityNotFoundException;
import com.aacdemy.moonlight.repository.car.CarRepository;
import com.aacdemy.moonlight.repository.car.CarTransferRepository;
import com.aacdemy.moonlight.service.impl.CarServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import static java.util.List.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@ExtendWith({MockitoExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
public class CarServiceTest {
    @Mock
    private FileResourceService fileResourceService;

    private CarService carService;
    @Mock
    private CarRepository carRepository;
    @Mock
    private CarTransferRepository carTransferRepository;

    @Mock
    private CarCategoryService carCategoryService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        carService = new CarServiceImpl(carRepository, carTransferRepository, carCategoryService, fileResourceService);
    }

    @Test
    void getCarByIdShouldReturnCar() {
        Car car = new Car();
        car.setId(1L);
        when(carRepository.getReferenceById(1L)).thenReturn(car);
        assertEquals(car, carService.getCarById(1L));
    }

    @Test
    void findCarByIdShouldReturnCar() {
        Car car = new Car();
        car.setId(1L);
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        assertEquals(car, carService.findCarById(1L));
    }

    @Test
    void findCarByIdShouldThrowEntityNotFoundException() {
        when(carRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> carService.findCarById(1L));
    }

    @Test
    void addCarShouldAddNewCar() {
        CarCategory category = new CarCategory();
        category.setType(CarType.SEDAN);
        category.setSeats(4);
        category.setPricePerDay(100.0);
        when(carCategoryService.findById(1L)).thenReturn(category);

        CarImportRequestDto requestDto = new CarImportRequestDto();
        requestDto.setMake("Toyota");
        requestDto.setModel("Corolla");
        requestDto.setYear("2021");
        requestDto.setCarCategoryId("1");

        Car car = new Car();
        car.setYear(2021);
        when(carRepository.save(any())).thenReturn(car);

        CarImportResponseDto responseDto = carService.addCar(requestDto);

        assertEquals(requestDto.getMake(), responseDto.getMake());
        assertEquals(requestDto.getModel(), responseDto.getModel());
        assertEquals(Integer.valueOf(requestDto.getYear()), Integer.valueOf(responseDto.getYear()));
        assertEquals("Sedan", responseDto.getType());
        assertEquals(4, Integer.parseInt(responseDto.getSeats()));
        assertEquals(Double.valueOf(100.0), Double.valueOf(responseDto.getPricePerDay()));
        assertIsInstanceOf(CarImportResponseDto.class, responseDto);

    }

    @Test
    void getAllCarsShouldReturnAllCars() {
        Car car1 = new Car();
        car1.setId(1L);
        Car car2 = new Car();
        car2.setId(2L);
        List<Car> cars = new ArrayList<>(Arrays.asList(car1, car2));

        when(carRepository.findAll()).thenReturn(cars);

        carService = new CarServiceImpl(carRepository, null, null, null);

        List<Car> result = carService.getAllCars();

        assertEquals(cars, result);
    }

    @Test
    void getCarsByMakeShouldReturnCarsWithMatchingMake() {
        Car car1 = new Car();
        car1.setId(1L);
        car1.setMake("Toyota");
        Car car2 = new Car();
        car2.setId(2L);
        car2.setMake("Honda");
        when(carRepository.findByMake("Toyota")).thenReturn(of(car1));
        carService = new CarServiceImpl(carRepository, null, null, null);
        List<Car> result = carService.getCarsByMake("Toyota");
        assertEquals(of(car1), result);
    }

    @Test
    void getCarsByModelShouldReturnCarsWithMatchingModel() {
        Car car1 = new Car();
        car1.setId(1L);
        car1.setModel("Corolla");
        Car car2 = new Car();
        car2.setId(2L);
        car2.setModel("Civic");

        when(carRepository.findByModel("Corolla")).thenReturn(of(car1));

        carService = new CarServiceImpl(carRepository, null, null, null);

        List<Car> result = carService.getCarsByModel("Corolla");

        assertEquals(of(car1), result);
    }

    @Test
    void getCarsByYearShouldReturnCarsWithMatchingYear() {
        Car car1 = new Car();
        car1.setId(1L);
        car1.setYear(2019);
        Car car2 = new Car();
        car2.setId(2L);
        car2.setYear(2021);

        when(carRepository.findByYear(2021)).thenReturn(of(car2));

        carService = new CarServiceImpl(carRepository, null, null, null);

        var result = carService.getCarsByYear(2021);

        assertEquals(of(car2), result);
    }

    @Test
    void findByCarCategoryCarSeatsShouldReturnCarsWithMatchingSeats() {
        CarCategory category = new CarCategory();
        category.setSeats(4);
        when(carCategoryService.findById(1L)).thenReturn(category);

        Car car1 = new Car();
        car1.setId(1L);
        car1.setMake("Toyota");
        car1.setModel("Corolla");
        car1.setYear(2020);
        car1.setCarCategory(category);

        Car car2 = new Car();
        car2.setId(2L);
        car2.setMake("Honda");
        car2.setModel("Civic");
        car2.setYear(2021);
        car2.setCarCategory(category);
        List<Car> cars = Arrays.asList(car1, car2);
        when(carRepository.findByCarCategorySeats(4)).thenReturn(cars);
        List<Car> result = carService.findByCarCategoryCarSeats(4);
        assertEquals(2, result.size());
        assertTrue(result.contains(car1));
        assertTrue(result.contains(car2));
    }

    @Test
    void testFindByCarCategoryCarType() {
        CarCategory sedan4Door = CarCategory.builder().type(CarType.SEDAN).seats(4).build();
        Car toyotaCamry = Car.builder().make("Toyota").model("Camry").year(2021).carCategory(sedan4Door).build();
        List<Car> expected = Collections.singletonList(toyotaCamry);
        when(carRepository.findByCarCategoryType(CarType.SEDAN)).thenReturn(expected);
        List<Car> result = carService.findByCarCategoryCarType(CarType.SEDAN);
        assertEquals(expected, result);
    }

    @Test
    void testFindAvailableCarsByDate() {
        Date date = new Date();
        CarCategory van = CarCategory.builder().type(CarType.VAN).seats(5).build();
        Car hondaCrv = Car.builder().make("Honda").model("CR-V").year(2022).carCategory(van).build();
        when(carTransferRepository.findAvailableCarsByDate(date)).thenReturn(Collections.singletonList(hondaCrv));
        List<Car> expected = Collections.singletonList(hondaCrv);
        List<Car> result = carService.findAvailableCarsByDate(date);
        assertEquals(expected, result);
    }

    @Test
    void testFindImagesByCarId() throws Exception {
        Car car1 = new Car();
        car1.setId(1L);

        FileResource file1 = new FileResource();
        file1.setImageName("test_image1.jpg");
        file1.setDataValue(new byte[]{1, 2, 3});
        file1.setCar(car1);

        FileResource file2 = new FileResource();
        file2.setImageName("test_image2.jpg");
        file2.setDataValue(new byte[]{4, 5, 6});
        file2.setCar(car1);

        List<FileResource> mockFileResources = new ArrayList<>();
        mockFileResources.add(file1);
        mockFileResources.add(file2);

        when(fileResourceService.findByCarId(anyLong())).thenReturn(mockFileResources);

        StreamingResponseBody result = carService.findImagesByCarId(1L);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        result.writeTo(outputStream);

        byte[] resultBytes = outputStream.toByteArray();
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 6}, resultBytes);
    }

    @Test
    void deleteCarsById() {
        Car car = new Car();
        car.setId(1L);
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        carService.deleteById(1L);
        verify(carRepository).deleteById(1L);
    }

    @Test
    void deleteCarsById_withInvalidId_shouldThrowEntityNotFoundException() {
        Car car = new Car();
        car.setId(1L);
        when(carRepository.findById(1L)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> carService.deleteById(1L));
        assertEquals("Car not found", exception.getMessage());
        verify(carRepository, never()).deleteById(1L);
    }

    @Test
    void findAvailableCarsByDateSeatsCarCategoryModel() {
        Date date = new Date();
        Optional<Integer> seats = Optional.of(5);
        Optional<Long> carCategoryId = Optional.of(1L);
        Optional<String> make = Optional.of("Toyota");
        Optional<String> model = Optional.of("VAN");
        List<Car> expectedCars = of(new Car(), new Car());

        when(carTransferRepository
                .findAvailableCarsByDateSeatsCarCategoryIdMakeModel(date, seats, carCategoryId, make, model))
                .thenReturn(expectedCars);

        List<Car> actualCars = carService
                .findAvailableCarsByDateSeatsCarCategoryModel(date, seats, carCategoryId, make, model);

        assertEquals(expectedCars, actualCars);
        verify(carTransferRepository)
                .findAvailableCarsByDateSeatsCarCategoryIdMakeModel(date, seats, carCategoryId, make, model);
    }

    public static <T> void assertIsInstanceOf(Class<T> expectedClass, Object object) {
        assertTrue(expectedClass.isInstance(object), "Expected object to be an instance of " + expectedClass.getName());
    }
}
