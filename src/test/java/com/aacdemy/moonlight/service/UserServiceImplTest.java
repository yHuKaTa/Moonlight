package com.aacdemy.moonlight.service;


import com.aacdemy.moonlight.config.security.JwtService;
import com.aacdemy.moonlight.convertor.UserConvertor;
import com.aacdemy.moonlight.dto.user.*;
import com.aacdemy.moonlight.entity.user.User;
import com.aacdemy.moonlight.entity.user.UserRole;
import com.aacdemy.moonlight.exception.AdminDeleteException;
import com.aacdemy.moonlight.exception.EntityNotFoundException;
import com.aacdemy.moonlight.repository.user.UserRepository;
import com.aacdemy.moonlight.service.impl.EmailServiceImpl;
import com.aacdemy.moonlight.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private UserConvertor userConvertor;
    @Mock
    private EmailServiceImpl emailService;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserService userService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");


    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(
                userRepository,
                userConvertor,
                bCryptPasswordEncoder,
                emailService,
                jwtService,
                authenticationManager);
    }

    @Test
    void testLogin() {

        UserLoginRequestDto request = UserLoginRequestDto.builder()
                .email("test@example.com")
                .password("testpassword")
                .build();

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("$2a$10$4D5/Z1k1sbz9w4Fdloxx9OPpKfNp2mqX1h.NPZL6s88wJ/vASeo/u");

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(jwtService.generateJwt(user)).thenReturn("generated_jwt_token");
        doReturn(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())).when(authenticationManager).authenticate(any());

        UserLoginResponseDto response = userService.login(request);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals("generated_jwt_token", response.getToken())
        );
    }

    @Test
    void testSaveUser() {

        UserRegistrationRequestDto request = new UserRegistrationRequestDto();
        request.setEmail("testuser@example.com");
        request.setPassword("testpassword");

        User userToBeSaved = new User();
        userToBeSaved.setEmail(request.getEmail());
        userToBeSaved.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail(request.getEmail());
        savedUser.setCreatedDate(new Date());

        UserRegistrationResponseDto expectedResponse = UserRegistrationResponseDto.builder()
                .email(request.getEmail())
                .dateCreated(dateFormat.format(savedUser.getCreatedDate()))
                .build();

        when(userConvertor.toClientUser(request)).thenReturn(userToBeSaved);
        when(userRepository.save(userToBeSaved)).thenReturn(savedUser);
        when(userConvertor.toRegistrationResponse(savedUser)).thenReturn(expectedResponse);

        UserRegistrationResponseDto response = userService.saveUser(request);

        verify(userRepository).save(userToBeSaved);
        verify(userConvertor).toClientUser(request);
        verify(userConvertor).toRegistrationResponse(savedUser);
        verify(emailService).sendRegistrationEmail(request);
        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    void testDeleteUser() {

        Long userId = 1L;
        User user = new User();
        UserRole role = new UserRole();

        role.setUserRole("ROLE_USER");
        user.setId(userId);
        user.setUserRole(role);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testDeleteUserIfUserNotFound() {

        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(userId));
        verify(userRepository).findById(userId);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testDeleteUserIfUserIsAdmin() {

        Long userId = 1L;
        User adminUser = new User();
        UserRole adminRole = new UserRole();
        adminRole.setUserRole("ROLE_ADMIN");
        adminUser.setUserRole(adminRole);

        when(userRepository.findById(userId)).thenReturn(Optional.of(adminUser));

        assertThrows(AdminDeleteException.class, () -> userService.deleteUser(userId));
        verify(userRepository).findById(userId);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testGetAllUsers() {

        User user1 = new User();
        user1.setFirstName("Ivan");
        user1.setLastName("Ivanov");
        user1.setPhoneNumber("089349923");

        User user2 = new User();
        user2.setFirstName("Dragan");
        user2.setLastName("Draganov");
        user2.setPhoneNumber("08932939284");

        List<User> userList = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(userList);

        when(userConvertor.toResponse(user1)).thenReturn(createUserResponseDto("Ivan", "Ivanov"));
        when(userConvertor.toResponse(user2)).thenReturn(createUserResponseDto("Dragan", "Draganov"));

        List<UserResponseDto> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("Ivan", result.get(0).getFirstName());
        assertEquals("Ivanov", result.get(0).getLastName());
        assertEquals("Dragan", result.get(1).getFirstName());
        assertEquals("Draganov", result.get(1).getLastName());
    }

    private UserResponseDto createUserResponseDto(String firstName, String lastName) {
        return UserResponseDto.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }

    @Test
    void testGetUsersByFirstName() {
        String firstName = "Ivan";

        User user1 = new User();
        user1.setFirstName("Ivan");
        user1.setLastName("Petkanov");

        User user2 = new User();
        user2.setFirstName("Ivan");
        user2.setLastName("Stoichov");

        List<User> users = List.of(user1, user2);
        when(userRepository.findByFirstName(firstName)).thenReturn(users);

        UserResponseDto response1 = UserResponseDto.builder()
                .firstName(user1.getFirstName())
                .lastName(user1.getLastName())
                .build();
        UserResponseDto response2 = UserResponseDto.builder()
                .firstName(user2.getFirstName())
                .lastName(user2.getLastName())
                .build();

        when(userConvertor.toResponse(user1)).thenReturn(response1);
        when(userConvertor.toResponse(user2)).thenReturn(response2);

        List<UserResponseDto> expected = List.of(response1, response2);
        List<UserResponseDto> actual = userService.getUsersByFirstName(firstName);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetUsersByLastName() {

        String lastName = "Petkanov";

        User user1 = new User();
        user1.setFirstName("Ivan");
        user1.setLastName("Petkanov");

        User user2 = new User();
        user2.setFirstName("Ivan");
        user2.setLastName("Stoichov");

        List<User> users = List.of(user1, user2);
        when(userRepository.findByLastName(lastName)).thenReturn(users);

        UserResponseDto response1 = UserResponseDto.builder()
                .firstName(user1.getFirstName())
                .lastName(user1.getLastName())
                .build();
        UserResponseDto response2 = UserResponseDto.builder()
                .firstName(user2.getFirstName())
                .lastName(user2.getLastName())
                .build();

        when(userConvertor.toResponse(user1)).thenReturn(response1);
        when(userConvertor.toResponse(user2)).thenReturn(response2);

        List<UserResponseDto> expected = List.of(response1, response2);
        List<UserResponseDto> actual = userService.getUsersByLastName(lastName);
        assertEquals(expected, actual);
    }

    @Test
    void testGetUsersByFullName() {
        String firstName = "Dragan";
        String lastName = "Draganov";

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);

        when(userRepository.findByFirstNameAndLastName(firstName, lastName)).thenReturn(List.of(user));

        UserResponseDto response = UserResponseDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
        when(userConvertor.toResponse(user)).thenReturn(response);

        List<UserResponseDto> expected = List.of(response);
        List<UserResponseDto> actual = userService.getUsersByFullName(firstName, lastName);
        assertEquals(expected, actual);
    }

    @Test
    public void findByIdTest() {
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.of(User.builder().email("test@gmail.com").build()));
        userService.getUserById(id);
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    public void findByEmailTest() {
        String email = "test@gmail.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(User.builder().id(1L).build()));
        userService.getUserByEmail(email);
        verify(userRepository, times(1)).findByEmail(email);
    }
    @Test
    public void testGetUserByPhoneNumber() {

        String phoneNumber = "1234567890";

        User user1 = new User();
        user1.setId(1L);
        user1.setPhoneNumber(phoneNumber);

        User user2 = new User();
        user2.setId(2L);
        user2.setPhoneNumber(phoneNumber);

        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(users);

        UserResponseDto userResponseDto1 = UserResponseDto.builder()
                .id(user1.getId())
                .build();

        UserResponseDto userResponseDto2 = UserResponseDto.builder()
                .id(user2.getId())
                .build();

        when(userConvertor.toResponse(user1)).thenReturn(userResponseDto1);
        when(userConvertor.toResponse(user2)).thenReturn(userResponseDto2);

        List<UserResponseDto> result = userService.getUserByPhoneNumber(phoneNumber);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId().longValue());
        assertEquals(2L, result.get(1).getId().longValue());
    }

    @Test
    public void testGetUserByEmail() {

        String email = "test@example.com";
        User user = new User();
        user.setId(1L);
        user.setFirstName("Ivan");
        user.setLastName("Ivanov");
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userConvertor.toResponse(user)).thenReturn(UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .build());

        UserResponseDto userResponseDto = userService.getUserByEmail(email);

        assertNotNull(userResponseDto);
        verify(userRepository, times(1)).findByEmail(email);
        verify(userConvertor, times(1)).toResponse(user);
    }
    @Test
    public void testGetByEmail() {
        String userEmail = "test@example.com";

        User user = new User();
        user.setEmail(userEmail);

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        User actualUser = userService.getByEmail(userEmail);

        assertNotNull(actualUser);
        assertEquals(userEmail, actualUser.getEmail());

        verify(userRepository, times(1)).findByEmail(userEmail);
    }

    @Test
    public void testGetUserByPassportID() {

        String passportID = "ASD123";

        User user = new User();
        user.setId(1L);
        user.setFirstName("Ivan");
        user.setLastName("Ivanov");
        user.setPassportID(passportID);

        when(userRepository.findByPassportID(passportID)).thenReturn(Optional.of(user));
        when(userConvertor.toResponse(user)).thenReturn(UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .build());

        UserResponseDto userResponseDto = userService.getUserByPassportID(passportID);

        assertNotNull(userResponseDto);
        verify(userRepository, times(1)).findByPassportID(passportID);
        verify(userConvertor, times(1)).toResponse(user);
    }

    @Test
    void testUpdateUser
            () {
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setFirstName("Ivan");
        userUpdateDto.setLastName("Ivanov");
        userUpdateDto.setEmail("ivan@example.com");
        userUpdateDto.setPassportID("ABC123BG");
        userUpdateDto.setPhoneNumber("08993483943");

        User userToBeUpdated = new User();
        userToBeUpdated.setFirstName("Dragan");
        userToBeUpdated.setLastName("Draganov");
        userToBeUpdated.setEmail("dragi@example.com");
        userToBeUpdated.setPassportID("DEF456BG");
        userToBeUpdated.setPhoneNumber("04903478392");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userToBeUpdated));

        UserResponseDto expectedResponseDto = UserResponseDto.builder()
                .firstName(userUpdateDto.getFirstName())
                .lastName(userUpdateDto.getLastName())
                .email(userUpdateDto.getEmail())
                .passportID(userUpdateDto.getPassportID())
                .phoneNumber(userUpdateDto.getPhoneNumber())
                .build();
        when(userConvertor.toResponse(userToBeUpdated)).thenReturn(expectedResponseDto);

        when(jwtService.extractUserName(anyString())).thenReturn(userUpdateDto.getEmail());

        UserResponseDto actualResponseDto = userService.updateUser(userUpdateDto, "validToken");

        verify(userRepository).findByEmail(userUpdateDto.getEmail());
        verify(userConvertor).toResponse(userToBeUpdated);
        assertEquals(expectedResponseDto, actualResponseDto);

        assertEquals(userUpdateDto.getFirstName(), userToBeUpdated.getFirstName());
        assertEquals(userUpdateDto.getLastName(), userToBeUpdated.getLastName());
        assertEquals(userUpdateDto.getEmail(), userToBeUpdated.getEmail());
        assertEquals(userUpdateDto.getPassportID(), userToBeUpdated.getPassportID());
        assertEquals(userUpdateDto.getPhoneNumber(), userToBeUpdated.getPhoneNumber());
    }
    @Test
    public void testUpdatePassword() {

        String token = "token";
        String currentPassword = "current_password";
        String newPassword = "new_password";
        String confirmPassword = "new_password";

        String userEmail = "test@abv.bg";
        String encodedPassword = "encoded_password";

        User existingUser = new User();
        existingUser.setEmail(userEmail);
        existingUser.setPassword(encodedPassword);

        User updatedUser = new User();
        updatedUser.setEmail(userEmail);
        updatedUser.setPassword(encodedPassword);

        LocalDateTime expectedDateTime = LocalDateTime.of(2023, 5, 20, 10, 30, 0);
        Date dateModified = Date.from(expectedDateTime.atZone(ZoneId.systemDefault()).toInstant());


        UserPasswordUpdateResponseDto expectedResponse = UserPasswordUpdateResponseDto.builder()
                .message("Password for Moonlight Hotel & Spa's account successfully changed.")
                .dateModified(dateModified)
                .build();

        when(jwtService.extractUserName(token)).thenReturn(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(existingUser));
        when(bCryptPasswordEncoder.matches(currentPassword, encodedPassword)).thenReturn(true);
        when(bCryptPasswordEncoder.encode(newPassword)).thenReturn(encodedPassword);
        when(userRepository.save(existingUser)).thenReturn(updatedUser);
        when(userConvertor.toUserPasswordUpdateResponse(updatedUser)).thenReturn(expectedResponse);

        UserPasswordUpdateResponseDto actualResponse = userService.updatePassword(token, currentPassword, newPassword, confirmPassword);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedResponse.getDateModified(), actualResponse.getDateModified());

        when(bCryptPasswordEncoder.matches(currentPassword, encodedPassword)).thenReturn(false);
        String incorrectPassword = confirmPassword;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.updatePassword(token, currentPassword, newPassword, incorrectPassword)
        );
        assertEquals("Current password is incorrect", exception.getMessage());

        when(bCryptPasswordEncoder.matches(currentPassword, encodedPassword)).thenReturn(true);
        confirmPassword = "different_password";
        String differentPassword = confirmPassword;
        exception = assertThrows(IllegalArgumentException.class, () ->
                userService.updatePassword(token, currentPassword, newPassword, differentPassword)
        );
        assertEquals("New password and confirm password do not match", exception.getMessage());
    }

    @Test
    public void testResetPassword() {
        String userEmail = "test@example.com";
        String newPassword = "new_password";

        UserResetPasswordRequestDto resetRequest = UserResetPasswordRequestDto.builder()
                .email(userEmail)
                .build();

        UserRole userRole = new UserRole();
        userRole.setUserRole("ROLE_USER");

        User user = new User();
        user.setEmail(userEmail);
        user.setUserRole(userRole);

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.encode(newPassword)).thenReturn("encoded_password");

        doNothing().when(emailService).sendPasswordResetEmail(any(User.class), anyString());

        UserResetPasswordResponseDto actualResponse = userService.resetPassword(resetRequest);

        assertNotNull(actualResponse);
        verify(userRepository, times(1)).findByEmail(userEmail);
        verify(userRepository, times(1)).save(user);
        verify(emailService, times(1)).sendPasswordResetEmail(any(User.class), anyString());
    }
    @Test
    void testIsEmailTakenIfExists() {

        String existingEmail = "test@abv.com";

        when(userRepository.existsByEmail(existingEmail)).thenReturn(true);

        boolean isEmailTaken = userService.isEmailTaken(existingEmail);
        assertTrue(isEmailTaken);
    }

    @Test
    public void testIsEmailTakenIfNotExist() {

        String nonExistingEmail = "test@abv.com";

        when(userRepository.existsByEmail(nonExistingEmail)).thenReturn(false);

        boolean isEmailTaken = userService.isEmailTaken(nonExistingEmail);
        assertFalse(isEmailTaken);
    }

    @Test
    void testIsPhoneNumberTakenIfExists() {

        String existingPhoneNumber = "1234567890";
        when(userRepository.existsByPhoneNumber(existingPhoneNumber)).thenReturn(true);

        boolean isPhoneNumberTaken = userService.isPhoneNumberTaken(existingPhoneNumber);

        assertTrue(isPhoneNumberTaken);
    }

    @Test
    void testIsPhoneNumberTakenIfNotExist() {

        String nonExistingPhoneNumber = "0987654321";
        when(userRepository.existsByPhoneNumber(nonExistingPhoneNumber)).thenReturn(false);

        boolean isPhoneNumberTaken = userService.isPhoneNumberTaken(nonExistingPhoneNumber);

        assertFalse(isPhoneNumberTaken);
    }

    @Test
    void testIsPassportIDTakenIfExists() {

        String existingPassportID = "AB1234567";
        when(userRepository.existsByPassportID(existingPassportID)).thenReturn(true);

        boolean isPassportIDTaken = userService.isPassportIDTaken(existingPassportID);

        assertTrue(isPassportIDTaken);
    }

    @Test
    void testIsPassportIDTakenIfNotExist() {

        String nonExistingPassportID = "CD9876543";

        when(userRepository.existsByPassportID(nonExistingPassportID)).thenReturn(false);

        boolean isPassportIDTaken = userService.isPassportIDTaken(nonExistingPassportID);

        assertFalse(isPassportIDTaken);
    }
}
