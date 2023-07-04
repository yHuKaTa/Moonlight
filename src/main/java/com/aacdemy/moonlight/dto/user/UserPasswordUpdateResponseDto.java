package com.aacdemy.moonlight.dto.user;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class UserPasswordUpdateResponseDto {

    @Builder.Default
    private String message = "Password for Moonlight Hotel & Spa's account successfully changed.";

    private Date dateModified;

}
