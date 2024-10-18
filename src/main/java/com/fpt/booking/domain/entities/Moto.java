package com.fpt.booking.domain.entities;

import com.fpt.booking.domain.enums.DateAudit;
import com.fpt.booking.domain.payload.request.MotoRequest;
import jakarta.persistence.*;
import lombok.*;



@Entity
@Table(name = "moto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Moto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "license_plate")
    private String licensePlate;
    private String brand;
    @Column(name = "body_style")
    private String bodyStyle;
    @Column(name = "machine_number")
    private String machineNumber;
    @Column(name = "frame_number")
    private String frameNumber;
    private String color;
    @Column(name = "car_image")
    private String carImage;
    private String origin;
    private Integer year;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    public Moto(MotoRequest motoRequest, User user) {
        this.licensePlate = motoRequest.getLicensePlate();
        this.brand = motoRequest.getBrand();
        this.bodyStyle = motoRequest.getBodyStyle();
        this.color = motoRequest.getColor();
        this.carImage = motoRequest.getCarImage();
        this.origin = motoRequest.getOrigin();
        this.machineNumber = motoRequest.getMachineNumber();
        this.frameNumber = motoRequest.getFrameNumber();
        this.year = motoRequest.getYear();
        this.user = user;
    }
}
