package com.aacdemy.moonlight.repository.screen;

import com.aacdemy.moonlight.entity.PaymentStatus;
import com.aacdemy.moonlight.entity.screen.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ScreenRepository extends JpaRepository<Screen, Long> {

    Optional<Screen> findByName(String name);
}
