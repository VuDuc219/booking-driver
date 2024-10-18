package com.fpt.booking.repository;

import com.fpt.booking.domain.entities.Moto;
import com.fpt.booking.domain.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MotoRepository extends JpaRepository<Moto, Long> {
    Optional<Moto> findByUser(User user);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM moto  m WHERE m.id = :id " , nativeQuery = true)
    void deleteMotoOfCustomer(Long id);
}
