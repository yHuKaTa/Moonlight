package com.aacdemy.moonlight.repository.user;

import com.aacdemy.moonlight.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByPassportID(String passportID);

    List<User> findByFirstName(String firstName);

    List<User> findByLastName(String lastName);

    List<User> findByUserRoleUserRole(String userRole);

    List<User> findByFirstNameAndLastName(String firstName, String lastName);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByPassportID(String passportID);
}
