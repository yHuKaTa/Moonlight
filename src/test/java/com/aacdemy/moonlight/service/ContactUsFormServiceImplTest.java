package com.aacdemy.moonlight.service;

import com.aacdemy.moonlight.convertor.ContactUsFormConvertor;
import com.aacdemy.moonlight.dto.ContactUsFormRequestDto;
import com.aacdemy.moonlight.dto.ContactUsFormResponseDto;
import com.aacdemy.moonlight.entity.ContactUsForm;
import com.aacdemy.moonlight.repository.ContactUsFormRepository;
import com.aacdemy.moonlight.service.impl.ContactUsFormServiceImpl;
import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class ContactUsFormServiceImplTest {
    @Mock
    private ContactUsFormRepository contactUsFormRepository;

    @Mock
    private ContactUsFormService contactUsFormService;
    @Mock
    private ContactUsFormConvertor contactUsFormConvertor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        contactUsFormService = new ContactUsFormServiceImpl(
                contactUsFormRepository,
                contactUsFormConvertor);
    }

    @Test
    void testSave() {
        ContactUsFormRequestDto requestDto = ContactUsFormRequestDto.builder()
                .email("test@test.com")
                .message("test message")
                .name("Test")
                .phoneNumber("0123456789")
                .build();
        ContactUsForm form = ContactUsForm.builder()
                .id(1L)
                .email(requestDto.getEmail())
                .message(requestDto.getMessage())
                .name(requestDto.getName())
                .phoneNumber(requestDto.getPhoneNumber())
                .build();

        ContactUsFormResponseDto responseDto = ContactUsFormResponseDto.builder()
                .id(1L)
                .email(requestDto.getEmail())
                .message(requestDto.getMessage())
                .name(requestDto.getName())
                .phoneNumber(requestDto.getPhoneNumber())
                .note("Your message was successfully sent!")
                .build();

        when(contactUsFormRepository.save(any(ContactUsForm.class))).thenReturn(form);
        when(contactUsFormConvertor.toContactUsForm(any(ContactUsFormRequestDto.class))).thenReturn(form);
        when(contactUsFormConvertor.toContactUsFormResponse(any(ContactUsForm.class))).thenReturn(responseDto);

        ContactUsFormResponseDto result = contactUsFormService.save(requestDto);

        assertEquals(responseDto, result);
    }


    @Test
    void saveContactUsForm() {
        ContactUsFormRequestDto contactUsFormRequestDto = buildContactUsFormRequest();
        ContactUsForm contactUsForm = buildContactUsForm();
        ContactUsFormResponseDto expectedDto = buildResponse(contactUsForm);
        when(contactUsFormConvertor.toContactUsForm(any(ContactUsFormRequestDto.class))).thenReturn(contactUsForm);
        when(contactUsFormConvertor.toContactUsFormResponse(any(ContactUsForm.class))).thenReturn(expectedDto);
        when(contactUsFormRepository.save(any(ContactUsForm.class))).thenReturn(contactUsForm);

        ContactUsFormResponseDto responseDto = contactUsFormService.save(contactUsFormRequestDto);

        assertEquals(expectedDto, responseDto);
        // verify(contactUsFormService, times(1)).save(contactUsFormRequestDto);
    }

    @Test
    void testDeleteById() {
        Long id = 1L;
        contactUsFormService.deleteById(id);
        verify(contactUsFormRepository).deleteById(id);
    }

    @Test
    void testGetContactUsFormsByPhoneNumber() {
        String phoneNumber = "01234567890";
        List<ContactUsForm> formList = new ArrayList<>();
        List<ContactUsFormResponseDto> responseDtoList = new ArrayList<>();
        when(contactUsFormRepository.findByPhoneNumber(phoneNumber)).thenReturn(formList);
        when(contactUsFormConvertor.toContactUsFormResponse(
                any(ContactUsForm.class))).thenReturn(ContactUsFormResponseDto.builder().build());

        List<ContactUsFormResponseDto> result = contactUsFormService.getContactUsFormsByPhoneNumber(phoneNumber);

        assertEquals(responseDtoList, result);
        verify(contactUsFormRepository).findByPhoneNumber(phoneNumber);
        verify(contactUsFormConvertor, times(formList.size())).toContactUsFormResponse(
                any(ContactUsForm.class));
    }

    @Test
    void testGetContactUsFormsByName() {
        String name = "Lilly Ivanova";
        List<ContactUsForm> formList = new ArrayList<>();
        List<ContactUsFormResponseDto> responseDtoList = new ArrayList<>();
        when(contactUsFormRepository.findByName(name)).thenReturn(formList);
        when(contactUsFormConvertor.toContactUsFormResponse(any(ContactUsForm.class))).thenReturn(ContactUsFormResponseDto.builder().build());

        List<ContactUsFormResponseDto> result = contactUsFormService.getContactUsFormsByName(name);

        assertEquals(responseDtoList, result);
        verify(contactUsFormRepository).findByName(name);
        verify(contactUsFormConvertor, times(formList.size())).toContactUsFormResponse(any(ContactUsForm.class));
    }

    @Test
    void testGetContactUsFormsByText() {
        String text = "random text";
        List<ContactUsForm> formList = new ArrayList<>();
        List<ContactUsFormResponseDto> responseDtoList = new ArrayList<>();
        when(contactUsFormRepository.findByMessageContaining(text)).thenReturn(formList);
        when(contactUsFormConvertor.toContactUsFormResponse(
                any(ContactUsForm.class))).thenReturn(ContactUsFormResponseDto.builder().build());

        List<ContactUsFormResponseDto> result = contactUsFormService.getContactUsFormsByText(text);

        assertEquals(responseDtoList, result);
        verify(contactUsFormRepository).findByMessageContaining(text);
        verify(contactUsFormConvertor, times(formList.size())).toContactUsFormResponse(any(ContactUsForm.class));
    }


    @Test
    @DisplayName("Test saveContactUsForm with invalid DTO - name")
    void testSaveContactUsFormWithInvalidDtoName() {
        ContactUsFormRequestDto contactUsFormDto = new ContactUsFormRequestDto();
        contactUsFormDto.setName("");
        contactUsFormDto.setEmail("vankata@gmail.com");
        contactUsFormDto.setPhoneNumber("0888850850");
        contactUsFormDto.setMessage("Hello");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ContactUsFormRequestDto>> violations = validator.validate(contactUsFormDto);

        assertEquals(1, violations.size());
        assertEquals("Name is required", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Test saveContactUsForm with invalid DTO - email")
    void testSaveContactUsFormWithInvalidDtoEmail() {
        ContactUsFormRequestDto contactUsFormDto = new ContactUsFormRequestDto();
        contactUsFormDto.setName("Simeon Sotirov");
        contactUsFormDto.setEmail("invalid_email");
        contactUsFormDto.setPhoneNumber("0888850850");
        contactUsFormDto.setMessage("Hello");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ContactUsFormRequestDto>> violations = validator.validate(contactUsFormDto);

        assertEquals(1, violations.size());
        assertEquals("Invalid email address", violations.iterator().next().getMessage());
    }

    //no coverage
    @Test
    @DisplayName("Test saveContactUsForm with invalid DTO - phone number")
    void testSaveContactUsFormWithInvalidDtoPhoneNumber() {
        ContactUsFormRequestDto contactUsFormDto = new ContactUsFormRequestDto();
        contactUsFormDto.setName("Simeon Sotirov");
        contactUsFormDto.setEmail("simo@gmail.com");
        contactUsFormDto.setPhoneNumber("invalid_phone_number");
        contactUsFormDto.setMessage("Hello");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ContactUsFormRequestDto>> violations = validator.validate(contactUsFormDto);

        assertEquals(1, violations.size());
        assertEquals("Doesn't seem to be a valid phone number", violations.iterator().next().getMessage());
    }

    //no coverage
    @Test
    @DisplayName("Test saveContactUsForm with invalid DTO - message")
    void testSaveContactUsFormWithInvalidDtoMessage() {
        ContactUsFormRequestDto contactUsFormDto = new ContactUsFormRequestDto();
        contactUsFormDto.setName("Ivan");
        contactUsFormDto.setEmail("ivan@example.com");
        contactUsFormDto.setPhoneNumber("+1 650-555-1212");
        contactUsFormDto.setMessage("");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ContactUsFormRequestDto>> violations = validator.validate(contactUsFormDto);

        assertEquals(1, violations.size());

        List<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .sorted().toList();

        assertEquals("Message is required", messages.get(0));
    }

    //no coverage
    private ContactUsFormRequestDto buildContactUsFormRequest() {
        ContactUsFormRequestDto clreq = new ContactUsFormRequestDto();
        clreq.setName("TESTOV");
        clreq.setEmail("test@abv.bg");
        clreq.setPhoneNumber("0888850987");
        clreq.setMessage("Kak da se swyrja s vas");
        return clreq;
    }

    //no coverage
    private ContactUsForm buildContactUsForm() {
        ContactUsForm clreq = new ContactUsForm();
        clreq.setId(1L);
        clreq.setName("TESTOV");
        clreq.setEmail("test@abv.bg");
        clreq.setPhoneNumber("0888850987");
        clreq.setMessage("Kak da se swyrja s vas");
        return clreq;
    }

    //no coverage
    private ContactUsFormResponseDto buildResponse(ContactUsForm contactUsForm) {
        return contactUsFormConvertor.toContactUsFormResponse(contactUsForm);
    }

}
