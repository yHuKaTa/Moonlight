package com.aacdemy.moonlight.service.impl;

import com.aacdemy.moonlight.dto.car.CarImportRequestDto;
import com.aacdemy.moonlight.dto.car.CarImportResponseDto;
import com.aacdemy.moonlight.entity.car.Car;
import com.aacdemy.moonlight.entity.car.FileResource;
import com.aacdemy.moonlight.entity.car.enums.CarType;
import com.aacdemy.moonlight.exception.EntityNotFoundException;
import com.aacdemy.moonlight.repository.car.CarTransferRepository;
import com.aacdemy.moonlight.repository.car.CarRepository;
import com.aacdemy.moonlight.service.CarCategoryService;
import com.aacdemy.moonlight.service.CarService;
import com.aacdemy.moonlight.service.FileResourceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Optional;



@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final CarTransferRepository carTransferRepository;
    private final CarCategoryService carCategoryService;
    private final FileResourceService fileResourceService;

    @Override
    public Car getCarById(Long id) {
        return carRepository.getReferenceById(id);
    }

    @Override
    public Car findCarById(Long id) {
        return carRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Car not found"));
    }

    @Override
    @Transactional
    public CarImportResponseDto addCar(CarImportRequestDto carImportRequest) {
        Car car = Car.builder().make(carImportRequest.getMake()).model(carImportRequest.getModel()).year(Integer.parseInt(carImportRequest.getYear())).carCategory(carCategoryService.findById(Long.parseLong(carImportRequest.getCarCategoryId()))).build();
        carRepository.save(car);

        return CarImportResponseDto.builder().make(car.getMake()).model(car.getModel()).year(String.valueOf(car.getYear())).type(car.getCarCategory().getType().getCarType()).seats(String.valueOf(car.getCarCategory().getSeats())).pricePerDay(String.valueOf(car.getCarCategory().getPricePerDay())).build();
    }

    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @Override
    public List<Car> getCarsByMake(String make) {
        return carRepository.findByMake(make);
    }

    @Override
    public List<Car> getCarsByModel(String model) {
        return carRepository.findByModel(model);
    }

    @Override
    public List<Car> getCarsByYear(int year) {
        return carRepository.findByYear(year);
    }

    @Override
    public List<Car> findByCarCategoryCarSeats(int carSeats) {
        return carRepository.findByCarCategorySeats(carSeats);
    }

    @Override
    public List<Car> findByCarCategoryCarType(CarType type) {
        return carRepository.findByCarCategoryType(type);
    }

    @Override
    public List<Car> findAvailableCarsByDate(Date date) {

        return carTransferRepository.findAvailableCarsByDate(date);
    }

    /**
     * Retrieves a list of available cars based on the specified criteria.
     *
     * @param date the date for which to check availability
     * @param seats the required number of seats (optional)
     * @param carCategoryID the required car category (optional)
     * @param model the required car model (optional)
     * @return a list of available cars that meet the specified criteria
     */
    @Override
    public List<Car> findAvailableCarsByDateSeatsCarCategoryModel(Date date,
                                                                  Optional<Integer> seats,
                                                                  Optional<Long> carCategoryID,
                                                                  Optional<String> make,
                                                                  Optional<String> model) {

        return carTransferRepository.findAvailableCarsByDateSeatsCarCategoryIdMakeModel(date, seats, carCategoryID, make, model);
    }

    /**
     * Returns all images associated with a car with the specified ID
     * V1 returns
     * V2, V3, V4:  Only one image can be displayed as a response in Postman
     * To view more than one image,please use a browser /?/.
     */
    @Override
    public StreamingResponseBody findImagesByCarId(Long carId) {
        List<FileResource> files = fileResourceService.findByCarId(carId);


// V1:
        return outputStream -> {
            for (FileResource file : files) {
                byte[] data = file.getDataValue();

//writes in console - as a result 3 images of the car are available:
//fileResourceService.findByCarId: File name: 1971efdd-c11b-43ee-a8b6-9e45d28f863d, carId: 1, length: 5078
//fileResourceService.findByCarId: File name: c74a2359-a075-439b-97fb-d7bdf80d5c0b, carId: 1, length: 5078
//fileResourceService.findByCarId: File name: e8743346-9b1a-4557-b5b7-ec822b3ba393, carId: 1, length: 24134
                System.out.println("fileResourceService.findByCarId: File name: "
                        + file.getImageName()
                        + ", carId: " + file.getCar().getId()
                        + ", length: " + data.length);

                // write the file data to the output stream
                outputStream.write(data, 0, data.length);
            }

            // flush the output stream
            outputStream.close();
        };


// ===========================V2==================================:
//        return outputStream -> {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            for (FileResource file : files) {
//                byte[] data = file.getDataValue();
//                baos.write(data, 0, data.length);
//            }
//            baos.flush();
//            outputStream.write(baos.toByteArray());
//        };

// =============================V3==============================:
//        return outputStream -> {
//            for (FileResource file : files) {
//                byte[] data = file.getDataValue();
//                outputStream.write(data, 0, data.length);
//            }
//        };

// ===========================V4================================:
//        return outputStream -> {
//            for (FileResource file : files) {
//                System.out.println(file.getImageName());
//                try (InputStream inputStream = new ByteArrayInputStream(file.getDataValue())) {
//                    IOUtils.copy(inputStream, outputStream);
//                } catch (IOException e) {
//                    // handle exception
//                }
//            }
//        };
//============================================================


    }

    @Override
    public void deleteById(Long id) throws EntityNotFoundException {
        Optional<Car> car = this.carRepository.findById(id);
        if (car.isEmpty()) {
            throw new EntityNotFoundException("Car not found");
        }
        this.carRepository.deleteById(id);
    }
}