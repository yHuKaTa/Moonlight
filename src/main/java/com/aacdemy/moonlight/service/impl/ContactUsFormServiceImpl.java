package com.aacdemy.moonlight.service.impl;

import com.aacdemy.moonlight.convertor.ContactUsFormConvertor;
import com.aacdemy.moonlight.dto.ContactUsFormRequestDto;
import com.aacdemy.moonlight.dto.ContactUsFormResponseDto;
import com.aacdemy.moonlight.entity.ContactUsForm;
import com.aacdemy.moonlight.repository.ContactUsFormRepository;
import com.aacdemy.moonlight.service.ContactUsFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactUsFormServiceImpl implements ContactUsFormService {

    private final ContactUsFormRepository contactUsFormRepository;
    private final ContactUsFormConvertor contactUsFormConvertor;

    @Autowired
    public ContactUsFormServiceImpl(ContactUsFormRepository contactUsFormRepository, ContactUsFormConvertor contactUsFormConvertor) {
        this.contactUsFormRepository = contactUsFormRepository;
        this.contactUsFormConvertor = contactUsFormConvertor;
    }

    @Override
    public ContactUsFormResponseDto save(ContactUsFormRequestDto contactUsFormRequestDto) {
        ContactUsForm contactUsForm = contactUsFormConvertor.toContactUsForm(contactUsFormRequestDto);

        return contactUsFormConvertor.toContactUsFormResponse(contactUsForm);
    }

    @Override
    public void deleteById(Long id) {
        contactUsFormRepository.deleteById(id);

    }

    @Override
    public List<ContactUsFormResponseDto> getContactUsFormsByPhoneNumber(String phoneNumber) {
        return contactUsFormRepository
                .findByPhoneNumber(phoneNumber)
                .stream()
                .map(contactUsFormConvertor::toContactUsFormResponse)
                .toList();
    }

    @Override
    public List<ContactUsFormResponseDto> getContactUsFormsByName(String name) {
        return contactUsFormRepository
                .findByName(name)
                .stream()
                .map(contactUsFormConvertor::toContactUsFormResponse)
                .toList();
    }

    @Override
    public List<ContactUsFormResponseDto> getContactUsFormsByText(String text) {
        return contactUsFormRepository
                .findByMessageContaining(text)
                .stream()
                .map(contactUsFormConvertor::toContactUsFormResponse)
                .toList();
    }
}
