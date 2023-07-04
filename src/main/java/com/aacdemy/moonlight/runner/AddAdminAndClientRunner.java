package com.aacdemy.moonlight.runner;

import com.aacdemy.moonlight.entity.user.User;
import com.aacdemy.moonlight.entity.user.UserRole;
import com.aacdemy.moonlight.exception.EntityNotFoundException;
import com.aacdemy.moonlight.repository.user.RoleRepository;
import com.aacdemy.moonlight.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.aacdemy.moonlight.util.NowToDate.getCurrentDate;

@Component
public class AddAdminAndClientRunner implements CommandLineRunner {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    RoleRepository userRoleRepository;
    @Autowired
    private UserRepository userRepository;

    //creates ROLE_ADMIN & ROLE_CLIENT if they do not already exist
    @Override
    public void run(String... args) throws Exception {

        String adminRoleName = "ROLE_ADMIN";
        String clientRoleName = "ROLE_CLIENT";

        // Check if ROLE_ADMIN  and ROLE_CLIENT already exists
        Optional<UserRole> optionalAdminRole = userRoleRepository.findByUserRole(adminRoleName);
        Optional<UserRole> optionalClientRole = userRoleRepository.findByUserRole(clientRoleName);

        // If they do not exist, create them
        if (optionalAdminRole.isEmpty()) {
            UserRole roleAdminToBeSaved = new UserRole();
            roleAdminToBeSaved.setUserRole(adminRoleName);
            userRoleRepository.save(roleAdminToBeSaved);
        }

        if (optionalClientRole.isEmpty()) {
            UserRole roleClientToBeSaved = new UserRole();
            roleClientToBeSaved.setUserRole(clientRoleName);
            userRoleRepository.save(roleClientToBeSaved);
        }

        //get all users with ROLE_ADMIN
        List<User> adminUsers = userRepository.findByUserRoleUserRole(adminRoleName);
        List<User> clientsUsers = userRepository.findByUserRoleUserRole(clientRoleName);

        // If there is no admin user exists, create one
        if (adminUsers.isEmpty()) {
            User adminUserToBeSaved = User.builder().build();
            adminUserToBeSaved.setFirstName("Admin");
            adminUserToBeSaved.setLastName("User");
            adminUserToBeSaved.setEmail("admin@example.com");
            adminUserToBeSaved.setPhoneNumber("01234567890");
            adminUserToBeSaved.setEnabled(true);
            adminUserToBeSaved.setPassword(bCryptPasswordEncoder.encode("Ad!min123"));
            adminUserToBeSaved.setCreatedDate(getCurrentDate());
            adminUserToBeSaved.setUserRole(userRoleRepository.findByUserRole("ROLE_ADMIN")
                    .orElseThrow(() -> new EntityNotFoundException("Role ADMIN not found")));
            userRepository.save(adminUserToBeSaved);
        }

        if (clientsUsers.isEmpty()) {
            User clientUserToBeSaved = User.builder().build();
            clientUserToBeSaved.setFirstName("NotAdmin");
            clientUserToBeSaved.setLastName("User");
            clientUserToBeSaved.setEmail("user@example.com");
            clientUserToBeSaved.setPhoneNumber("012345675555");
            clientUserToBeSaved.setEnabled(true);
            clientUserToBeSaved.setPassword(bCryptPasswordEncoder.encode("Us3r!!123"));
            clientUserToBeSaved.setCreatedDate(getCurrentDate());
            clientUserToBeSaved.setUserRole(userRoleRepository.findByUserRole("ROLE_CLIENT")
                    .orElseThrow(() -> new EntityNotFoundException("Role CLIENT not found")));
            userRepository.save(clientUserToBeSaved);
        }
    }
}
