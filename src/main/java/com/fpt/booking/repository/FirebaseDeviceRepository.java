package com.fpt.booking.repository;

import com.fpt.booking.domain.entities.FirebaseDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FirebaseDeviceRepository extends JpaRepository<FirebaseDevice, Long> {

    List<FirebaseDevice> findByUserId(Long userId);

    List<FirebaseDevice> findByDeviceToken(String deviceToken);
}
