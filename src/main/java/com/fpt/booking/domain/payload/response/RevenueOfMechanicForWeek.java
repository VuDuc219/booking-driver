package com.fpt.booking.domain.payload.response;

public interface RevenueOfMechanicForWeek {
    String getName();
    String getYear();
    String getMonth();
    String getWeek();
    Double getSumOfRevenue();
    Integer getNumOfRq();

}
