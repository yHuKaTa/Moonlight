package com.aacdemy.moonlight.dto.room;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class RoomFacilityUpdRequestDto {

    @NotNull(message = "Facility name is required")
    @NotBlank(message = "Insert valid facility name")
    @Size(max = 50)
    private String currentFacility;

    @NotNull(message = "Facility name is required")
    @NotBlank(message = "Insert valid facility name")
    @Size(max = 50)
    private String newFacility;

    @JsonCreator
    public RoomFacilityUpdRequestDto(String currentFacility, String newFacility) {
        this.currentFacility = currentFacility;
        this.newFacility = newFacility;
    }

}
