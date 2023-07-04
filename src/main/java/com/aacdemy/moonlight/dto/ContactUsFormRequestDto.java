package com.aacdemy.moonlight.dto;

import com.aacdemy.moonlight.util.annotation.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactUsFormRequestDto {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE, message = "Invalid email address")
    private String email;

    @PhoneNumber(message = "Doesn't seem to be a valid phone number")
    private String phoneNumber;

    @NotBlank(message = "Message is required")
    private String message;

}