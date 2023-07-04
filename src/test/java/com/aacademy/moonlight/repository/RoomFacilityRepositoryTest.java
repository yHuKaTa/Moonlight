package com.aacademy.moonlight.repository;

import com.aacdemy.moonlight.Application;
import com.aacdemy.moonlight.entity.hotel.Room;
import com.aacdemy.moonlight.entity.hotel.RoomFacility;
import com.aacdemy.moonlight.entity.hotel.RoomType;
import com.aacdemy.moonlight.entity.hotel.RoomView;
import com.aacdemy.moonlight.repository.hotel.RoomFacilityRepository;
import com.aacdemy.moonlight.repository.hotel.RoomRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ContextConfiguration(classes = Application.class)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RoomFacilityRepositoryTest {

    @Autowired
    private RoomFacilityRepository roomFacilityRepository;

    @Autowired
    private RoomRepository roomRepository;

    @BeforeAll
    public void init() {
        Room room = Room.builder()
                .roomNumber(1)
                .type(RoomType.STANDARD)
                .view(RoomView.SEA)
                .price(BigDecimal.TEN)
                .people(1)
                .build();

        RoomFacility facility = RoomFacility.builder().facility("CableTV").room(List.of(room)).build();
        room.setFacilities(List.of(facility));

        room = roomRepository.save(room);
        facility = roomFacilityRepository.save(facility);
    }

    @Test
    public void testFindByFacility() {
        Optional<RoomFacility> facility = roomFacilityRepository.findByFacility("CableTV");
        Assertions.assertNotNull(facility);
        Assertions.assertEquals("CableTV", facility.get().getFacility());
    }

    @Test
    public void testExistsByFacility() {
        Assertions.assertTrue(roomFacilityRepository.existsByFacility("CableTV"));
    }

    @AfterAll
    public void erase(){
        Room room = roomRepository.findByRoomNumber(1).get();
        RoomFacility facility = roomFacilityRepository.findByFacility("CableTV").get();

        roomFacilityRepository.delete(facility);
        roomRepository.delete(room);
    }
}
