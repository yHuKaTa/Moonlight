package com.aacdemy.moonlight.controller;

import com.aacdemy.moonlight.dto.car.CarImportRequestDto;
import com.aacdemy.moonlight.dto.car.CarImportResponseDto;
import com.aacdemy.moonlight.entity.car.Car;
import com.aacdemy.moonlight.entity.car.enums.CarType;
import com.aacdemy.moonlight.service.CarService;
import com.aacdemy.moonlight.service.FileResourceService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
@Validated
public class CarController {
    private final CarService carService;

    private final FileResourceService fileResourceService;

    @PostMapping(path = "/add-car")
    @Operation(summary = "Add new car", description = "Add new car by providing car's make, model, year and car category ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarImportResponseDto.class), examples = {@ExampleObject(value = """                                                                    
                    {
                        "make": "Car",
                        "model": "Model",
                        "year": "2000",
                        "type": "Sedan",
                        "seats" : "5",
                        "pricePerDay" : "1,99"
                    }""")})),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                    {
                        "message" : "Validation errors:",
                        "errors" : [
                            "The car's model field shouldn't be blank!",
                            "The car category ID field shouldn't be blank!",
                            "The car's year field shouldn't be blank!",
                            "The car's brand field shouldn't be blank!"
                        ]
                    }"""))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                    {
                            "timestamp": "2023-04-25T15:12:55.260+00:00",
                            "status": 403,
                            "error": "Forbidden",
                            "path": "/cars/add-car"
                    }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                    {
                            "timestamp": "2023-04-25T15:27:05.776+00:00",
                            "status": 500,
                            "error": "Internal Server Error",
                            "path": "/cars/add-car"
                    }""")))
    })
    @RolesAllowed({"ROLE_ADMIN"})
    public ResponseEntity<CarImportResponseDto> addCar(
            @RequestHeader("Authorization") @RequestBody @Valid CarImportRequestDto carRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(carService.addCar(carRequest));
    }

    @GetMapping
    @Operation(summary = "Get all cars", description = "Get a list of all available cars")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarImportResponseDto.class),
                    examples = @ExampleObject(value = """
            [
                {
                    "make": "Toyota",
                    "model": "Camry",
                    "year": "2022",
                    "type": "Sedan",
                    "seats": "5",
                    "pricePerDay": "50.00"
                },
                {
                    "make": "Honda",
                    "model": "Accord",
                    "year": "2021",
                    "type": "Sedan",
                    "seats": "5",
                    "pricePerDay": "60.00"
                }
            ]
        """))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 404,
                "error": "Not Found",
                "path": "/cars/{id}"
            }
        """))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 500,
                "error": "Internal Server Error",
                "path": "/cars"
            }
        """)))
    })
    public ResponseEntity<List<Car>> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get car by id", description = "Get a car by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class), examples = @ExampleObject(value = """
            {
                "make": "Toyota",
                "model": "Camry",
                "year": "2022",
                "type": "Sedan",
                "seats": 5,
                "pricePerDay": 50.00
            }
        """))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                {
                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                    "status": 404,
                    "error": "Not Found",
                    "path": "/cars/{id}"
                }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                {
                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                    "status": 500,
                    "error": "Internal Server Error",
                    "path": "/cars/{id}"
                }""")))
    })
    public ResponseEntity<Car> getCarById(
            @PathVariable Long id) {
        Car car = carService.findCarById(id);
        if (car == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(car);
    }

    @GetMapping("/make/{make}")
    @Operation(summary = "Get cars by make", description = "Get a list of cars by their make")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class),
                    examples = @ExampleObject(value = """
            [
                {
                    "make": "Toyota",
                    "model": "Camry",
                    "year": "2022",
                    "type": "Sedan",
                    "seats": 5,
                    "pricePerDay": 50.00
                },
                {
                    "make": "Honda",
                    "model": "Accord",
                    "year": "2023",
                    "type": "Sedan",
                    "seats": 5,
                    "pricePerDay": 55.00
                }
            ]
        """))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                {
                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                    "status": 404,
                    "error": "Not Found",
                    "path": "/cars/make/{make}"
                }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                {
                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                    "status": 500,
                    "error": "Internal Server Error",
                    "path": "/cars/make/{make}"
                }""")))
    })
    public ResponseEntity<List<Car>> getCarsByMake(@PathVariable String make) {
        return ResponseEntity.ok(carService.getCarsByMake(make));
    }

    @GetMapping("/model/{model}")
    @Operation(summary = "Get cars by model", description = "Get a list of cars by their model")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class),
                    examples = @ExampleObject(value = """
            [
                {
                    "make": "Toyota",
                    "model": "Camry",
                    "year": "2022",
                    "type": "Sedan",
                    "seats": 5,
                    "pricePerDay": 50.00
                },
                {
                    "make": "Toyota",
                    "model": "Corolla",
                    "year": "2023",
                    "type": "Sedan",
                    "seats": 5,
                    "pricePerDay": 45.00
                }
            ]
        """))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                {
                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                    "status": 404,
                    "error": "Not Found",
                    "path": "/cars/model/{model}"
                }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                {
                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                    "status": 500,
                    "error": "Internal Server Error",
                    "path": "/cars/make/{make}"
                }""")))
    })
    public ResponseEntity<List<Car>> getCarsByModel(@PathVariable String model) {
        return ResponseEntity.ok(carService.getCarsByModel(model));
    }

    @GetMapping("/year/{year}")
    @Operation(summary = "Get cars by year", description = "Get a list of cars by their year")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class),
                    examples = @ExampleObject(value = """
            [
                {
                    "make": "Toyota",
                    "model": "Camry",
                    "year": "2022",
                    "type": "Sedan",
                    "seats": 5,
                    "pricePerDay": 50.00
                },
                {
                    "make": "Honda",
                    "model": "Accord",
                    "year": "2022",
                    "type": "Sedan",
                    "seats": 5,
                    "pricePerDay": 55.00
                }
            ]
        """))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                {
                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                    "status": 404,
                    "error": "Not Found",
                    "path": "/cars/year/{year}"
                }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                {
                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                    "status": 500,
                    "error": "Internal Server Error",
                    "path": "/cars/year/{year}"
                }""")))
    })
    public ResponseEntity<List<Car>> getCarsByYear(@PathVariable int year) {
        return ResponseEntity.ok(carService.getCarsByYear(year));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get cars by type", description = "Get a list of cars by their type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class),
                    examples = @ExampleObject(value = """
           [
                {
                    "make": "Toyota",
                    "model": "Camry",
                    "year": "2022",
                    "type": "Sedan",
                    "seats": 5,
                    "pricePerDay": 50.00
                },
                {
                    "make": "Honda",
                    "model": "CR-V",
                    "year": "2022",
                    "type": "Van",
                    "seats": 5,
                    "pricePerDay": 65.00
                }
            ]
        """))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                {
                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                    "status": 404,
                    "error": "Not Found",
                    "path": "/cars/type/{type}"
                }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                {
                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                    "status": 500,
                    "error": "Internal Server Error",
                    "path": "/cars/type/{type}"
                }""")))
    })
    public ResponseEntity<List<Car>> findByCarCategoryCarType(
            @PathVariable CarType type) {
        return ResponseEntity.ok(carService.findByCarCategoryCarType(type));
    }

    @GetMapping("/car-seats/{carSeats}")
    @Operation(summary = "Get cars by number of seats", description = "Get a list of cars by the number of seats")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class),
                    examples = @ExampleObject(value = """
            [
                {
                    "make": "Toyota",
                    "model": "Camry",
                    "year": "2022",
                    "type": "Sedan",
                    "seats": 5,
                    "pricePerDay": 50.00
                },
                {
                    "make": "Honda",
                    "model": "CR-V",
                    "year": "2022",
                    "type": "Van",
                    "seats": 5,
                    "pricePerDay": 65.00
                }
            ]
        """))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                {
                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                    "status": 404,
                    "error": "Not Found",
                    "path": "/cars/car-seats/{carSeats}"
                }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                {
                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                    "status": 500,
                    "error": "Internal Server Error",
                    "path": "/cars/car-seats/{carSeats}"
                }""")))
    })
    public ResponseEntity<List<Car>> findByCarCategoryCarSeats(
            @PathVariable int carSeats) {
        return ResponseEntity.ok(carService.findByCarCategoryCarSeats(carSeats));
    }

    // localhost:8080/cars/available?date=2023-05-01
    @GetMapping("/available-date")
    @Operation(summary = "Get available cars by date", description = "Get a list of available cars by a specified date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class),
                    examples = @ExampleObject(value = """
            [
                {
                    "make": "Toyota",
                    "model": "Camry",
                    "year": "2022",
                    "type": "Sedan",
                    "seats": 5,
                    "pricePerDay": 50.00
                },
                {
                    "make": "Honda",
                    "model": "CR-V",
                    "year": "2022",
                    "type": "SUV",
                    "seats": 5,
                    "pricePerDay": 65.00
                }
            ]
        """))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                {
                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                    "status": 404,
                    "error": "Not Found",
                    "path": "/cars/available-date"
                }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                {
                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                    "status": 500,
                    "error": "Internal Server Error",
                    "path": "/cars/available-date"
                }""")))
    })
    public ResponseEntity<List<Car>> findAvailableCarsByDate(
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        return ResponseEntity.ok(carService.findAvailableCarsByDate(date));
    }

    /**
     * Retrieves a list of available cars based on the specified criteria.
     *
     * @param date the date for which to check availability
     * @param seats the required number of seats (optional)
     * @param carCategoryID the required car category (optional)
     * @param model the required car model (optional)
     * @return a list of available cars that meet the specified criteria
     *  https://bootcamp-java-23.services.nasbg.com/cars/available-criteria?date=2023-05-06&seats=5&carCategoryID=1&model=aygo
     */
    @GetMapping("/available-criteria")
    @Operation(summary = "Get available cars by date, seats, category, model", description = "Get a list of available cars by a specified date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of available cars",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Car.class), examples = @ExampleObject(value = """
                            [
                                {
                                    "make": "Toyota",
                                    "model": "Camry",
                                    "year": "2022",
                                    "type": "Sedan",
                                    "seats": 5,
                                    "pricePerDay": 50.00
                                },
                                {
                                    "make": "Honda",
                                    "model": "CR-V",
                                    "year": "2022",
                                    "type": "Van",
                                    "seats": 5,
                                    "pricePerDay": 65.00
                                }
                            ]
                        """))),
            @ApiResponse(responseCode = "400", description = "Required parameter date is missing!",
                    content = @Content(mediaType = "application/json",
                            examples = {@ExampleObject(value =
                                    "{Required parameter date is missing!}")})),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            examples = {@ExampleObject(value =
                                    "{Internal server error}")})),
    })
    public ResponseEntity<List<Car>> findAvailableCarsByDateSeatsCarCategoryModel(
            @Parameter(name = "The date to check availability for, in format yyyy-MM-dd", example = "2023-05-10", required = true)
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @Parameter(name = "The number of seats for the car (optional)", example = "4")
            @RequestParam("seats") Optional<Integer> seats,
            @Parameter(name = "The ID of the car category to filter by (optional)", example = "1")
            @RequestParam("car-categoryID") Optional<Long> carCategoryID,
            @Parameter(name = "The model name to filter by (optional)", example = "Trabant")
            @RequestParam("make") Optional<String> make,
            @Parameter(name = "The model name to filter by (optional)", example = "X7")
            @RequestParam("model") Optional<String> model) {

        return ResponseEntity.ok(carService.findAvailableCarsByDateSeatsCarCategoryModel(date, seats, carCategoryID, make, model));
    }

    /**
     * Retrieves available images for a car with the given ID.
     *
     * @param id The ID of the car.
     * @return A ResponseEntity with a StreamingResponseBody that streams the images as a response body, and appropriate headers and HTTP status code.
     */
    @GetMapping(path = "/images/show/{id}")
    @Operation(summary = "Get available images by car id", description = "Retrieves available images for a car with the given ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "multipart/mixed", examples = @ExampleObject(value = """
        --Boundary
        Content-Type: image/jpeg
        Content-Length: 12345

        [binary data]

        --Boundary
        Content-Type: image/jpeg
        Content-Length: 67890

        [binary data]
        """))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 404,
                "error": "Not Found",
                "path": "/images/show/{id}"
            }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 500,
                "error": "Internal Server Error",
                "path": "/images/show/{id}"
            }""")))
    })
    public ResponseEntity<StreamingResponseBody> downloadImagesByCarId(
            @PathVariable Long id) {
        StreamingResponseBody responseBody = carService.findImagesByCarId(id);
        HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.IMAGE_JPEG); // visualize only the first image of the car - for V2, V3, V4 of carService.findImagesByCarId
        headers.setContentType(MediaType.MULTIPART_MIXED); //for V4 of carService.findImagesByCarId
        return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
    }
