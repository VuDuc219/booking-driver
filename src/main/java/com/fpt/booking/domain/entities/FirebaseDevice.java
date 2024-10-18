package com.fpt.booking.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "firebase_info")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FirebaseDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "device_token")
    private String deviceToken;

    public FirebaseDevice(User user, String deviceToken) {
        this.user = user;
        this.deviceToken = deviceToken;
    }
}