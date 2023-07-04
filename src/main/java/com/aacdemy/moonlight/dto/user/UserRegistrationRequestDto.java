package com.aacdemy.moonlight.dto.user;

import com.aacdemy.moonlight.util.annotation.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationRequestDto {

    @NotBlank(message = "First name is required")
    @Size(max = 20)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 20)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE, message = "Invalid email address")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Size(max = 20)
    @PhoneNumber(message = "Doesn't seem to be a valid phone number")
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=(.*[a-z]){1,})(?=(.*[\\d]){1,})(?=(.*[\\W]){1,})(?!.*\\s).{8,}$",
            message = "Your new password should have " +
                    "at least one upper case English letter, " +
                    "at least one lower case English letter, " +
                    "at least one digit, " +
                    "at least one special character," +
                    " minimum eight in length")
    private String password;

}