package com.aacdemy.moonlight.dto.user;

import com.aacdemy.moonlight.entity.user.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
public class UserResponseDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String passportID;

    private UserRole role;

    private boolean enabled;

    protected Date createdDate;

    protected Date modifiedDate;

}
