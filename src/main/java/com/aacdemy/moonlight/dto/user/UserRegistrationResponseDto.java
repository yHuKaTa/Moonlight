package com.aacdemy.moonlight.dto.user;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserRegistrationResponseDto {

    @Builder.Default
    String message = "Your registration to Moonlight Hotel & Spa successful was successful. " +
            "Your login details were sent to:";

    String email;

    @Builder.Default
    String status = "201";

    String dateCreated;

    private String token;

}
