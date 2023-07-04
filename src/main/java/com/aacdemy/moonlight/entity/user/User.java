package com.aacdemy.moonlight.entity.user;

import com.aacdemy.moonlight.util.annotation.PhoneNumber;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "USERS")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "FIRST_NAME", nullable = false)
    @Size(min = 2, max = 255, message = "The user's first name should consist of 2 to 255 characters")
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    @Size(min = 2, max = 255, message = "The user's last name should consist of 2 to 255 characters")
    private String lastName;

    @Column(name = "EMAIL", nullable = false, unique = true)
    @Size(min = 5, max = 255, message = "The user's email should consist of 5 to 255 characters")
    @Email(regexp = "^[^\s@]+@[^\s@]+\\.[^\s@]+$", message = "The user's email should be valid email format")
    private String email;

    @Column(name = "PHONE_NUMBER", nullable = false)
    @Size(min = 2, max = 15, message = "The user's phone number should consist of 2 to 15 digits")
    @PhoneNumber(message = "Doesn't seem to be a valid phone number")
    private String phoneNumber;

    @Column(name = "PASSPORT_ID", unique = true)
    @Size(min = 9, max = 15, message = "The user's passportID should consist of 9 to 15 symbols")
    @Pattern(regexp = "[\\d\\s]{9,15}", message = "The user's passportID should be valid format with characters and digits")
    private String passportID;

    @Column(name = "PASSWORD")
    @Size(min = 8, max = 255, message = "The user's password should consist of 8 to 255 symbols")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,255}", message = "The user's password must consist at least 1 digit,lowercase character and uppercase character")
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ROLE_ID")
    private UserRole userRole;

    @Column(name = "ENABLED", columnDefinition = "BIT", length = 1)
    private boolean enabled;

    @Column(name = "CREATED_DATE", nullable = false, updatable = false)
    protected Date createdDate;

    @Column(name = "DATE_OF_MODIFICATION")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date modifiedDate;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.getUserRole()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
