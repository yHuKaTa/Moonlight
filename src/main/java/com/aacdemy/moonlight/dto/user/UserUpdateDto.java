package com.aacdemy.moonlight.dto.user;

import com.aacdemy.moonlight.util.annotation.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserUpdateDto {

    private String ID;

    private String firstName;

    private String lastName;

    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE, message = "Invalid email address")
    private String email;
    @PhoneNumber(message = "Doesn't seem to be a valid phone number")
    private String phoneNumber;

    private String passportID; //password ID is not required

}
