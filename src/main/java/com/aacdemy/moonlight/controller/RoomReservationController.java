package com.aacdemy.moonlight.controller;
import com.aacdemy.moonlight.config.security.JwtService;
import com.aacdemy.moonlight.dto.roomReservation.RoomReservationRequestDto;
import com.aacdemy.moonlight.dto.roomReservation.RoomReservationResponseDto;
import com.aacdemy.moonlight.dto.roomReservation.RoomReservationUpdateRequestDto;
import com.aacdemy.moonlight.dto.roomReservation.RoomReservationUpdateResponseDto;
import com.aacdemy.moonlight.entity.hotel.Room;
import com.aacdemy.moonlight.exception.EntityNotFoundException;
import com.aacdemy.moonlight.service.RoomReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.xml.bind.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@Validated
@RequestMapping("/room-reservations")
public class RoomReservationController {

    private final RoomReservationService roomReservationService;

    private final JwtService jwtService;
    @Autowired
    public RoomReservationController(RoomReservationService roomReservationService, JwtService jwtService) {
        this.roomReservationService = roomReservationService;
        this.jwtService = jwtService;
    }
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete room reservation by ID", description = "Deletes a room reservation by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                    {
                        "timestamp": "2023-04-25T15:27:05.776+00:00",
                        "status": 404,
                        "error": "Room not found",
                        "path": "/room-reservations/delete/{id}"
                    }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                    {
                        "timestamp": "2023-04-25T15:27:05.776+00:00",
                        "status": 500,
                        "error": "Internal Server Error",
                        "path": "/room-reservations/delete/{id}"
                    }""")))
    })
    public ResponseEntity<HttpStatus> deleteRoomReservation(@PathVariable Long id) {
        roomReservationService.deleteRoomReservation(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Add new room reservation", description = "Add new room reservation by providing room's ID, start and end date of reservation, number of adults and children and bed type.")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RoomReservationResponseDto.class), examples = {@ExampleObject(value = """                                                                    
            {
                "id": 21,
                "startDate": "2023-05-15",
                "endDate": "2023-05-15",
                "roomType": "STUDIO",
                "adults": 2,
                "children": 1,
                "price": 320.00,
                "fullPrice": 320.00,
                "daysStaying": 1
            }""")})), @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "message": "Validation errors:",
                "errors": [
                    "Adults value should be positive value",
                    "The end date for transfer must be present or future date",
                    "Children value should be positive or zero value",
                    "Adults value should be positive value",
                    "The start date for transfer must be present or future date"
                ]
            }"""))), @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "error": "Room for reservation not found"
            }"""))), @ApiResponse(responseCode = "406", description = "Not Acceptable", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                 {
                     "error": "Required logged user!"
                     "error": "Invalid input data. Please check input 2023-05-145 and try again with format yyyy-MM-dd."
                 }
            """)))})
    public ResponseEntity<RoomReservationResponseDto> saveReservation(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(schema = @Schema(implementation = RoomReservationRequestDto.class)))
            @Valid RoomReservationRequestDto reservationRequestDto,
            @RequestHeader("Authorization") String token) throws ValidationException {
        return ResponseEntity.status(HttpStatus.CREATED).body(roomReservationService.saveReservation(
                reservationRequestDto, jwtService.extractUserName(token.substring(7))));
    }

    @GetMapping(path = "available-rooms")
    @Operation(summary = "Get available rooms by dates, adults and children", description = "Get a list of available rooms by start and end date, and people")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "406", description = "Not Acceptable",
                    content = @Content(mediaType = "application/json",
                            examples = {@ExampleObject(value =
                                    "{" +
                                            "    \"Invalid input data\": \"Please check startDate parameter and try again!\"" +
                                            "}")})),
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of available rooms for the specified number of adults",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Room.class))),
            @ApiResponse(responseCode = "303", description = "Successfully retrieved list of available rooms depending on the number of adults",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Room.class))),
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
    public ResponseEntity<List<Room>> getAvailableRooms(
            @RequestParam("start-date")
            @Parameter(name = "start-date",
                    description = "The start date for check of available rooms, in format yyyy-MM-dd",
                    example = "2023-05-10", required = true)
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            @FutureOrPresent(message = "Enter valid present/future date!")
            LocalDate startDate,
            @RequestParam("end-date")
            @Parameter(name = "end-date",
                    description = "The end date for check of available rooms, in format yyyy-MM-dd",
                    example = "2023-05-10", required = true)
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            @FutureOrPresent(message = "Enter valid present/future date!")
            LocalDate endDate,
            @Param("adults")
            @PositiveOrZero(message = "Number of adults must be zero or positive value")
            @Parameter(name = "adults",
                    description = "The number of adults for check of available rooms",
                    example = "6")
            @Digits(integer=2, fraction=0, message="Adults field must have digits")
            Integer adults,
            @Param("children")
            @PositiveOrZero(message = "Number of children must be zero or positive value")
            @Parameter(name = "children",
                    description = "The number of children for check of available rooms",
                    example = "10")
            @Digits(integer=2, fraction=0, message="Children field must have digits")
            Integer children
    ) {
        if (Objects.isNull(children)) {
            children = 0;
        }
        if (Objects.isNull(adults)) {
            adults = 0;
        }
        try {
            return ResponseEntity.status(HttpStatus.OK).body(roomReservationService.getAvailableRooms(startDate, endDate, adults, children));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.SEE_OTHER).body(roomReservationService.getAlternativeRooms(startDate, endDate, adults, children));
        }
    }
    @PutMapping("updatePayment")
    @RolesAllowed({"ROLE_ADMIN"})
    @Operation(summary = "Update room reservation payment status", description = "Update room reservation payment status by providing room reservation's ID, and payment status.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RoomReservationUpdateResponseDto.class), examples = {@ExampleObject(value = """                                                                    
            {
                 "id": 1,
                 "startDate": "2023-05-17",
                 "endDate": "2023-05-18",
                 "adults": 2,
                 "children": 0,
                 "room": {
                     "id": 3,
                     "roomNumber": 120,
                     "type": "STANDARD",
                     "view": "POOL",
                     "price": 220.00,
                     "people": 2,
                     "facilities": []
                 },
                 "status": "PAID",
                 "type": "TWIN_BEDS"
             }""")})), @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "errors:": [
                        "Input valid payment status: PAID or UNPAID"
                    ]
                ]
            }"""))), @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                    "error": "No existing reservation with provided ID"
            }"""))), @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                     "error": "You do not have permission to access this resource"
            """)))})
    public ResponseEntity<RoomReservationUpdateResponseDto> updateRoomReservationPaymentStatus(
            @RequestHeader("Authorization") String authorization,
            @RequestBody @Valid RoomReservationUpdateRequestDto roomUpdate) {
        return ResponseEntity.ok(roomReservationService.updateRoomReservationPaymentStatus(roomUpdate));
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update room's reservation parameters",
            description = "Update room reservation parameters by providing room reservation's ID and start date or end date, or room's view, or room type, or adults value, or children value, or bed type.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoomReservationUpdateResponseDto.class),
                            examples = @ExampleObject(value = """
                            {
                                "id": 1,
                                "startDate": "2023-05-17",
                                "endDate": "2023-05-18",
                                "adults": 2,
                                "children": 0,
                                "room": {
                                    "id": 3,
                                    "roomNumber": 120,
                                    "type": "STANDARD",
                                    "view": "POOL",
                                    "price": 220.00,
                                    "people": 2,
                                    "facilities": []
                                },
                                "status": "PAID",
                                "type": "TWIN_BEDS"
                            }
                        """))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(value = """
                            {
                                "errors": [
                                    "Room reservation ID is a required parameter",
                                    "Please provide a valid start date!",
                                    "Please provide a valid start date that is before the end date!",
                                    "Please provide a valid end date!",
                                    "Please provide a valid end date that is after the start date!",
                                    "Please input a valid room type: STANDARD, STUDIO, or APARTMENT",
                                    "Please input a valid room view: SEA, GARDEN, or POOL",
                                    "Please provide a positive value for adults!",
                                    "Please provide a positive value for children or zero!",
                                    "Please input a valid bed type: TWIN_BEDS or BEDROOM"
                                ]
                            }
                        """))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(value = """
                            {
                                "error": "No existing reservation with the provided ID",
                                "error": "Sorry! We can't change the start date because the room is occupied for this date.",
                                "error": "Sorry! We can't change the end date because the room is occupied for this date.",
                                "error": "Sorry! We can't change the room because all rooms with the requested view are occupied.",
                                "error": "Sorry! We can't change the room type because all rooms with the requested type are occupied.",
                                "error": "Sorry! We can't change the adults because there are no available rooms with the requested value."
                            }
                        """))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(value = """
                            {
                                "error": "You do not have permission to access this resource"
                            }
                        """)))
    })
    public ResponseEntity<RoomReservationUpdateResponseDto> updateRoomReservationRoomView(
            @RequestHeader("Authorization") String authorization,
            @RequestBody @Valid RoomReservationUpdateRequestDto roomUpdate) {
        return ResponseEntity.ok(roomReservationService.updateRoomReservationParameters(roomUpdate));
    }

}
