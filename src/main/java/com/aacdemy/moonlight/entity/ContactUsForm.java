package com.aacdemy.moonlight.entity;

import com.aacdemy.moonlight.util.annotation.PhoneNumber;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "CONTACT_US")
public class ContactUsForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false, length = 40)
    private String name;

    @Column(name = "EMAIL", nullable = false, length = 40)
    private String email;

    @Column(name = "PHONE_NUMBER", nullable = false, length = 20)
    @PhoneNumber(message = "Doesn't seem to be a valid phone number")
    private String phoneNumber;

    @Column(name = "MESSAGE", nullable = false)
    private String message;

}
