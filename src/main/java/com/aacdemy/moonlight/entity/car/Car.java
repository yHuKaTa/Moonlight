package com.aacdemy.moonlight.entity.car;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "CARS")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(nullable = false, name = "MAKE")
    @Size(min = 2, max = 45)
    private String make;

    @Column(nullable = false, name = "MODEL")
    @Size(min = 1, max = 45)
    private String model;

    @Column(nullable = false, name = "CAR_YEAR")
    @Digits(integer=4, fraction=0, message="Year must be at least 4 digits")
    private int year;

    @ManyToOne(fetch = FetchType.EAGER)
    // NOT FetchType.LAZY or :
    // com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: java.util.ArrayList[0]->com.aacdemy.moonlight.entity.car.Car["carCategory"]->com.aacdemy.moonlight.entity.car.CarCategory$HibernateProxy$FIlp8iS5["hibernateLazyInitializer"])
    @JsonManagedReference
    private CarCategory carCategory;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, fetch = FetchType.LAZY) //still displayed as EAGER !?
    @JsonManagedReference
    private List<FileResource> fileResources = new ArrayList<>();

}
