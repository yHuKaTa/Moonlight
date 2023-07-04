package com.aacdemy.moonlight.controller;

import com.aacdemy.moonlight.entity.restaurant.TableRestaurant;
import com.aacdemy.moonlight.entity.restaurant.TableZone;
import com.aacdemy.moonlight.service.TableReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/restaurant")
public class TableRestaurantController {

    private final TableReservationService tableReservationService;

    @Autowired
    public TableRestaurantController(TableReservationService tableReservationService) {
        this.tableReservationService = tableReservationService;
    }

    @Operation(summary = "Get available table according params",
            description = "Retrieves available table  reservation according date, hour, zone, isSmoking, tableId, people.")
    @PreAuthorize("isAuthenticated or hasAuthority('ROLE_ADMIN')")
    @GetMapping()
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TableRestaurant.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 500,
                "error": "Internal Server Error",
                "path": "/restaurant"
            }""")))
    })
    public ResponseEntity<List<TableRestaurant>> findAvailableTableByDateTimeZoneIsSmokingTableIdPeople
    (@Parameter(name = "The date to check availability for, in format yyyy-MM-dd", example = "2023-05-10", required = true)
     @RequestParam(name = "date") LocalDate date,@RequestHeader("Authorization")

     @Parameter(name = "The hour to check availability for, in format HH", example = "18")
     @RequestParam(name = "time")  @DateTimeFormat(pattern = "HH")Optional<LocalTime> hour,

     @Parameter(name = "The zone to check availability for", example = "SALON")
     @RequestParam(name = "zone") Optional<TableZone> zone,

     @Parameter(name = "Availability for is smoking or non-smoking", example = "true")
     @RequestParam(name = "isSmoking") Optional<Boolean> isSmoking,

     @Parameter(name = "Availability for is smoking or non-smoking", example = "true")
     @RequestParam(name = "tableId") Optional<Long> tableId,

     @Parameter(name = "Availability for is smoking or non-smoking", example = "true")
     @RequestParam(name = "people") Optional<Integer> seats) {

        List<TableRestaurant> tableRestaurants = tableReservationService
                .findAvailableTableByDateTimeZoneIsSmokingTableIdPeople(
                        date, hour, zone, isSmoking, tableId, seats
                );

        return ResponseEntity.ok(tableRestaurants);
    }
}
