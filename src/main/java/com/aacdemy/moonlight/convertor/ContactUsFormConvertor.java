package com.aacdemy.moonlight.convertor;

import com.aacdemy.moonlight.dto.ContactUsFormRequestDto;
import com.aacdemy.moonlight.dto.ContactUsFormResponseDto;
import com.aacdemy.moonlight.entity.ContactUsForm;
import com.aacdemy.moonlight.repository.ContactUsFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContactUsFormConvertor {


    private final ContactUsFormRepository contactUsFormRepository;

    @Autowired
    public ContactUsFormConvertor(ContactUsFormRepository contactUsFormRepository) {
        this.contactUsFormRepository = contactUsFormRepository;
    }

    public ContactUsForm toContactUsForm(ContactUsFormRequestDto contactUsFormRequestDto) {
        ContactUsForm contactUsForm = ContactUsForm.builder()
                .name(contactUsFormRequestDto.getName())
                .email(contactUsFormRequestDto.getEmail())
                .phoneNumber(contactUsFormRequestDto.getPhoneNumber())
                .message(contactUsFormRequestDto.getMessage())
                .build();
        contactUsFormRepository.save(contactUsForm);
        return contactUsForm;
    }

    public ContactUsFormResponseDto toContactUsFormResponse(ContactUsForm contactUsForm) {
        ContactUsFormResponseDto contactUsFormResponseDto = ContactUsFormResponseDto.builder()
                .id(contactUsForm.getId())
                .name(contactUsForm.getName())
                .email(contactUsForm.getEmail())
                .phoneNumber(contactUsForm.getPhoneNumber())
                .message(contactUsForm.getMessage())
                .build();
        return contactUsFormResponseDto;
    }
}
