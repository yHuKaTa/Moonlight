package com.aacdemy.moonlight.runner;

import com.aacdemy.moonlight.entity.screen.Screen;
import com.aacdemy.moonlight.repository.screen.ScreenEventRepository;
import com.aacdemy.moonlight.entity.screen.ScreenSeat;
import com.aacdemy.moonlight.repository.screen.ScreenRepository;
import com.aacdemy.moonlight.repository.screen.ScreenSeatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BarScreenRunner implements CommandLineRunner {
    private final ScreenEventRepository screenEventRepository;

    public BarScreenRunner(ScreenEventRepository screenEventRepository) {
        this.screenEventRepository = screenEventRepository;
    }

    @Autowired
    ScreenRepository screenRepository;
    @Autowired
    ScreenSeatsRepository screenSeatsRepository;
    Screen screen1;
    Screen screen2;
    Screen screen3;
    List<ScreenSeat> screen1Seats;
    List<ScreenSeat> screen2Seats;
    List<ScreenSeat> screen3Seats;
    String s1 = "ScreenOne";
    String s2 = "ScreenTwo";
    String s3 = "ScreenTree";

    @Override
    public void run(String... args) throws Exception {

        if (screenRepository.count() == 0) {
            screen1 = Screen.builder()
                    .name(s1)
//                    .seat(screen1Seats)
                    .build();

            screen2 = Screen.builder()
                    .name(s2)
//                    .seat(screen2Seats)
                    .build();

            screen3 = Screen.builder()
                    .name(s3)
//                    .seat(screen3Seats)
                    .build();
            screenRepository.saveAll(List.of(screen1, screen2, screen3));
        }
        initSeats();
    }

    public void initSeats() {
        Screen sc1 = screenRepository.findByName(s1).orElseThrow();
        Screen sc2 = screenRepository.findByName(s2).orElseThrow();
        Screen sc3 = screenRepository.findByName(s3).orElseThrow();

        if (screenSeatsRepository.count() == 0) {
            screen1Seats = new ArrayList<>();
            for (int i = 1; i <= 21; i++) {
                ScreenSeat seat = ScreenSeat.builder()
                        .seatPosition(i)
                        .screen(sc1)
                        .build();
                screen1Seats.add(seat);
            }
            screen2Seats = new ArrayList<>();
            for (int i = 1; i <= 21; i++) {
                ScreenSeat seat = ScreenSeat.builder()
                        .seatPosition(i)
                        .screen(sc2)
                        .build();
                screen2Seats.add(seat);
            }
            screen3Seats = new ArrayList<>();
            for (int i = 1; i <= 21; i++) {
                ScreenSeat seat = ScreenSeat.builder()
                        .seatPosition(i)
                        .screen(sc3)
                        .build();
                screen3Seats.add(seat);
            }
            screenSeatsRepository.saveAll(screen1Seats);
            screenSeatsRepository.saveAll(screen2Seats);
            screenSeatsRepository.saveAll(screen3Seats);
        }
    }
}
