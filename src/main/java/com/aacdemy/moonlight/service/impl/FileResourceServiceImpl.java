package com.aacdemy.moonlight.service.impl;

import com.aacdemy.moonlight.dto.car.FileResourceResponseDto;
import com.aacdemy.moonlight.entity.car.FileResource;
import com.aacdemy.moonlight.exception.EntityNotFoundException;
import com.aacdemy.moonlight.repository.car.CarRepository;
import com.aacdemy.moonlight.repository.car.FileResourceRepository;
import com.aacdemy.moonlight.service.FileResourceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Service
@RequiredArgsConstructor
public class FileResourceServiceImpl implements FileResourceService {
    private final FileResourceRepository fileResourceRepository;

    private final CarRepository carRepository;

    @Override
    @Transactional
    public FileResourceResponseDto uploadImages(MultipartFile[] files, Long carId) throws IOException, SQLIntegrityConstraintViolationException {
        if (carRepository.findById(carId).isEmpty()) {
            throw new EntityNotFoundException("Car with ID " + carId + " isn't found!");
        }
        if (carRepository.getReferenceById(carId).getFileResources().size() >= 3) {
            throw new SQLIntegrityConstraintViolationException("Images of the car have reached max count of 3 files.");
        }
        FileResource newImage = new FileResource();
        for (MultipartFile image : files) {
            newImage.setDataValue(image.getBytes());
            newImage.setImageName(String.valueOf(UUID.randomUUID()));
            newImage.setCar(carRepository.findById(carId).orElseThrow(
                    () -> new EntityNotFoundException(("Car with ID " + carId + " isn't found!"))));
            fileResourceRepository.save(newImage);
        }
        if (fileResourceRepository.findByImageName(newImage.getImageName()).isPresent()) {
            newImage = fileResourceRepository.findByImageName(newImage.getImageName()).get();
        }
        return FileResourceResponseDto.builder()
                .id(newImage.getId())
                .fileName(newImage.getImageName())
                .carId(newImage.getCar().getId())
                .build();
    }

    @Override
    public StreamingResponseBody downloadImageById(Long id) {
        return outputStream -> outputStream.write(fileResourceRepository.getReferenceById(id).getDataValue(),
                0,
                fileResourceRepository.getReferenceById(id).getDataValue().length);
    }

    @Override
    public StreamingResponseBody downloadFirstImageOfCarByCarId(Long id) {
        return outputStream -> outputStream.write(
                carRepository.getReferenceById(id).getFileResources().get(0).getDataValue(),
                0,
                carRepository.getReferenceById(id).getFileResources().get(0).getDataValue().length);
    }

    @Override
    public StreamingResponseBody downloadImagesOfCarByCarId(Long id) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zop = new ZipOutputStream(
                baos);
        for (FileResource file : carRepository.getReferenceById(id).getFileResources()) {
            zop.putNextEntry(new ZipEntry(file.getImageName() + ".jpg"));
            zop.write(file.getDataValue(),0,file.getDataValue().length);
            zop.closeEntry();
        }
        zop.finish();
        zop.close();
        baos.close();
        return outputStream -> outputStream.write(baos.toByteArray());
    }

    @Override
    public List<FileResource> findByCarId(Long carId) {
        return fileResourceRepository.findByCarId(carId);
    }
}
