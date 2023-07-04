package com.aacdemy.moonlight.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ContactUsFormResponseDto {

    private Long id;
    private String name;
    private String phoneNumber;
    private String email;
    private String message;
    @Builder.Default
    private String note = "Your message was successfully sent!";

}
