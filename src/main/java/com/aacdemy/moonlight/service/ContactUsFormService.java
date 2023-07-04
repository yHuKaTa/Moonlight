package com.aacdemy.moonlight.service;

import com.aacdemy.moonlight.dto.ContactUsFormRequestDto;
import com.aacdemy.moonlight.dto.ContactUsFormResponseDto;

import java.util.List;

public interface ContactUsFormService {
    ContactUsFormResponseDto save(ContactUsFormRequestDto contactUsForm);

    void deleteById(Long id);

    List<ContactUsFormResponseDto> getContactUsFormsByPhoneNumber(String phoneNumber);

    List<ContactUsFormResponseDto> getContactUsFormsByName(String name);

    List<ContactUsFormResponseDto> getContactUsFormsByText(String text);

}
