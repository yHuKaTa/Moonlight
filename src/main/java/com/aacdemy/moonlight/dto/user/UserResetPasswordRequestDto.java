package com.aacdemy.moonlight.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResetPasswordRequestDto {

    @NotBlank(message = "Email is required")
    private String email;
}
