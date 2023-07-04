package com.aacdemy.moonlight.controller;

import com.aacdemy.moonlight.config.security.JwtService;
import com.aacdemy.moonlight.dto.screen.ScreenReservationRequestDto;
import com.aacdemy.moonlight.dto.screen.ScreenReservationResponseDto;
import com.aacdemy.moonlight.dto.screen.ScreenReservationSearchResponseDto;
import com.aacdemy.moonlight.entity.screen.ScreenSeat;
import com.aacdemy.moonlight.service.ScreenReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

@RestController
@Validated
@RequestMapping("/screen-reservations")
public class ScreenReservationController {
    private final ScreenReservationService screenReservationService;

    private final JwtService jwtService;

    @Autowired
    public ScreenReservationController(ScreenReservationService screenReservationService, JwtService jwtService) {
        this.screenReservationService = screenReservationService;
        this.jwtService = jwtService;
    }

    @PostMapping("/add-reservation")
    @PreAuthorize("isAuthenticated")
    @Operation(summary = "Add new event's seats reservation", description = "Add reservation for event's seats by providing date, event ID, required seat numbers.")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ScreenReservationResponseDto.class), examples = {@ExampleObject(value = """                                                                    
            {
                "event": {
                    "id": 3,
                    "event": "SportEvent",
                    "dateEvent": "2023-06-01",
                    "screen": {
                        "id": 1,
                        "name": "ScreenOne"
                    }
                },
                "reservedSeats": [
                    {
                        "id": 17,
                        "seatPosition": 17,
                        "screen": {
                            "id": 1,
                            "name": "ScreenOne"
                        }
                    }
                ]
            }""")})), @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "message": "Validation errors:",
                "errors": [
                    "Seat's numbers shouldn't be blank!",
                    "The seat's numbers should be between 1 and 21",
                    "The event ID shouldn't be blank!",
                    "Event ID should be positive value",
                    "The date for event reservation shouldn't be blank!"
                ]
            }"""))), @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                    "error": "Event not found"
            }"""))), @ApiResponse(responseCode = "406", description = "Not Acceptable", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                 {
                     "error": "Required logged user!"
                     "error": "Invalid input data. Please check input 2023-06-015 and try again with format yyyy-MM-dd."
                 }
            """)))})
    public ResponseEntity<ScreenReservationResponseDto> addScreenReservation(@RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(schema = @Schema(implementation = ScreenReservationRequestDto.class))) @Valid ScreenReservationRequestDto screenReservationRequestDto, @RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(screenReservationService.addReservation(screenReservationRequestDto, jwtService.extractUserName(token.substring(7))));
    }

    @GetMapping("/available-seats/{eventId}")
    @Operation(summary = "Get free seats for an event", description = "Retrieve the list of free seats for a specific event.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ScreenSeat.class))))})
    public ResponseEntity<List<ScreenSeat>> getFreeSeatsForEvent(@PathVariable Long eventId) {
        List<ScreenSeat> freeSeats = screenReservationService.getFreeSeatsForEvent(eventId);
        return ResponseEntity.ok(freeSeats);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(path = "/get-all")
    @Operation(summary = "Gets all screen reservations")
    public ResponseEntity<List<ScreenReservationSearchResponseDto>> getAllScreenReservations() {
        List<ScreenReservationSearchResponseDto> screenReservationList = screenReservationService.findAll();
        return ResponseEntity.ok(screenReservationList);
    }

    @Operation(summary = "Gets screen reservations for currently logged user")
    @PreAuthorize("isAuthenticated")
    @GetMapping("/current-user")
    public ResponseEntity<List<ScreenReservationSearchResponseDto>> getByCurrentUser(@RequestHeader("Authorization") String token)
            throws ParseException {
        List<ScreenReservationSearchResponseDto> screenReservationList = screenReservationService.findByCurrentlyLoggedUser(token);
        return ResponseEntity.ok(screenReservationList);
    }
    @GetMapping()
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Gets screen reservations for a date")
    public ResponseEntity<List<ScreenReservationSearchResponseDto>> findScreenReservationsByDate
            (
                    @RequestParam("date")
                    @Parameter(name = "date", required = true)
                    @DateTimeFormat(pattern = "yyyy-MM-dd")
                    LocalDate date
            )
    {
        List<ScreenReservationSearchResponseDto> screenReservations = screenReservationService.findScreenReservationsByDate(date);
        return ResponseEntity.ok(screenReservations);
    }
}
