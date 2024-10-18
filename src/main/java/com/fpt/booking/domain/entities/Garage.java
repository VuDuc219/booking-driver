package com.fpt.booking.domain.entities;

import com.fpt.booking.domain.enums.DateAudit;
import com.fpt.booking.domain.payload.request.GarageRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "garage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Garage extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String username;
    private String address;
    @Column(name = "lattitude")
    private Float latiTude;
    @Column(name = "longtitude")
    private Float longiTude;
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    public Garage(GarageRequest garageRequest, User user) {
        this.name = garageRequest.getName();
        this.username = garageRequest.getUsername();
        this.address = garageRequest.getAddress();
        this.latiTude = garageRequest.getLatiTude();
        this.longiTude = garageRequest.getLongiTude();
        this.description = garageRequest.getDescription();
        this.user = user;
    }
}
