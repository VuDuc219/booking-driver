package com.fpt.booking.domain.enums;

import lombok.Getter;

@Getter
public enum RoleName {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_MECHANIC("ROLE_MECHANIC");


    private String value;

    RoleName(String value){
        this.value = value;
    }

}
