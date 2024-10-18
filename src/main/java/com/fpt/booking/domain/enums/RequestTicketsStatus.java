package com.fpt.booking.domain.enums;

import lombok.Getter;

@Getter
public enum RequestTicketsStatus {
    NEW("NEW"), // Trạng thái khi mới tạo phiếu
    GARAGE_CONFIRMED("GARAGE_CONFIRMED"), // Trạng thái khi garage xác nhận sửa xe
    GARAGE_NO_ACTION("GARAGE_NO_ACTION"), // Trạng thái khi không có bất cứ garage nào nhận xe sau 5p
    CUSTOMER_CHECKED_IN("CUSTOMER_CHECKED_IN"), // Trạng thái xe sẽ đã được check in để chuẩn bị sửa chữa
    CHECKED("CHECKED"), // Trạng thái kiểm tra xe xong
    CHECKING("CHECKING"), // Trạng thái kiểm tra xe có bị hư hại gì không
    WAITING_CUSTOMER_APPROVE_PRICE("WAITING_CUSTOMER_APPROVE_PRICE"), // Trạng thái khi garage đưa bản báo giá cho khách hàng
    CUSTOMER_APPROVED_PRICE("CUSTOMER_APPROVED_PRICE"), // Trạng thái khách hàng chấp nhận giá ở phía garage
    PROCESSING("PROCESSING"), // Trạng thái bắt đầu sửa chữa
    FIXED("FIXED"), // Trạng thái đã sửa xong
    PENDING_PAYMENT("PENDING_PAYMENT"), // Trạng thái thanh toán
    COMPLETED_PAYMENT("COMPLETED_PAYMENT"),
    GARAGE_HANDED_OVER_MOTO("GARAGE_HANDED_OVER_MOTO"), // Trạng thái garage bàn giao xe
    COMPLETED("COMPLETED"), // Trạng thái hoàn thành sau khi bàn giao, tự động cronjob trong vòng 1 ngày
    CANCELED("CANCELED"), // Trạng thái khi khách hàng hủy phiếu
    GARAGE_CANCELED("GARAGE_CANCELED"); // Trạng thái khi garage hủy phiếu

    private String value;

    RequestTicketsStatus(String value) {
        this.value = value;
    }

}