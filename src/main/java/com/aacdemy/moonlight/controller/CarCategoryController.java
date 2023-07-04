package com.aacdemy.moonlight.controller;

import com.aacdemy.moonlight.dto.car.CarCategoryRequestDto;
import com.aacdemy.moonlight.dto.car.CarCategoryResponseDto;
import com.aacdemy.moonlight.entity.car.CarCategory;
import com.aacdemy.moonlight.entity.car.enums.CarType;
import com.aacdemy.moonlight.service.CarCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/car-category")
@Validated
@RequiredArgsConstructor
public class CarCategoryController {
    private final CarCategoryService carCategoryService;

    @PostMapping(path = "/add-category")
    @RolesAllowed({"ROLE_ADMIN"})
    @Operation(summary = "Add new car category", description = "Add new car category by providing category's type, seats and price per day.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarCategoryResponseDto.class), examples = {@ExampleObject(value = """                                                                    
                    {
                        "id": "3",
                        "type": "Van",
                        "seats": "5",
                        "pricePerDay": "100.0"
                    }""")})),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                    {
                        "message" : "Validation errors:",
                        "errors" : [
                            "The car's seats field shouldn't be blank!",
                            "The car's type field shouldn't be blank!",
                            "The car's price per day field shouldn't be blank!"
                        ]
                    }"""))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                    {
                            "timestamp": "2023-04-25T15:12:55.260+00:00",
                            "status": 403,
                            "error": "Forbidden",
                            "path": "/car-category/add-category"
                    }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                    {
                            "timestamp": "2023-04-25T16:06:58.625+00:00",
                            "status": 500,
                            "error": "Internal Server Error",
                            "path": "/car-category/add-category"
                    }""")))
    })
    public ResponseEntity<CarCategoryResponseDto> addCarCategory(@RequestHeader("Authorization") @RequestBody @Valid CarCategoryRequestDto carCategoryRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(carCategoryService.addCategory(carCategoryRequest));
    }

    @GetMapping()
    @Operation(summary = "Get all car categories", description = "Retrieve a list of all car categories.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarCategoryResponseDto.class), examples = @ExampleObject(value = """
                [
                    {
                        "id": "1",
                        "type": "Sedan",
                        "seats": "5",
                        "pricePerDay": "50.00"
                    },
                    {
                        "id": "2",
                        "type": "SUV",
                        "seats": "7",
                        "pricePerDay": "80.00"
                    }
                ]"""))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                    {
                        "timestamp": "2023-04-25T16:06:58.625+00:00",
                        "status": 404,
                        "error": "Not Found",
                        "path": "/car-category"
                    }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                    {
                            "timestamp": "2023-04-25T16:06:58.625+00:00",
                            "status": 500,
                            "error": "Internal Server Error",
                            "path": "/car-category"
                    }""")))
    })
    public ResponseEntity<List<CarCategory>> getAllCarCategories() {
        List<CarCategory> carCategories = carCategoryService.getAllCarCategories();
        return ResponseEntity.ok(carCategories);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get car category by ID", description = "Get car category by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarCategoryResponseDto.class), examples = @ExampleObject(value = """
                {
                    "id": "1",
                    "type": "Sedan",
                    "seats": "5",
                    "pricePerDay": "50.00"
                }"""))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                    {
                        "timestamp": "2023-04-25T16:06:58.625+00:00",
                        "status": 404,
                        "error": "Not Found",
                        "path": "/car-category/{id}"
                    }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                    {
                            "timestamp": "2023-04-25T16:06:58.625+00:00",
                            "status": 500,
                            "error": "Internal Server Error",
                            "path": "/car-category/{id}"
                    }""")))
    })
    public ResponseEntity<CarCategory> getCarCategoryById(@PathVariable Long id) {
        CarCategory carCategory = carCategoryService.findById(id);
        if (carCategory == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(carCategory);
    }

    @GetMapping("/type")
    @Operation(summary = "Get car categories by type", description = "Get a list of car categories by their type.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarCategoryResponseDto.class), examples = @ExampleObject(value = """
                [
                    {
                        "id": "1",
                        "type": "Sedan",
                        "seats": "5",
                        "pricePerDay": "50.00"
                    },
                    {
                        "id": "2",
                        "type": "Sport",
                        "seats": "7",
                        "pricePerDay": "80.00"
                    }
                ]"""))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                    {
                        "timestamp": "2023-04-25T16:06:58.625+00:00",
                        "status": 404,
                        "error": "Not Found",
                        "path": "/car-category/type"
                    }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                    {
                            "timestamp": "2023-04-25T16:06:58.625+00:00",
                            "status": 500,
                            "error": "Internal Server Error",
                            "path": "/car-category/type"
                    }""")))
    })
    public ResponseEntity<List<CarCategory>> getCarCategoriesByType(@RequestParam("type") CarType type) {
        List<CarCategory> carCategories = carCategoryService.getCarCategoriesByType(type);
        if (carCategories.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(carCategories);
    }

    @GetMapping("/seats/{seats}")
    @Operation(summary = "Get car categories by seats", description = "Get a list of car categories by the number of seats.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarCategoryResponseDto.class)
                    , examples = @ExampleObject(value = """
                [
                    {
                        "id": "1",
                        "type": "Sedan",
                        "seats": "5",
                        "pricePerDay": "50.00"
                    },
                    {
                        "id": "2",
                        "type": "Van",
                        "seats": "7",
                        "pricePerDay": "80.00"
                    }
                ]"""))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                {
                    "timestamp": "2023-04-25T16:06:58.625+00:00",
                    "status": 404,
                    "error": "Not Found",
                    "path": "/car-category/seats/{seats}"
                }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                {
                    "timestamp": "2023-04-25T16:06:58.625+00:00",
                    "status": 500,
                    "error": "Internal Server Error",
                    "path": "/car-category/seats/{seats}"
               }""")))
    })
    public ResponseEntity<List<CarCategory>> getCarCategoriesBySeats(@PathVariable int seats) {
        List<CarCategory> carCategories = carCategoryService.getCarCategoriesBySeats(seats);
        if (carCategories.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(carCategories);
    }

    @GetMapping("/price/{pricePerDay}")
    @Operation(summary = "Get car categories by price per day", description = "Get a list of car categories by the price per day.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarCategoryResponseDto.class),
                    examples = @ExampleObject(value = """
                [
                    {
                        "id": "1",
                        "type": "Sedan",
                        "seats": "5",
                        "pricePerDay": "50.00"
                    },
                    {
                        "id": "2",
                        "type": "Sport",
                        "seats": "7",
                        "pricePerDay": "80.00"
                    }
                ]"""))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                    {
                        "timestamp": "2023-04-25T16:06:58.625+00:00",
                        "status": 404,
                        "error": "Not Found",
                        "path": "/car-category/price/{pricePerDay}"
                    }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                    {
                            "timestamp": "2023-04-25T16:06:58.625+00:00",
                            "status": 500,
                            "error": "Internal Server Error",
                            "path": "/car-category/price/{pricePerDay}"
                    }""")))
    })
    public ResponseEntity<List<CarCategory>> getCarCategoriesByPricePerDay(@PathVariable double pricePerDay) {
        List<CarCategory> carCategories = carCategoryService.getCarCategoriesByPricePerDay(pricePerDay);
        if (carCategories.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(carCategories);
    }
}
