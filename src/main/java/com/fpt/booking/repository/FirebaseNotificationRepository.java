package com.fpt.booking.repository;

import com.fpt.booking.domain.entities.FirebaseNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FirebaseNotificationRepository extends JpaRepository<FirebaseNotification, Long>, JpaSpecificationExecutor<FirebaseNotification> {
}