//    public ResponseEntity<MultiValueMap<String, Object>> getCarImages(@PathVariable Long id) {
//        List<FileResource> images = fileResourceService.findByCarId(id);
//        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//        for (FileResource image : images) {
//            ByteArrayResource resource = new ByteArrayResource(image.getDataValue());
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.IMAGE_JPEG);
//            headers.setContentDispositionFormData("attachment", image.getImageName());
//            body.add("images", new HttpEntity<>(resource, headers));
//        }
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_MIXED);
//        return new ResponseEntity<>(body, headers, HttpStatus.OK);
//    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Deletes car by id", description = "Deletes a car by its' id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(value = """
                        {
                            "timestamp": "2023-04-25T15:12:55.260+00:00",
                            "status": 403,
                            "error": "Forbidden",
                            "path": "/car-category/add-category"
                        }"""))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(value = """
                        {
                            "timestamp": "2023-04-25T15:27:05.776+00:00",
                            "status": 404,
                            "error": "Not Found",
                            "path": "/cars/{id}"
                        }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(value = """
                        {
                            "timestamp": "2023-04-25T15:27:05.776+00:00",
                            "status": 500,
                            "error": "Internal Server Error",
                            "path": "/cars/{id}"
                        }""")))
    })
    public ResponseEntity<HttpStatus> deleteCar(@Parameter(description = "Authorization token", required = true,
            schema = @Schema(implementation = String.class),
            examples = @ExampleObject(value = "Bearer {access_token}"))
                                                @RequestHeader("Authorization") String authorization,@PathVariable Long id) {
        carService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
