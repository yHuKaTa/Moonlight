package com.aacademy.moonlight.repository;

import com.aacdemy.moonlight.Application;
import com.aacdemy.moonlight.entity.hotel.Room;
import com.aacdemy.moonlight.entity.hotel.RoomFacility;
import com.aacdemy.moonlight.entity.hotel.RoomType;
import com.aacdemy.moonlight.entity.hotel.RoomView;
import com.aacdemy.moonlight.repository.hotel.ReservationRepository;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DataJpaTest
@ContextConfiguration(classes = Application.class)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HotelRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomFacilityRepository roomFacilityRepository;

    @BeforeAll
    public void init(){
        Room room = Room.builder()
                .roomNumber(20)
                .id(1L)
                .people(1)
                .price(new BigDecimal("10.00"))
                .type(RoomType.STANDARD)
                .view(RoomView.SEA)
                .build();

        RoomFacility facility = RoomFacility.builder().facility("WiFi").room(List.of(room)).build();
        room.setFacilities(List.of(facility));

        room = roomRepository.save(room);
        facility = roomFacilityRepository.save(facility);
    }

    @Test
    public void testFindByRoomNumber() {
        Optional<Room> foundRoom = roomRepository.findByRoomNumber(20);
        assertEquals(20, foundRoom.get().getRoomNumber());

    }

    @Test
    public void testFindByType() {
        List<Room> foundRoom = roomRepository.findByType(RoomType.STANDARD);
        assertEquals(1,foundRoom.size());
        assertEquals(RoomType.STANDARD, foundRoom.get(0).getType());
    }

    @Test
    public void testFindByView() {
        List<Room> foundRooms = roomRepository.findByView(RoomView.SEA);
        assertEquals(1,foundRooms.size());
        assertEquals(RoomView.SEA,foundRooms.get(0).getView());

    }

    @Test
    public void testFindByPrice() {
        List<Room> foundRooms = roomRepository.findByPrice(BigDecimal.TEN);
        assertEquals(1, foundRooms.size());
        assertEquals(10, foundRooms.listIterator().next().getPrice().intValue());
    }

    @Test
    public void testFindByPeople() {
        List<Room> foundRooms = roomRepository.findByPeople(1);
        assertEquals(1, foundRooms.size());
        assertEquals(1, foundRooms.get(0).getPeople());

    }
    @Test
    public void testFindByFacilities() {
        List<Room> foundRooms = roomRepository.findByFacilitiesContaining("WiFi");
        assertEquals(1,foundRooms.size());
        assertEquals("WiFi",foundRooms.get(0).getFacilities().get(0).getFacility());
    }

    @AfterAll
    public void erase(){
        Room room = roomRepository.findById(1L).get();
        RoomFacility facility = roomFacilityRepository.findById(1L).get();

        roomFacilityRepository.delete(facility);
        roomRepository.delete(room);
    }
}
