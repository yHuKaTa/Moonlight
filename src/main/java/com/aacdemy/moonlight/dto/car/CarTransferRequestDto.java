package com.aacdemy.moonlight.dto.car;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CarTransferRequestDto {
    @Pattern(regexp = "[\\d]{1,2}", message = "The car's ID field should consist of 1 to 2 digits")
    @NotBlank(message = "The car's ID field shouldn't be blank!")
    private String carId;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    @NotNull(message = "The date for transfer shouldn't be blank!")
    @FutureOrPresent(message = "Please enter a valid present/future date!")
    private LocalDate date;

}
