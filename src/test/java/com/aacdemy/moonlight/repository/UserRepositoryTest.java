package com.aacdemy.moonlight.repository;

import com.aacdemy.moonlight.entity.user.User;
import com.aacdemy.moonlight.entity.user.UserRole;
import com.aacdemy.moonlight.repository.user.RoleRepository;
import com.aacdemy.moonlight.repository.user.UserRepository;
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

import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeAll
    public void init() {
        UserRole role = new UserRole();
        role.setUserRole("ROLE_ADMIN");
        roleRepository.save(role);

        User user = userRepository.save(User.builder()
                .firstName("Admin")
                .lastName("Adminov")
                .email("admin@mail.bg")
                .phoneNumber("+359879233212")
                .passportID("12101013113")
                .password("1PassWord!")
                .userRole(role)
                .enabled(true)
                .createdDate(Date.from(Instant.now()))
                .modifiedDate(Date.from(Instant.now()))
                .build());

        User user1 = userRepository.save(User.builder()
                .firstName("Admini")
                .lastName("Adminova")
                .email("admins@mail.bg")
                .phoneNumber("+359879233212")
                .passportID("1210101321")
                .password("1PassWord!")
                .userRole(role)
                .enabled(true)
                .createdDate(Date.from(Instant.now()))
                .modifiedDate(Date.from(Instant.now()))
                .build());

        User user2 = userRepository.save(User.builder()
                .firstName("Admin")
                .lastName("Adminov")
                .email("adminis@mail.bg")
                .phoneNumber("+359879233212")
                .passportID("1210101331")
                .password("1PassWord!")
                .userRole(role)
                .enabled(true)
                .createdDate(Date.from(Instant.now()))
                .modifiedDate(Date.from(Instant.now()))
                .build());
    }

    @Test
    public void findByIdReturnUser() {
        Long id = userRepository.findByEmail("admin@mail.bg").get().getId();
        User result = userRepository.findById(id).get();
        Assertions.assertEquals("Admin", result.getFirstName());
    }

    @Test
    public void findByIdReturnEmpty() {
        Optional<User> result = userRepository.findById(41L);
        Assertions.assertEquals(Optional.empty(), result);
    }

    @Test
    public void findByEmailReturnUser() {
        User result = userRepository.findByEmail("admin@mail.bg").get();
        Assertions.assertEquals("admin@mail.bg", result.getEmail());
    }

    @Test
    public void findByEmailReturnEmpty() {
        Optional<User> result = userRepository.findByEmail("mailing@mail.bg");
        Assertions.assertEquals(Optional.empty(), result);
    }

    @Test
    public void findByPhoneNumberReturnList() {
        List<User> result = userRepository.findByPhoneNumber("+359879233212");
        Assertions.assertEquals("+359879233212", result.listIterator().next().getPhoneNumber());
    }

    @Test
    public void findByPhoneNumberReturnEmpty() {
        List<User> result = userRepository.findByPhoneNumber("+359878787878");
        Assertions.assertEquals(List.of(), result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findByPassportIdReturnUser() {
        User result = userRepository.findByPassportID("12101013113").get();
        Assertions.assertEquals("12101013113", result.getPassportID());
    }

    @Test
    public void findByPassportIdReturnEmpty() {
        Optional<User> result = userRepository.findByPassportID("12101013");
        Assertions.assertEquals(Optional.empty(), result);
    }

    @Test
    public void findByFirstNameReturnList() {
        List<User> result = userRepository.findByFirstName("Admin");
        Assertions.assertEquals("Admin", result.listIterator().next().getFirstName());
    }

    @Test
    public void findByFirstNameReturnEmpty() {
        List<User> result = userRepository.findByFirstName("Administrator");
        Assertions.assertEquals(List.of(), result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findByLastNameReturnList() {
        List<User> result = userRepository.findByLastName("Adminov");

        Assertions.assertEquals("Adminov", result.listIterator().next().getLastName());
    }

    @Test
    public void findByLastNameReturnEmpty() {
        List<User> result = userRepository.findByLastName("Adminievi");
        Assertions.assertEquals(List.of(), result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findByUserRoleUserRoleReturnList() {
        List<User> result = userRepository.findByUserRoleUserRole("ROLE_ADMIN");
        Assertions.assertEquals("ROLE_ADMIN", result.listIterator().next().getUserRole().getUserRole());
    }

    @Test
    public void findByUserRoleUserRoleReturnEmpty() {
        List<User> result = userRepository.findByUserRoleUserRole("ROLE_USER");
        Assertions.assertEquals(List.of(), result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findByFirstNameAndLastNameReturnList() {
        List<User> result = userRepository.findByFirstNameAndLastName("Admin", "Adminov");
        Assertions.assertEquals("Admin", result.listIterator().next().getFirstName());
        Assertions.assertEquals("Adminov", result.listIterator().next().getLastName());
    }

    @Test
    public void findByFirstNameAndLastNameReturnEmpty() {
        List<User> result = userRepository.findByFirstNameAndLastName("Admins", "Adminiev");
        Assertions.assertEquals(List.of(), result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void existsByEmailReturnTrue() {
        Assertions.assertTrue(userRepository.existsByEmail("admin@mail.bg"));
    }

    @Test
    public void existsByEmailReturnFalse() {
        Assertions.assertFalse(userRepository.existsByEmail("mail@mailing.bg"));
    }

    @Test
    public void existsByPhoneNumberReturnTrue() {
        Assertions.assertTrue(userRepository.existsByPhoneNumber("+359879233212"));
    }

    @Test
    public void existsByPhoneNumberReturnFalse() {
        Assertions.assertFalse(userRepository.existsByPhoneNumber("+3598792332123"));
    }

    @Test
    public void existsByPassportIdReturnTrue() {
        Assertions.assertTrue(userRepository.existsByPassportID("12101013113"));
    }

    @Test
    public void existsByPassportIdReturnFalse() {
        Assertions.assertFalse(userRepository.existsByPassportID("121010131"));
    }
}
