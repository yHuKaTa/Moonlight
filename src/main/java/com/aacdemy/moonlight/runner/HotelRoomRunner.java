package com.aacdemy.moonlight.runner;

import com.aacdemy.moonlight.entity.hotel.Room;
import com.aacdemy.moonlight.entity.hotel.RoomType;
import com.aacdemy.moonlight.entity.hotel.RoomView;
import com.aacdemy.moonlight.repository.hotel.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;

@Component
public class HotelRoomRunner implements CommandLineRunner {
    private final RoomRepository roomRepository;

    public HotelRoomRunner(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roomRepository.count() == 0) {
            initStandardRooms();
            initStudioRooms();
            initApartments();
        }
    }

    public void initStandardRooms() {
        Room standardSeaViewRoom = new Room();
        standardSeaViewRoom.setRoomNumber(100);
        standardSeaViewRoom.setType(RoomType.STANDARD);
        standardSeaViewRoom.setView(RoomView.SEA);
        standardSeaViewRoom.setPeople(2);
        standardSeaViewRoom.setPrice(BigDecimal.valueOf(220.0));
        standardSeaViewRoom.setFacilities(new ArrayList<>());

        roomRepository.save(standardSeaViewRoom);

        Room standardSeaViewRoom2 = new Room();
        standardSeaViewRoom2.setRoomNumber(110);
        standardSeaViewRoom2.setType(RoomType.STANDARD);
        standardSeaViewRoom2.setView(RoomView.SEA);
        standardSeaViewRoom2.setPeople(2);
        standardSeaViewRoom2.setPrice(BigDecimal.valueOf(220.0));
        standardSeaViewRoom2.setFacilities(new ArrayList<>());

        roomRepository.save(standardSeaViewRoom2);

//        -----------------------------------------------------------------------------

        Room standardSwimmingPoolViewRoom = new Room();
        standardSwimmingPoolViewRoom.setRoomNumber(120);
        standardSwimmingPoolViewRoom.setType(RoomType.STANDARD);
        standardSwimmingPoolViewRoom.setView(RoomView.POOL);
        standardSwimmingPoolViewRoom.setPeople(2);
        standardSwimmingPoolViewRoom.setPrice(BigDecimal.valueOf(220.0));
        standardSwimmingPoolViewRoom.setFacilities(new ArrayList<>());

        roomRepository.save(standardSwimmingPoolViewRoom);

        Room standardSwimmingPoolViewRoom2 = new Room();
        standardSwimmingPoolViewRoom2.setRoomNumber(130);
        standardSwimmingPoolViewRoom2.setType(RoomType.STANDARD);
        standardSwimmingPoolViewRoom2.setView(RoomView.POOL);
        standardSwimmingPoolViewRoom2.setPeople(2);
        standardSwimmingPoolViewRoom2.setPrice(BigDecimal.valueOf(220.0));
        standardSwimmingPoolViewRoom2.setFacilities(new ArrayList<>());

        roomRepository.save(standardSwimmingPoolViewRoom2);

//        -----------------------------------------------------------------------------

        Room standardGardenViewRoom = new Room();
        standardGardenViewRoom.setRoomNumber(150);
        standardGardenViewRoom.setType(RoomType.STANDARD);
        standardGardenViewRoom.setView(RoomView.GARDEN);
        standardGardenViewRoom.setPeople(2);
        standardGardenViewRoom.setPrice(BigDecimal.valueOf(220.0));
        standardGardenViewRoom.setFacilities(new ArrayList<>());

        roomRepository.save(standardGardenViewRoom);

        Room standardGardenViewRoom2 = new Room();
        standardGardenViewRoom2.setRoomNumber(160);
        standardGardenViewRoom2.setType(RoomType.STANDARD);
        standardGardenViewRoom2.setView(RoomView.GARDEN);
        standardGardenViewRoom2.setPeople(2);
        standardGardenViewRoom2.setPrice(BigDecimal.valueOf(220.0));
        standardGardenViewRoom2.setFacilities(new ArrayList<>());

        roomRepository.save(standardGardenViewRoom2);

        Room standardGardenViewRoom3 = new Room();
        standardGardenViewRoom3.setRoomNumber(170);
        standardGardenViewRoom3.setType(RoomType.STANDARD);
        standardGardenViewRoom3.setView(RoomView.GARDEN);
        standardGardenViewRoom3.setPeople(2);
        standardGardenViewRoom3.setPrice(BigDecimal.valueOf(220.0));
        standardGardenViewRoom3.setFacilities(new ArrayList<>());

        roomRepository.save(standardGardenViewRoom3);

        Room standardGardenViewRoom4 = new Room();
        standardGardenViewRoom4.setRoomNumber(180);
        standardGardenViewRoom4.setType(RoomType.STANDARD);
        standardGardenViewRoom4.setView(RoomView.GARDEN);
        standardGardenViewRoom4.setPeople(2);
        standardGardenViewRoom4.setPrice(BigDecimal.valueOf(220.0));
        standardGardenViewRoom4.setFacilities(new ArrayList<>());

        roomRepository.save(standardGardenViewRoom4);

    }

    public void initStudioRooms() {
        Room studioSeaViewRoom = new Room();
        studioSeaViewRoom.setRoomNumber(200);
        studioSeaViewRoom.setType(RoomType.STUDIO);
        studioSeaViewRoom.setView(RoomView.SEA);
        studioSeaViewRoom.setPeople(3);
        studioSeaViewRoom.setPrice(BigDecimal.valueOf(320.0));
        studioSeaViewRoom.setFacilities(new ArrayList<>());

        roomRepository.save(studioSeaViewRoom);

        Room studioSeaViewRoom2 = new Room();
        studioSeaViewRoom2.setRoomNumber(210);
        studioSeaViewRoom2.setType(RoomType.STUDIO);
        studioSeaViewRoom2.setView(RoomView.SEA);
        studioSeaViewRoom2.setPeople(3);
        studioSeaViewRoom2.setPrice(BigDecimal.valueOf(320.0));
        studioSeaViewRoom2.setFacilities(new ArrayList<>());

        roomRepository.save(studioSeaViewRoom2);

//        -----------------------------------------------------------------

        Room studioSwimmingPoolViewRoom = new Room();
        studioSwimmingPoolViewRoom.setRoomNumber(220);
        studioSwimmingPoolViewRoom.setType(RoomType.STUDIO);
        studioSwimmingPoolViewRoom.setView(RoomView.POOL);
        studioSwimmingPoolViewRoom.setPeople(3);
        studioSwimmingPoolViewRoom.setPrice(BigDecimal.valueOf(320.0));
        studioSwimmingPoolViewRoom.setFacilities(new ArrayList<>());

        roomRepository.save(studioSwimmingPoolViewRoom);

        Room studioSwimmingPoolViewRoom2 = new Room();
        studioSwimmingPoolViewRoom2.setRoomNumber(230);
        studioSwimmingPoolViewRoom2.setType(RoomType.STUDIO);
        studioSwimmingPoolViewRoom2.setView(RoomView.POOL);
        studioSwimmingPoolViewRoom2.setPeople(3);
        studioSwimmingPoolViewRoom2.setPrice(BigDecimal.valueOf(320.0));
        studioSwimmingPoolViewRoom2.setFacilities(new ArrayList<>());

        roomRepository.save(studioSwimmingPoolViewRoom2);

//       --------------------------------------------------------

        Room studioGardenViewRoom = new Room();
        studioGardenViewRoom.setRoomNumber(240);
        studioGardenViewRoom.setType(RoomType.STUDIO);
        studioGardenViewRoom.setView(RoomView.GARDEN);
        studioGardenViewRoom.setPeople(3);
        studioGardenViewRoom.setPrice(BigDecimal.valueOf(320.0));
        studioGardenViewRoom.setFacilities(new ArrayList<>());

        roomRepository.save(studioGardenViewRoom);

        Room studioGardenViewRoom2 = new Room();
        studioGardenViewRoom2.setRoomNumber(250);
        studioGardenViewRoom2.setType(RoomType.STUDIO);
        studioGardenViewRoom2.setView(RoomView.GARDEN);
        studioGardenViewRoom2.setPeople(3);
        studioGardenViewRoom2.setPrice(BigDecimal.valueOf(320.0));
        studioGardenViewRoom2.setFacilities(new ArrayList<>());

        roomRepository.save(studioGardenViewRoom2);

    }

    public void initApartments() {
        Room apartmentSeaView = new Room();
        apartmentSeaView.setRoomNumber(300);
        apartmentSeaView.setType(RoomType.APARTMENT);
        apartmentSeaView.setView(RoomView.SEA);
        apartmentSeaView.setPeople(4);
        apartmentSeaView.setPrice(BigDecimal.valueOf(520.0));
        apartmentSeaView.setFacilities(new ArrayList<>());

        roomRepository.save(apartmentSeaView);

        Room apartmentSeaView2 = new Room();
        apartmentSeaView2.setRoomNumber(320);
        apartmentSeaView2.setType(RoomType.APARTMENT);
        apartmentSeaView2.setView(RoomView.SEA);
        apartmentSeaView2.setPeople(4);
        apartmentSeaView2.setPrice(BigDecimal.valueOf(520.0));
        apartmentSeaView2.setFacilities(new ArrayList<>());

        roomRepository.save(apartmentSeaView2);

//        ---------------------------------------------------------------

        Room apartmentSwimmingPoolView = new Room();
        apartmentSwimmingPoolView.setRoomNumber(350);
        apartmentSwimmingPoolView.setType(RoomType.APARTMENT);
        apartmentSwimmingPoolView.setView(RoomView.POOL);
        apartmentSwimmingPoolView.setPeople(4);
        apartmentSwimmingPoolView.setPrice(BigDecimal.valueOf(520.0));
        apartmentSwimmingPoolView.setFacilities(new ArrayList<>());

        roomRepository.save(apartmentSwimmingPoolView);

    }

}