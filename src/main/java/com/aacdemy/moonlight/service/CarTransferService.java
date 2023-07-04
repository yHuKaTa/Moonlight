package com.aacdemy.moonlight.service;

import com.aacdemy.moonlight.dto.car.CarTransferRequestDto;
import com.aacdemy.moonlight.dto.car.CarTransferResponseDto;
import com.aacdemy.moonlight.entity.car.Car;
import com.aacdemy.moonlight.entity.car.CarTransfer;
import com.aacdemy.moonlight.entity.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public interface CarTransferService {

    CarTransferResponseDto addCarTransfer(CarTransferRequestDto carTransfer, String username) throws ParseException;

    List<CarTransfer> getAllTransfers();

    CarTransfer getTransferById(Long id);

    List<CarTransfer> getTransfersByUserEmail(String email);

    List<CarTransfer> getTransfersByCar(String car);

    String deleteTransferById(Long id);
}

