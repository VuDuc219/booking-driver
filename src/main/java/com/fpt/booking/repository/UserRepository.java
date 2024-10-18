package com.fpt.booking.repository;


import com.fpt.booking.domain.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    @Query(value = "SELECT u FROM User u WHERE u.phone = :username OR u.email = :username")
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByOtp(Integer otp);

    Boolean existsByPhone(String phone);

    List<User> findByName(String name);

    long count();

    @Modifying
    @Transactional
    @Query(value = "UPDATE users u SET u.access_token = :accessToken WHERE u.id = :id", nativeQuery = true)
    void updateAccessTokenForUser(String accessToken, Long id);


    @Modifying
    @Transactional
    @Query(value = "UPDATE user_roles us SET us.role_id = 2 WHERE us.user_id = :userId", nativeQuery = true)
    void updateRoleOfCustomer(Long userId);
}
