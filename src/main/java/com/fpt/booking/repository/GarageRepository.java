package com.fpt.booking.repository;

import com.fpt.booking.domain.entities.Garage;
import com.fpt.booking.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GarageRepository extends JpaRepository<Garage, Long> {
    Optional<Garage> findByUser(User user);
}
