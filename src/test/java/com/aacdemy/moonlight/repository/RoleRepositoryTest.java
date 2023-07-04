package com.aacdemy.moonlight.repository;

import com.aacdemy.moonlight.entity.user.UserRole;
import com.aacdemy.moonlight.repository.user.RoleRepository;
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

import java.util.Optional;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    @BeforeAll
    public void init() {
        UserRole role = new UserRole();
        role.setUserRole("ROLE_ADMINISTRATOR");
        roleRepository.save(role);
    }

    @Test
    public void save() {
        Optional<UserRole> result = roleRepository.findByUserRole("ROLE_ADMINISTRATOR");
        Assertions.assertEquals("ROLE_ADMINISTRATOR", result.get().getUserRole());
    }

    @Test
    public void findByUserRoleReturnUserRole() {
        Optional<UserRole> result = roleRepository.findByUserRole("ROLE_ADMINISTRATOR");
        Assertions.assertTrue(roleRepository.count() != 0);
        Assertions.assertEquals("ROLE_ADMINISTRATOR", result.get().getUserRole());
    }

    @Test
    public void findByUserRoleReturnNull() {
        Optional<UserRole> result = roleRepository.findByUserRole("ROLE_MODERATOR");
        Assertions.assertEquals(Optional.empty(), result);
    }
}
