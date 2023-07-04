package com.aacdemy.moonlight.controller;

import com.aacdemy.moonlight.dto.room.RoomFacilityRequestDto;
import com.aacdemy.moonlight.dto.room.RoomFacilityResponseDto;
import com.aacdemy.moonlight.dto.room.RoomResponseDto;
import com.aacdemy.moonlight.entity.hotel.Room;
import com.aacdemy.moonlight.entity.hotel.RoomType;
import com.aacdemy.moonlight.entity.hotel.RoomView;
import com.aacdemy.moonlight.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find room by ID", description = "Returns a single room based on the ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RoomResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                                    "status": 404,
                                    "error": "Room not found",
                                    "path": "/rooms/{id}"
                                }
                            """))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                                    "status": 500,
                                    "error": "Internal Server Error",
                                    "path": "/rooms/{id}"
                                }
                            """)))
    })
    public ResponseEntity<Room> findById(@PathVariable Long id) {
        Room room = roomService.findById(id);
            return ResponseEntity.ok(room);
    }

    @GetMapping("/room-number/{roomNumber}")
    @Operation(summary = "Find room by number", description = "Returns a single room based on the number.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RoomResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                                    "status": 404,
                                    "error": "Room not found",
                                    "path": "/rooms/room-number/{roomNumber}"
                                }
                            """))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                                    "status": 500,
                                    "error": "Internal Server Error",
                                    "path": "/rooms/room-number/{roomNumber}"
                                }
                            """)))
    })
    public ResponseEntity<Room> findByRoomNumber(@PathVariable int roomNumber) {
        Room room = roomService.findByRoomNumber(roomNumber);
        return ResponseEntity.ok(room);
    }

    @GetMapping("/room-type/{type}")
    @Operation(summary = "Find room by type", description = "Returns a single room based on the type.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RoomResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                                    "status": 404,
                                    "error": "Room not found",
                                    "path": "/rooms/room-type/{type}"
                                }
                            """))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                                    "status": 500,
                                    "error": "Internal Server Error",
                                    "path": "/rooms/room-type/{type}"
                                }
                            """)))
    })
    public ResponseEntity<List<Room>> findByRoomType(@PathVariable Object type) {
        if (Objects.isNull(type) || (type.toString().isEmpty()) || !((type.toString().matches("((?i)(STANDARD)|(?i)(STUDIO)|(?i)(APARTMENT))")))) {
            return ResponseEntity.notFound().build();
        } else {
            RoomType reqType = null;
            if (type.toString().equalsIgnoreCase("standard")) {
                reqType = RoomType.STANDARD;
            } else if (type.toString().equalsIgnoreCase("studio")) {
                reqType = RoomType.STUDIO;
            } else if (type.toString().equalsIgnoreCase("apartment")) {
                reqType = RoomType.APARTMENT;
            }
            List<Room> rooms = roomService.findByRoomType(reqType);
            return ResponseEntity.ok(rooms);
        }
    }

    @GetMapping("/room-view/{view}")
    @Operation(summary = "Find room by view", description = "Returns a single room based on the view.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RoomResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                                    "status": 404,
                                    "error": "Room not found",
                                    "path": "/rooms/room-view/{view}"
                                }
                            """))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                                    "status": 500,
                                    "error": "Internal Server Error",
                                    "path": "/rooms/room-view/{view}"
                                }
                            """)))
    })
    public ResponseEntity<List<Room>> findByRoomView(@PathVariable Object view) {
        if (Objects.isNull(view) || (view.toString().isEmpty()) || !((view.toString().matches("((?i)(sea)|(?i)(garden)|(?i)(pool))")))) {
            return ResponseEntity.notFound().build();
        } else {
            RoomView roomView = null;
            if (view.toString().equalsIgnoreCase("sea")) {
                roomView = RoomView.SEA;
            } else if (view.toString().equalsIgnoreCase("garden")) {
                roomView = RoomView.GARDEN;
            } else if (view.toString().equalsIgnoreCase("pool")) {
                roomView = RoomView.POOL;
            }
            List<Room> rooms = roomService.findByRoomView(roomView);
            return ResponseEntity.ok(rooms);
        }
    }

    @GetMapping("/room-price/{price}")
    @Operation(summary = "Find room by price", description = "Returns a single room based on the price.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RoomResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                                    "status": 404,
                                    "error": "Room not found",
                                    "path": "/rooms/room-price/{price}"
                                }
                            """))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                                    "status": 500,
                                    "error": "Internal Server Error",
                                    "path": "/rooms/room-price/{price}"
                                }
                            """)))
    })
    public ResponseEntity<List<Room>> findByPrice(@PathVariable BigDecimal price) {
        List<Room> rooms = roomService.findByPrice(price);
        if (rooms.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(rooms);
        }
    }

    @GetMapping("/people/{people}")
    @Operation(summary = "Find room by people", description = "Returns a single room based on the people.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RoomResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                                    "status": 404,
                                    "error": "Room not found",
                                    "path": "/rooms/people/{people}"
                                }
                            """))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                                    "status": 500,
                                    "error": "Internal Server Error",
                                    "path": "/rooms/people/{people}"
                                }
                            """)))
    })
    public ResponseEntity<List<Room>> findByPeople(@PathVariable int people) {
        List<Room> rooms = roomService.findByPeople(people);
        if (rooms.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(rooms);
        }
    }

    @GetMapping("/search/{facility}")
    @Operation(summary = "Search rooms by facility", description = "Returns a list of rooms based on the facility.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RoomResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                                    "status": 404,
                                    "error": "Not Found",
                                    "message": "Room not found",
                                    "path": "/rooms/search/{facility}"
                                }
                            """))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2023-04-25T15:27:05.776+00:00",
                                    "status": 500,
                                    "error": "Internal Server Error",
                                    "message": "An internal server error occurred",
                                    "path": "/rooms/search/{facility}"
                                }
                            """)))
    })
    public ResponseEntity<List<Room>> searchRoomsByFacility(@PathVariable String facility) {
        List<Room> rooms = roomService.findRoomsByFacility(facility);
        if (rooms.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(rooms);
        }
    }

    //DELETE localhost:8080/rooms/{roomId}/facilities
    //{
    //    "facility": "Wi-fi"
    //}
    @Operation(summary = "Delete facility from specific room",
            description = "Deletes the specified facility from room identified by the given roomId.")
    @DeleteMapping("/{roomId}/facilities")
    @RolesAllowed({"ROLE_ADMIN"})
    public ResponseEntity<RoomFacilityResponseDto> deleteFacilityFromRoom(
            @PathVariable Long roomId,
            @Parameter(description = "Authorization token", required = true, example = "Bearer <token>")
            @RequestHeader("Authorization") String authorization,
            @RequestBody RoomFacilityRequestDto roomFacilityRequestDto) {
        String token = authorization.substring(7);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(roomService.deleteFacilityFromRoom(roomId, roomFacilityRequestDto));
    }

    //PUT localhost:8080/rooms/{roomId}/facilities
    //{
    //    "facility": "Wi-fi"
    //}
    @Operation(summary = "Add facility to a specific room",
            description = "Adds the specified facility to the room identified by the given roomId.")
    @PutMapping("/{roomId}/facilities")
    @RolesAllowed({"ROLE_ADMIN"})
    public ResponseEntity<RoomFacilityResponseDto> addFacilityToRoom(
            @PathVariable Long roomId,
            @Parameter(description = "Authorization token", required = true, example = "Bearer <token>")
            @RequestHeader("Authorization") String authorization,
            @RequestBody RoomFacilityRequestDto roomFacilityRequestDto) {
        String token = authorization.substring(7);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(roomService.addFacilityToRoom(roomId, roomFacilityRequestDto));
    }
}
