package com.ayush.pidtracker.repository;


import com.ayush.pidtracker.entity.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StorageRepository extends JpaRepository<ImageData,Long> {



    Optional<ImageData> findByName(String fileName);

}
