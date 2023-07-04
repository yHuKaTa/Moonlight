package com.aacdemy.moonlight.controller;

import com.aacdemy.moonlight.config.security.JwtService;
import com.aacdemy.moonlight.dto.car.CarTransferRequestDto;
import com.aacdemy.moonlight.dto.car.CarTransferResponseDto;
import com.aacdemy.moonlight.entity.car.CarTransfer;
import com.aacdemy.moonlight.service.CarTransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/transfers")
@Validated
public class CarTransferController {

    private final CarTransferService carTransferService;

    private final JwtService jwtService;

    @Autowired
    public CarTransferController(CarTransferService carTransferService, JwtService jwtService) {
        this.carTransferService = carTransferService;
        this.jwtService = jwtService;
    }

    @PostMapping(path = "/add-transfer")
    @Operation(summary = "Add new car transfer", description = "Add new car transfer by providing car's seats, make + model and date for transfer.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarTransferResponseDto.class), examples = {@ExampleObject(value = """                                                                    
            {
                "message": "Your reservation for car transfer from/to Moonlight Hotel & Spa successful was successful. Car transfer details:",
                "id": "6",
                "make": "Bmw",
                "model": "e36",
                "seats": "5",
                "date": "2005-01-01",
                "price": "20.0"
            }""")})), @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "message" : "Validation errors:",
                "errors" : [
                            "The car's field shouldn't be blank!",
                            "The car's field should be valid format with characters and digits",
                            "The car's seats field should consist of 1 to 2 digits",
                            "Date format invalid! Provide date format YYYY-MM-DD!",
                            "The car's seats field shouldn't be blank!",
                            "The date for transfer shouldn't be blank!"
                ]
            }"""))), @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                 "error": "JWT token has expired"
            }""")))})
    public ResponseEntity<CarTransferResponseDto> saveCarTransfer(@RequestBody @Valid CarTransferRequestDto carTransferRequestDto, @RequestHeader("Authorization") String token) throws ParseException {
        return ResponseEntity.status(HttpStatus.CREATED).body(carTransferService.addCarTransfer(carTransferRequestDto, jwtService.extractUserName(token.substring(7))));
    }

    @GetMapping(path = "/all-transfers")
    @RolesAllowed({"ROLE_ADMIN"})
    @Operation(summary = "Get all car transfers", description = "Get a list of all car transfers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "multipart/mixed",
                            schema = @Schema(implementation = CarTransfer.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(value = """
                                    {
                                        "timestamp": "2023-04-25T15:12:55.260+00:00",
                                        "status": 403,
                                        "error": "Forbidden",
                                        "path": "/transfers/all-transfers"
                                    }"""))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(value = """
                                    {
                                        "timestamp": "2023-04-25T15:27:05.776+00:00",
                                        "status": 404,
                                        "error": "Not Found",
                                        "path": "/transfers/all-transfers"
                                    }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(value = """
                                    {
                                        "timestamp": "2023-04-25T15:27:05.776+00:00",
                                        "status": 500,
                                        "error": "Internal Server Error",
                                        "path": "/transfers/all-transfers"
                                    }""")))
    })
    public ResponseEntity<List<CarTransfer>> getAllTransfers(@Parameter(description = "Authorization token", required = true,
            schema = @Schema(implementation = String.class),
            examples = @ExampleObject(value = "Bearer {access_token}"))
                                                             @RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(carTransferService.getAllTransfers());
    }

    @GetMapping(path = "/{id}")
    @RolesAllowed({"ROLE_ADMIN"})
    @Operation(summary = "Get car transfer by id", description = "Get a car transfer by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "multipart/mixed",
                            schema = @Schema(implementation = CarTransfer.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(value = """
                        {
                            "timestamp": "2023-04-25T15:12:55.260+00:00",
                            "status": 403,
                            "error": "Forbidden",
                            "path": "/transfers/{id}"
                        }"""))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(value = """
                        {
                            "timestamp": "2023-04-25T15:27:05.776+00:00",
                            "status": 404,
                            "error": "Not Found",
                            "path": "/transfers/{id}"
                        }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(value = """
                        {
                            "timestamp": "2023-04-25T15:27:05.776+00:00",
                            "status": 500,
                            "error": "Internal Server Error",
                            "path": "/transfers/{id}"
                        }""")))
    })
    public ResponseEntity<CarTransfer> getTransferById(@Parameter(description = "Transfer ID", required = true,
            schema = @Schema(implementation = Long.class),
            examples = @ExampleObject(value = "1"))
                                                       @PathVariable Long id,
                                                       @Parameter(description = "Authorization token", required = true,
                                                               schema = @Schema(implementation = String.class),
                                                               examples = @ExampleObject(value = "Bearer {access_token}"))
                                                       @RequestHeader("Authorization") String authorizationHeader) {
        CarTransfer transfer = carTransferService.getTransferById(id);
        if (Objects.isNull(transfer)) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(carTransferService.getTransferById(id));
        }
    }

    @GetMapping(path = "/get-with-car/{car}")
    @RolesAllowed({"ROLE_ADMIN"})
    @Operation(summary = "Get car transfers by car make or model", description = "Get car transfers by car make or model")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "multipart/mixed")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                {
                    "timestamp": "2023-04-25T15:12:55.260+00:00",
                    "status": 403,
                    "error": "Forbidden",
                    "path": "/transfers/get-with-car/{car}"
                }"""))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                {
                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                    "status": 404,
                    "error": "Not Found",
                    "path": "/transfers/get-with-car/{car}"
                }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                {
                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                    "status": 500,
                    "error": "Internal Server Error",
                    "path": "/transfers/get-with-car/{car}"
                }""")))
    })
    public ResponseEntity<List<CarTransfer>> getTransfersByCar( @Parameter(description = "Car make or model", required = true,
            schema = @Schema(implementation = String.class),
            examples = @ExampleObject(value = "Toyota Camry"))
                                                                @PathVariable String car,
                                                                @Parameter(description = "Authorization token", required = true,
                                                                        schema = @Schema(implementation = String.class),
                                                                        examples = @ExampleObject(value = "Bearer {access_token}"))
                                                                @RequestHeader("Authorization") String authorizationHeader) {
        List<CarTransfer> transfers = carTransferService.getTransfersByCar(car);
        if (transfers.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(transfers);
        }
    }

    @GetMapping
    @Operation(summary = "Get car transfers by user email", description = "Get car transfers by user email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "multipart/mixed")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                {
                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                    "status": 404,
                    "error": "Not Found",
                    "path": "/transfers"
                }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                {
                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                    "status": 500,
                    "error": "Internal Server Error",
                    "path": "/transfers"
                }""")))
    })
    public ResponseEntity<List<CarTransfer>> getTransfersByUserEmail(   @Parameter(description = "Authorization token", required = true,
            schema = @Schema(implementation = String.class),
            examples = @ExampleObject(value = "Bearer {access_token}"))
                                                                        @RequestHeader("Authorization") String token) {
        List<CarTransfer> transfers = carTransferService.getTransfersByUserEmail(jwtService.extractUserName(token.substring(7)));
        if (transfers.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(transfers);
        }
    }

    @DeleteMapping(path = "/{id}")
    @RolesAllowed({"ROLE_ADMIN"})
    @Operation(summary = "Delete car transfer by ID", description = "Delete car transfer by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                {
                        "timestamp": "2023-04-25T15:12:55.260+00:00",
                        "status": 403,
                        "error": "Forbidden",
                        "path": "/transfers/{id}"
                }"""))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                {
                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                    "status": 404,
                    "error": "Not Found",
                    "path": "/transfers/{id}"
                }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                {
                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                    "status": 500,
                    "error": "Internal Server Error",
                    "path": "/transfers/get-with-car/{car}"
                }""")))
    })
    public ResponseEntity<String> deleteTransferById(@Parameter(description = "Authorization token", required = true,
            schema = @Schema(implementation = String.class),
            examples = @ExampleObject(value = "Bearer {access_token}"))
                                                     @RequestHeader("Authorization") String token,
                                                     @Parameter(description = "Transfer ID", required = true,
                                                             schema = @Schema(implementation = Long.class),
                                                             examples = @ExampleObject(value = "123"))
                                                     @PathVariable Long id) {
        String result = carTransferService.deleteTransferById(id);
        if (Objects.isNull(result) || result.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(result);
        }
    }
}
