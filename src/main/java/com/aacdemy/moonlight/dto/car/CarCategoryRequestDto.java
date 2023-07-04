package com.aacdemy.moonlight.dto.car;

import com.aacdemy.moonlight.util.annotation.ValidCarTypeEnum;
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
public class CarCategoryRequestDto {
    @Pattern(regexp = "[A-Za-z]{3,45}", message = "The car's type field should be valid format with characters")
    @Size(min = 3, max = 45, message = "The car's type field should consist of 3 to 45 characters")
    @ValidCarTypeEnum
    @NotBlank(message = "The car's type field shouldn't be blank!")
    private String carType;

    @Pattern(regexp = "[\\d]{1,2}", message = "The car's seats field should consist of 1 to 2 digits")
    @NotBlank(message = "The car's seats field shouldn't be blank!")
    private String seats;

    @Pattern(regexp = "(0|([1-9][0-9]+))\\.?[\\d]+", message = "The car's price per day field should be valid format with digits")
    @NotBlank(message = "The car's price per day field shouldn't be blank!")
    private String pricePerDay;
}
