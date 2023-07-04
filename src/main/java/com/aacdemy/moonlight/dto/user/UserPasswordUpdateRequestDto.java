package com.aacdemy.moonlight.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPasswordUpdateRequestDto {

    @NotBlank
    private String currentPassword;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=(.*[a-z]){1,})(?=(.*[\\d]){1,})(?=(.*[\\W]){1,})(?!.*\\s).{8,}$",
            message = """
                    Your new password should have:
                    at least one upper case English letter
                    at least one lower case English letter
                    at least one digit
                    at least one special character
                    minimum eight in length""")
    private String newPassword;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
}
