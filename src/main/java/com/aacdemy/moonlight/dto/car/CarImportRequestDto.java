package com.aacdemy.moonlight.dto.car;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CarImportRequestDto {
    @Pattern(regexp = "[A-Za-z]{2,45}", message = "The car's brand field should be valid format with characters")
    @Size(min = 2, max = 45, message = "The car's brand field should consist of 2 to 45 characters")
    @NotBlank(message = "The car's brand field shouldn't be blank!")
    private String make;

    @Pattern(regexp = "[\\dA-Za-z]{1,45}", message = "The car's model field should be valid format with characters and digits")
    @Size(min = 1, max = 45, message = "The car's model field should consist of 1 to 45 characters")
    @NotBlank(message = "The car's model field shouldn't be blank!")
    private String model;

    @Pattern(regexp = "[\\d]{4,}", message = "The car's year field should consist of 4 digits")
    @NotBlank(message = "The car's year field shouldn't be blank!")
    private String year;

    @Pattern(regexp = "[\\d]{1,}", message = "The car category ID field should consist digits")
    @NotBlank(message = "The car category ID field shouldn't be blank!")
    private String carCategoryId;

}
