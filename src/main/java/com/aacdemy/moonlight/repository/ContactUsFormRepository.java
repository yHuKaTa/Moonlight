package com.aacdemy.moonlight.repository;

import com.aacdemy.moonlight.entity.ContactUsForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactUsFormRepository extends JpaRepository<ContactUsForm, Long> {
    List<ContactUsForm> findByEmail(String email);

    List<ContactUsForm> findByPhoneNumber(String phoneNumber);

    List<ContactUsForm> findByName(String name);

    List<ContactUsForm> findByMessageContaining(String text);

}
