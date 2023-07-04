package com.aacdemy.moonlight.repository;

import com.aacdemy.moonlight.entity.ContactUsForm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ContactUsFormRepositoryTest {
    @Autowired
    private ContactUsFormRepository contactUsFormRepository;

    @BeforeAll
    public void init() {
        ContactUsForm form = contactUsFormRepository.save(ContactUsForm.builder()
                .name("User")
                .email("email@mail.bg")
                .phoneNumber("+359889385664")
                .message("No Text")
                .build());

        ContactUsForm deleteForm = contactUsFormRepository.save(ContactUsForm.builder()
                .name("Maniac")
                .email("emails@mail.bg")
                .phoneNumber("+3598893856644")
                .message("To much Text")
                .build());
    }

    @Test
    public void save() {
        Optional<ContactUsForm> result = Optional.of(contactUsFormRepository.findByName("User").listIterator().next());
        Assertions.assertEquals("User", result.get().getName());
    }

    @Test
    public void deleteById() {
        contactUsFormRepository.deleteById(2L);
        Assertions.assertFalse(contactUsFormRepository.existsById(2L));
    }

    @Test
    public void findByEmail() {
        List<ContactUsForm> result = contactUsFormRepository.findByEmail("email@mail.bg");
        Assertions.assertEquals(contactUsFormRepository.findById(1L).get().getEmail(), result.listIterator().next().getEmail());
    }

    @Test
    public void findByEmailReturnEmptyList() {
        List<ContactUsForm> result = contactUsFormRepository.findByEmail("mail@mail.bg");
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findByPhoneNumber() {
        List<ContactUsForm> result = contactUsFormRepository.findByPhoneNumber("+359889385664");
        Assertions.assertEquals("+359889385664", result.listIterator().next().getPhoneNumber());
    }

    @Test
    public void findByPhoneNumberReturnEmptyList() {
        List<ContactUsForm> result = contactUsFormRepository.findByPhoneNumber("+35988938566");
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findByName() {
        List<ContactUsForm> result = contactUsFormRepository.findByName("User");
        Assertions.assertEquals("User", result.listIterator().next().getName());
    }

    @Test
    public void findByNameReturnEmptyList() {
        List<ContactUsForm> result = contactUsFormRepository.findByName("Petar");
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findByMessageContaining() {
        List<ContactUsForm> result = contactUsFormRepository.findByMessageContaining("Text");
        Assertions.assertTrue(result.listIterator().next().getMessage().contains("Text"));
    }

    @Test
    public void findByMessageContainingReturnEmptyList() {
        List<ContactUsForm> result = contactUsFormRepository.findByMessageContaining("Petar");
        Assertions.assertTrue(result.isEmpty());
    }
}
