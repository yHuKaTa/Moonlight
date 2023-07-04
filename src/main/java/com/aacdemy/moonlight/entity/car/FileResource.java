package com.aacdemy.moonlight.entity.car;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "FILE_RESOURCES")
public class FileResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "IMAGE_NAME", unique = true)
    private String imageName;

    @Lob
    @Column(name = "IMAGE", columnDefinition = "MEDIUMBLOB")
    private byte[] dataValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Car car;
}
