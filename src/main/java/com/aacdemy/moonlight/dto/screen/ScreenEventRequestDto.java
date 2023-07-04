package com.aacdemy.moonlight.dto.screen;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScreenEventRequestDto {
    @NotBlank(message = "Name is required field")
    String name;

    @NotNull(message = "Screen Id is required field")
    Long screenId;

    @NotNull(message = "Date is required field")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "The date must be present or future")
    LocalDate date;
}
