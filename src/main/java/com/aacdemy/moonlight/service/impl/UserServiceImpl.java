package com.aacdemy.moonlight.service.impl;

import com.aacdemy.moonlight.config.security.JwtService;
import com.aacdemy.moonlight.convertor.UserConvertor;
import com.aacdemy.moonlight.dto.user.*;
import com.aacdemy.moonlight.entity.user.User;
import com.aacdemy.moonlight.exception.AdminDeleteException;
import com.aacdemy.moonlight.exception.EntityNotFoundException;
import com.aacdemy.moonlight.repository.user.UserRepository;
import com.aacdemy.moonlight.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.aacdemy.moonlight.util.RandomString.generateRandomString;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserConvertor userConvertor;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailServiceImpl emailService;

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserConvertor userConvertor, BCryptPasswordEncoder bCryptPasswordEncoder, EmailServiceImpl emailService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userConvertor = userConvertor;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailService = emailService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserLoginResponseDto login(UserLoginRequestDto request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new EntityNotFoundException("Please verify your credentials"));
        var jwtToken = jwtService.generateJwt(user);
        return UserLoginResponseDto.builder().token(jwtToken).build();
    }

    @Override
    public UserRegistrationResponseDto saveUser(UserRegistrationRequestDto userRequestDto) {
        User userToBeSaved = userConvertor.toClientUser(userRequestDto);
        User user = userRepository.save(userToBeSaved);
        UserRegistrationResponseDto userRegResponse = userConvertor.toRegistrationResponse(user);
        emailService.sendRegistrationEmail(userRequestDto);

        return userRegResponse;
    }

    public void deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            checkIfUserIsNotAdmin(user.get());
            userRepository.deleteById(id);
        } else throw new EntityNotFoundException("User not found");
    }

    private void checkIfUserIsNotAdmin(User user) {
        if (user.getUserRole().getUserRole().equals("ROLE_ADMIN")) {
            throw new AdminDeleteException();
        }
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = (userRepository.findById(id)).orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
        return userConvertor.toResponse(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream().map(userConvertor::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> getUsersByFirstName(String firstName) {
        return userRepository.findByFirstName(firstName).stream().map(userConvertor::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> getUsersByLastName(String lastName) {
        return userRepository.findByLastName(lastName).stream().map(userConvertor::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> getUsersByFullName(String firstName, String lastName) {
        return userRepository.findByFirstNameAndLastName(firstName, lastName).stream().map(userConvertor::toResponse).collect(Collectors.toList());
    }

    @Override
    public UserResponseDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));
        return userConvertor.toResponse(user);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));
    }

    @Override
    public List<UserResponseDto> getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).stream().map(userConvertor::toResponse).collect(Collectors.toList());
    }

    @Override
    public UserResponseDto getUserByPassportID(String passportID) {
        User user = userRepository.findByPassportID(passportID).orElseThrow(() -> new EntityNotFoundException("User with passport ID " + passportID + " not found"));
        return userConvertor.toResponse(user);
    }

    /*
     * To be updated according to application's need
     *
     * Updates a user's information based on the provided UserUpdateDto:
     *
     * The method checks if the user's first name, last name, email, password or passport ID fields are different
     * If any field is different, it is updated in the user object.
     *
     */
    @Override
    @Transactional
    public UserResponseDto updateUser(UserUpdateDto userUpdateDto, String token) {

        String userEmail = jwtService.extractUserName(token);
        User userToBeUpdated = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!userUpdateDto.getFirstName().equals(userToBeUpdated.getFirstName())) {
            userToBeUpdated.setFirstName(userUpdateDto.getFirstName());
        }
        if (!userUpdateDto.getLastName().equals(userToBeUpdated.getLastName())) {
            userToBeUpdated.setLastName(userUpdateDto.getLastName());
        }
        if (!userUpdateDto.getEmail().equals(userToBeUpdated.getEmail())) {
            userToBeUpdated.setEmail(userUpdateDto.getEmail());
        }
        if (!userUpdateDto.getPassportID().equals(userToBeUpdated.getPassportID())) {
            userToBeUpdated.setPassportID(userUpdateDto.getPassportID());
        }
        if (!userUpdateDto.getPhoneNumber().equals(userToBeUpdated.getPhoneNumber())) {
            userToBeUpdated.setPhoneNumber(userUpdateDto.getPhoneNumber());
        }

        return userConvertor.toResponse(userToBeUpdated);
    }
@Transactional
public UserPasswordUpdateResponseDto updatePassword(String token, String currentPassword, String newPassword, String confirmPassword) {

    String userEmail = jwtService.extractUserName(token);
    User existingUser = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

    if (!bCryptPasswordEncoder.matches(currentPassword, existingUser.getPassword())) {
        throw new IllegalArgumentException("Current password is incorrect");
    }

    if (!newPassword.equals(confirmPassword)) {
        throw new IllegalArgumentException("New password and confirm password do not match");
    }

    existingUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
    existingUser.setModifiedDate(new Date());

    User updatedUser = userRepository.save(existingUser);

    return userConvertor.toUserPasswordUpdateResponse(updatedUser);
}

    @Override
    public UserResetPasswordResponseDto resetPassword(UserResetPasswordRequestDto email) {
        Optional<User> user = userRepository.findByEmail(email.getEmail());

        if (user.isPresent() && !user.get().getUserRole().getUserRole().equalsIgnoreCase("ROLE_ADMIN")) {
            String newPassword = generateRandomString(9);
            user.get().setPassword(bCryptPasswordEncoder.encode(newPassword));
            userRepository.save(user.get());

            emailService.sendPasswordResetEmail(user.get(), newPassword);
        } else throw new EntityNotFoundException("User not found");
        return UserResetPasswordResponseDto.builder().build();
    }

    @Override
    public boolean isEmailTaken(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean isPhoneNumberTaken(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public boolean isPassportIDTaken(String passportID) {
        return userRepository.existsByPassportID(passportID);
    }
}