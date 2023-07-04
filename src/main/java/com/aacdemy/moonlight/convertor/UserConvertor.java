package com.aacdemy.moonlight.convertor;


import com.aacdemy.moonlight.config.security.JwtService;
import com.aacdemy.moonlight.dto.user.UserPasswordUpdateResponseDto;
import com.aacdemy.moonlight.dto.user.UserRegistrationRequestDto;
import com.aacdemy.moonlight.dto.user.UserRegistrationResponseDto;
import com.aacdemy.moonlight.dto.user.UserResponseDto;
import com.aacdemy.moonlight.entity.user.UserRole;
import com.aacdemy.moonlight.entity.user.User;

import com.aacdemy.moonlight.exception.EntityNotFoundException;
import com.aacdemy.moonlight.repository.user.RoleRepository;
import com.aacdemy.moonlight.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static com.aacdemy.moonlight.util.NowToDate.getCurrentDate;

@Component
public class UserConvertor {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;

    private final RoleRepository userRoleRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserConvertor(BCryptPasswordEncoder bCryptPasswordEncoder, JwtService jwtService, RoleRepository userRoleRepository, UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtService = jwtService;
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
    }

    public User toClientUser(UserRegistrationRequestDto userRequestDto) {
        User user = new User();
        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setEmail(userRequestDto.getEmail());
        user.setPhoneNumber(userRequestDto.getPhoneNumber());
        user.setPassword(bCryptPasswordEncoder.encode(userRequestDto.getPassword()));
        user.setEnabled(true);

        user.setCreatedDate(getCurrentDate());

        UserRole role = userRoleRepository.findByUserRole("ROLE_CLIENT")
                .orElseThrow(() -> new EntityNotFoundException("ROLE_CLIENT role not found"));
        user.setUserRole(role);

        return user;
    }

    public UserResponseDto toResponse(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .passportID(user.getPassportID())
                .role(user.getUserRole())
                .enabled(user.isEnabled())
                .createdDate(user.getCreatedDate())
                .modifiedDate(user.getModifiedDate())
                .build();
    }

    public UserRegistrationResponseDto toRegistrationResponse(User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateCreated = user.getCreatedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String formattedDate = dateCreated.format(formatter);

        String token = jwtService.generateJwt(user);

        return UserRegistrationResponseDto.builder()
                .email(user.getEmail())
                .dateCreated(formattedDate)
                .token(token)
                .build();
    }

    public UserPasswordUpdateResponseDto toUserPasswordUpdateResponse(User user) {
        User userToBeModified = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setModifiedDate(getCurrentDate());
        return UserPasswordUpdateResponseDto.builder()
                .dateModified(userToBeModified.getModifiedDate())
                .build();
    }
}
