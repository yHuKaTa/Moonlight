package com.aacdemy.moonlight.service;

import com.aacdemy.moonlight.dto.user.UserRegistrationRequestDto;
import com.aacdemy.moonlight.entity.user.User;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {

    void sendRegistrationEmail(UserRegistrationRequestDto user);

    void sendPasswordResetEmail(User user, String newPassword);

}
