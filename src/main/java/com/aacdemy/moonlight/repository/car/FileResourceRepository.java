package com.aacdemy.moonlight.repository.car;

import com.aacdemy.moonlight.entity.car.FileResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileResourceRepository extends JpaRepository<FileResource, Long> {
    Optional<FileResource> findByImageName(String imageName);
    List<FileResource> findByCarId(Long id);

}
