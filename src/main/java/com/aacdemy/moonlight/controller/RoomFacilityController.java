package com.aacdemy.moonlight.controller;

import com.aacdemy.moonlight.dto.room.RoomFacilityRequestDto;
import com.aacdemy.moonlight.dto.room.RoomFacilityUpdRequestDto;
import com.aacdemy.moonlight.entity.hotel.RoomFacility;
import com.aacdemy.moonlight.service.RoomFacilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/facility")
public class RoomFacilityController {
    private final RoomFacilityService roomFacilityService;

    @Autowired
    public RoomFacilityController(RoomFacilityService roomFacilityService) {
        this.roomFacilityService = roomFacilityService;
    }

    //POST localhost:8080/facility
    //{
    //    "facility": "Wi-fi"
    //}
    @Operation(summary = "Add facility",
            description = "Add the specified facility.")
    @PostMapping
    @RolesAllowed({"ROLE_ADMIN"})
    public ResponseEntity<RoomFacility> addFacility(
            @RequestBody @Valid RoomFacilityRequestDto roomFacilityRequestDto) {
        return ResponseEntity.ok(roomFacilityService.addFacility(roomFacilityRequestDto));
    }

    //GET localhost:8080/facility?facility=TV

    @Operation(summary = "Get facility",
            description = "Get facility info by name.")
    @GetMapping
    @RolesAllowed({"ROLE_ADMIN"})
    public ResponseEntity<RoomFacility> getFacility(
            @Parameter(name = "The name of room facility", example = "TV")
            @RequestParam("facility")
            @Size(max = 10) String facility) {
        return ResponseEntity.ok(roomFacilityService.getFacility(facility));
    }

    //GET localhost:8080/facility/all

    @Operation(summary = "Get all facilities",
            description = "Get all facilities info")
    @GetMapping(path = "/all")
    @RolesAllowed({"ROLE_ADMIN"})
    public ResponseEntity<List<RoomFacility>> getAllFacilities() {
        return ResponseEntity.ok(roomFacilityService.getAllFacilities());
    }

    //DELETE localhost:8080/facility
    //{
    //    "facility": "Wi-fi"
    //}
    @Operation(summary = "Delete facility",
            description = "Deletes the specified facility.")
    @DeleteMapping
    @RolesAllowed({"ROLE_ADMIN"})
    public ResponseEntity<HttpStatus> deleteFacility(
            @RequestBody @Valid RoomFacilityRequestDto roomFacilityRequestDto) {
        roomFacilityService.deleteFacility(roomFacilityRequestDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //PUT localhost:8080/facility
    //{
    //    "currentFacility" = "TV",
    //    "newFacility" = "TV+Soundbar"
    //}
    @Operation(summary = "Update facility",
            description = "Update the specified facility.")
    @PutMapping
    @RolesAllowed({"ROLE_ADMIN"})
    public ResponseEntity<RoomFacility> updateFacility(
            @RequestBody @Valid RoomFacilityUpdRequestDto roomFacilityUpdRequestDto) {
        return ResponseEntity.ok(roomFacilityService.updateFacility(roomFacilityUpdRequestDto));
    }
}
