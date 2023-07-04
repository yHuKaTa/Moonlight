package com.aacdemy.moonlight.controller;
import com.aacdemy.moonlight.config.security.JwtService;
import com.aacdemy.moonlight.dto.screen.ScreenEvenResponseDto;
import com.aacdemy.moonlight.entity.restaurant.TableReservation;
import com.aacdemy.moonlight.dto.restaurant.TableReservationRequestDto;
import com.aacdemy.moonlight.dto.restaurant.TableReservationResponseDto;
import com.aacdemy.moonlight.service.TableReservationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.aacdemy.moonlight.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.text.ParseException;
import java.util.List;

@RestController
@Validated
@RequestMapping("/table-reservations")
public class TableReservationController {
    private final TableReservationService tableReservationService;
    private final UserService userService;
    private final JwtService jwtService;

    public TableReservationController(TableReservationService tableReservationService, UserService userService, JwtService jwtService) {
        this.tableReservationService = tableReservationService;
        this.userService = userService;
        this.jwtService = jwtService;
    }
    @GetMapping("/current-user")
    @Operation(summary = "Get table reservation by currently logged user", description = "Retrieves table reservation by currently logged user.")
    @PreAuthorize("isAuthenticated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TableReservationRequestDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                    {
                        "timestamp": "2023-04-25T16:06:58.625+00:00",
                        "status": 404,
                        "error": "Not Found",
                        "message": "User not found",
                        "path": "/table-reservations/current-user"
                    }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                    {
                            "timestamp": "2023-04-25T16:06:58.625+00:00",
                            "status": 500,
                            "error": "Internal Server Error",
                            "path": "/table-reservations/current-user"
                    }""")))
    })
    public ResponseEntity<List<TableReservation>> getByCurrentUser(@RequestHeader("Authorization") String token) throws ParseException {
        List<TableReservation> reservations = tableReservationService.findByCurrentlyLoggedUser(token);
        return ResponseEntity.ok(reservations);
    }
    ///table-reservations/get-by-user/{userID}
    // @PreAuthorize("#userId == authentication.getPrincipal().id or hasAuthority('ROLE_ADMIN')")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("get-by-user/{userId}")
    @Operation(summary = "Get table reservations by user ID",
            description = "Retrieves a list of room reservation by user's ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "406", description = "Not Acceptable",
                    content = @Content(mediaType = "application/json",
                            examples = {@ExampleObject(value =
                                    "{" +
                                            "    \"Invalid input data\": \"Please check startDate parameter and try again!\"" +
                                            "}")})),
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of available rooms",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TableReservation.class))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            examples = {@ExampleObject(value =
                                    "{" +
                                            "\"error\": \"There are no rooms available according to specify start and end date!\"" +
                                            "}")})),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json",
                            examples = {@ExampleObject(value =
                                    "{" +
                                            "    \"message\": \"Constraint violation error\",\n" +
                                            "    \"errors\": [\n" +
                                            "        \"Enter valid present/future date!\",\n" +
                                            "        \"Enter valid present/future date!\"\n" +
                                            "    ]" +
                                            "}")})),
    })
    public ResponseEntity<List<TableReservation>> getByUserId(@RequestHeader("Authorization") @PathVariable Long userId) {
        List<TableReservation> reservation = tableReservationService.findByUserId(userId);
        return ResponseEntity.ok(reservation);
    }

    ///table-reservations/get-all
    @GetMapping("/get-all")
    @Operation(summary = "Get all table reservations", description = "Retrieves a list of all table reservations.")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "multipart/mixed")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                    {
                            "timestamp": "2023-04-25T15:12:55.260+00:00",
                            "status": 403,
                            "error": "Forbidden",
                            "path": "/table-reservations/get-all"
                    }"""))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 404,
                "error": "Not Found",
                "path": "/table-reservations/get-all"
            }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 500,
                "error": "Internal Server Error",
                "path": "/table-reservations/get-all"
            }""")))
    })

    public ResponseEntity<List<TableReservation>> getAllTableReservations() {
        List<TableReservation> reservations = tableReservationService.findAll();
        return ResponseEntity.ok(reservations);
    }


    @PostMapping("/add-reservation")
    @PreAuthorize("isAuthenticated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ScreenEvenResponseDto.class), examples = {@ExampleObject(value = """                                                                    
                    {
                      "data": "2023-06-11",
                      "hour": 0,
                      "tableRestaurant": "string"
                      "tableNumber": "1"
                      "people": "2"
                    }""")})),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                    {
                        "message" : "Validation errors:",
                        "errors" : [
                            "The data is required!",
                            "The hour Id is required!",
                            "The tableRestaurant is required!",
                            "The tableNumber is required!",
                            "The people is required!",
                        ]
                    }"""))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                    {
                            "timestamp": "2023-04-25T15:12:55.260+00:00",
                            "status": 403,
                            "error": "Forbidden",
                            "path": "/table-reservations/add-reservation"
                    }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                    {
                            "timestamp": "2023-04-25T15:27:05.776+00:00",
                            "status": 500,
                            "error": "Internal Server Error",
                            "path": "/table-reservations/add-reservation"
                    }""")))
    })
    public ResponseEntity<TableReservationResponseDto> saveReservation(
            @RequestBody TableReservationRequestDto reservationRequest,
            @Valid @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.substring(7);
        TableReservationResponseDto tableReservationResponseDto = tableReservationService.saveReservation(reservationRequest, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(tableReservationResponseDto);
    }
}
