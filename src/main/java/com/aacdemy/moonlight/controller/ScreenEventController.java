package com.aacdemy.moonlight.controller;

import com.aacdemy.moonlight.dto.screen.ScreenEvenResponseDto;
import com.aacdemy.moonlight.dto.screen.ScreenEventRequestDto;
import com.aacdemy.moonlight.entity.screen.ScreenEvent;
import com.aacdemy.moonlight.service.ScreenEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/screen-event")
public class ScreenEventController {

    private final ScreenEventService screenEventService;

    @PostMapping("/add-event")
    @PreAuthorize("isAuthenticated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ScreenEvenResponseDto.class), examples = {@ExampleObject(value = """                                                                    
                    {
                      "name": "string",
                      "screenId": 0,
                      "date": "2023-06-11"
                    }""")})),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                    {
                        "message" : "Validation errors:",
                        "errors" : [
                            "The name is required!",
                            "The screen Id is required!",
                            "The date is required!",
                        ]
                    }"""))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                    {
                            "timestamp": "2023-04-25T15:12:55.260+00:00",
                            "status": 403,
                            "error": "Forbidden",
                            "path": "/screen-event/add-event"
                    }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                    {
                            "timestamp": "2023-04-25T15:27:05.776+00:00",
                            "status": 500,
                            "error": "Internal Server Error",
                            "path": "/screen-event/add-event"
                    }""")))
    })
    public ResponseEntity<ScreenEvent> addScreenEvent( @RequestHeader("Authorization") String authorization,
                                                       @RequestBody @Valid ScreenEventRequestDto screenEventRequestDto){
        String token = authorization.substring(7);
        return ResponseEntity.status(HttpStatus.OK)
                .body(screenEventService.addScreenEvent(screenEventRequestDto));
    }
    @GetMapping("/event")
    @Operation(summary = "Get available event by date", description = "Get a list of available event by a specified date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ScreenEvenResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T16:06:58.625+00:00",
                "status": 404,
                "error": "Not Found",
                "message": "Event not found",
                "path": "/screen-event/event"
            }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T16:06:58.625+00:00",
                "status": 500,
                "error": "Internal Server Error",
                "message": "Internal server error occurred",
                "path": "/screen-event/event"
            }""")))
    })
    public ResponseEntity<List<ScreenEvent>> findAvailableEventsByDate(
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        return ResponseEntity.ok(screenEventService.findAvailableEventsByDate(date));
    }
}
