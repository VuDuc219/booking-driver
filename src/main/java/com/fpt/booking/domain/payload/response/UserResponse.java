package com.fpt.booking.domain.payload.response;

import com.fpt.booking.domain.enums.RoleName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private Boolean isLocked;

    private Boolean isActive;

    private Boolean isMechanic;

    private String address;

    private String phone;

    private String imageUrl;

    private GarageResponse garage;

    private MotoResponse moto;

    private RoleName roleName;
    public RoleName getRoleName() {
        if (garage == null) {
            return RoleName.ROLE_USER;
        } else {
            return RoleName.ROLE_MECHANIC;
        }
    }

    public void setRoleName(RoleName roleName) {
        this.roleName = roleName;
    }

}
