package com.aacdemy.moonlight.dto.room;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class RoomFacilityRequestDto {

    @NotNull(message = "Facility name is required")
    @NotBlank(message = "Insert valid facility name")
    @Size(max = 50)
    private String facility;

    @JsonCreator
    public RoomFacilityRequestDto(String facility) {
        this.facility = facility;
    }

}
