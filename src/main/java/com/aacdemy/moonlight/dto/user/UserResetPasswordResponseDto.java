package com.aacdemy.moonlight.dto.user;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserResetPasswordResponseDto {

    @Builder.Default
    private String message = "Your password for Moonlight Hotel & Spa was sent to the provided e-mail address.";
}
