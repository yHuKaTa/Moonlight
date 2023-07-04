package com.aacdemy.moonlight.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.aacdemy.moonlight.dto.car.CarCategoryRequestDto;
import com.aacdemy.moonlight.dto.car.CarCategoryResponseDto;
import com.aacdemy.moonlight.entity.car.CarCategory;
import com.aacdemy.moonlight.entity.car.enums.CarType;
import com.aacdemy.moonlight.exception.EntityNotFoundException;
import com.aacdemy.moonlight.repository.car.CarCategoryRepository;
import com.aacdemy.moonlight.service.impl.CarCategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith({MockitoExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class CarCategoryServiceImplTest {

    @Mock
    //This annotation is used to mock dependencies in the class being tested, so that the class can be tested in isolation
    private CarCategoryRepository carCategoryRepository;

    @InjectMocks
    //This annotation is used to inject the mocked dependencies into the class being tested, so that the class can use the mocked dependencies instead of real dependencies during testing
    private CarCategoryServiceImpl carCategoryService;

    @Test
    void testFindById_Success() {
        // given
        Long id = 1L;
        CarCategory carCategory = new CarCategory();
        carCategory.setId(id);
        when(carCategoryRepository.findById(id)).thenReturn(Optional.of(carCategory));

        // when
        CarCategory result = carCategoryService.findById(id);

        // then
        assertThat(result).isEqualTo(carCategory);
    }

    @Test
    void testFindById_EntityNotFoundException() {
        // given
        Long id = 1L;
        when(carCategoryRepository.findById(id)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> carCategoryService.findById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Car category with ID " + id + " isn't found!");
    }

    @Test
    void addCategory_ShouldReturnCarCategoryResponseDto() {
        MockitoAnnotations.openMocks(this);

        CarCategoryRequestDto requestDto = new CarCategoryRequestDto();
        requestDto.setCarType("SPORT");
        requestDto.setSeats("2");
        requestDto.setPricePerDay("200.00");

        CarCategory savedCarCategory = new CarCategory();
        savedCarCategory.setId(1L);
        savedCarCategory.setType(CarType.SPORT);
        savedCarCategory.setSeats(2);
        savedCarCategory.setPricePerDay(200.00);

        when(carCategoryRepository.save(any(CarCategory.class))).thenReturn(savedCarCategory);

        CarCategoryResponseDto responseDto = carCategoryService.addCategory(requestDto);

        //assertEquals("1", responseDto.getId());
        assertEquals("SPORT", responseDto.getType().toUpperCase());
        assertEquals("2", responseDto.getSeats());
        assertEquals("200.0", responseDto.getPricePerDay());

        verify(carCategoryRepository, times(0)).save(any(CarCategory.class));
    }

    @Test
    void addCategory_ShouldThrowException_WhenInvalidCarType() {
        CarCategoryRequestDto requestDto = new CarCategoryRequestDto();
        requestDto.setCarType("INVALID_TYPE");
        requestDto.setSeats("5");
        requestDto.setPricePerDay("50.00");

        assertThrows(IllegalArgumentException.class, () -> carCategoryService.addCategory(requestDto));
    }

    @Test
    void addCategory_ShouldThrowException_WhenInvalidSeats() {
        CarCategoryRequestDto requestDto = new CarCategoryRequestDto();
        requestDto.setCarType("SPORT");
        requestDto.setSeats("INVALID_SEATS");
        requestDto.setPricePerDay("200.00");

        assertThrows(NumberFormatException.class, () -> carCategoryService.addCategory(requestDto));
    }

    @Test
    void addCategory_ShouldThrowException_WhenInvalidPricePerDay() {
        CarCategoryRequestDto requestDto = new CarCategoryRequestDto();
        requestDto.setCarType("SPORT");
        requestDto.setSeats("2");
        requestDto.setPricePerDay("INVALID_PRICE");

        assertThrows(NumberFormatException.class, () -> carCategoryService.addCategory(requestDto));
    }

    @Test
    void testGetAllCarCategories() {
        List<CarCategory> expectedCategories = Arrays.asList(
                new CarCategory(1L, CarType.SPORT, 2, 100.0),
                new CarCategory(2L, CarType.SPORT, 2, 100.0)
        );
        when(carCategoryRepository.findAll()).thenReturn(expectedCategories);
        carCategoryService.getAllCarCategories();
        verify(carCategoryRepository, times(1)).findAll();
    }

    @Test
    void testGetCarCategoriesByType() {
        List<CarCategory> expectedCategories = Arrays.asList(
                new CarCategory(1L, CarType.SPORT, 2, 100.0),
                new CarCategory(2L, CarType.SPORT, 2, 100.0)
        );
        CarType type = CarType.SPORT;
        Mockito.when(carCategoryRepository.findByType(type)).thenReturn(expectedCategories);

        carCategoryService.getCarCategoriesByType(type);
        verify(carCategoryRepository, times(1)).findByType(type);
    }

    @Test
    void testGetCarCategoriesBySeats() {
        List<CarCategory> expectedCategories = Arrays.asList(
                new CarCategory(1L, CarType.SPORT, 2, 100.0),
                new CarCategory(2L, CarType.SPORT, 2, 100.0)
        );
        int seats = 2;
        Mockito.when(carCategoryRepository.findBySeats(seats)).thenReturn(expectedCategories);
        carCategoryService.getCarCategoriesBySeats(seats);
        verify(carCategoryRepository, times(1)).findBySeats(seats);
    }

    @Test
    void testGetCarCategoriesByPricePerDay() {
        List<CarCategory> expectedCategories = Arrays.asList(
                new CarCategory(1L, CarType.SPORT, 2, 100.0),
                new CarCategory(2L, CarType.SPORT, 2, 100.0)
        );
        double pricePerDay = 100.0;
        Mockito.when(carCategoryRepository.findByPricePerDay(pricePerDay)).thenReturn(expectedCategories);
        carCategoryService.getCarCategoriesByPricePerDay(pricePerDay);
        verify(carCategoryRepository, times(1)).findByPricePerDay(pricePerDay);
    }
}



