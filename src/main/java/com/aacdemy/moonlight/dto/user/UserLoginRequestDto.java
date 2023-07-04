package com.aacdemy.moonlight.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
public class UserLoginRequestDto {

    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
