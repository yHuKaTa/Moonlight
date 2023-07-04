package com.aacdemy.moonlight.service;

import com.aacdemy.moonlight.dto.car.FileResourceResponseDto;
import com.aacdemy.moonlight.entity.car.FileResource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import com.aacdemy.moonlight.entity.car.FileResource;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

public interface FileResourceService {
    FileResourceResponseDto uploadImages(MultipartFile[] file, Long carId) throws IOException, SQLIntegrityConstraintViolationException;
    StreamingResponseBody downloadImageById(Long id);
    StreamingResponseBody downloadFirstImageOfCarByCarId(Long id);
    StreamingResponseBody downloadImagesOfCarByCarId(Long id) throws IOException;
    List<FileResource> findByCarId(Long carId);

}
