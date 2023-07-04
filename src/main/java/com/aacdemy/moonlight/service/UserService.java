package com.aacdemy.moonlight.service;

import com.aacdemy.moonlight.dto.user.*;
import com.aacdemy.moonlight.entity.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    UserLoginResponseDto login(UserLoginRequestDto request);

    UserRegistrationResponseDto saveUser(UserRegistrationRequestDto user);

    void deleteUser(Long id);

    UserResponseDto getUserById(Long id);

    List<UserResponseDto> getAllUsers();

    List<UserResponseDto> getUsersByFirstName(String firstName);

    List<UserResponseDto> getUsersByLastName(String lastName);

    List<UserResponseDto> getUsersByFullName(String firstName, String lastName);

    UserResponseDto getUserByEmail(String email);

    List<UserResponseDto> getUserByPhoneNumber(String phoneNumber);
    User getByEmail(String email);

    UserResponseDto getUserByPassportID(String passportID);

    UserResponseDto updateUser(UserUpdateDto user, String token);

    UserResetPasswordResponseDto resetPassword(UserResetPasswordRequestDto email);

    UserPasswordUpdateResponseDto updatePassword(String token, String currentPassword, String newPassword, String confirmPassword);
    boolean isEmailTaken(String email);

    boolean isPhoneNumberTaken(String phoneNumber);

    boolean isPassportIDTaken(String passportID);
}