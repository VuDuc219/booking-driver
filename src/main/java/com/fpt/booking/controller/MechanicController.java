package com.fpt.booking.controller;

import com.fpt.booking.controller.base.BaseController;
import com.fpt.booking.domain.enums.RequestTicketsStatus;
import com.fpt.booking.domain.payload.request.GarageRequest;
import com.fpt.booking.domain.payload.request.InspectionResultsRequest;
import com.fpt.booking.domain.payload.request.MotoRequest;
import com.fpt.booking.domain.payload.request.RepairQuoteRequest;
import com.fpt.booking.services.MechanicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mechanic")
@RequiredArgsConstructor
public class MechanicController extends BaseController {

    private final MechanicService mechanicService;

    @GetMapping("/get-all-request-ticket")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get all request ticket")
    public ResponseEntity<?> getAllRequestTicket(@RequestParam RequestTicketsStatus status,
                                                 @RequestParam(required = false, defaultValue = "1") Integer pageNo,
                                                 @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        return createSuccessResponse("Danh sách của phiếu yêu cầu", mechanicService.getAllRequestTicket(status,pageNo, pageSize));
    }


    @PostMapping("/add-new-garage")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create a new garage for mechanic")
    public ResponseEntity<?> createANewGarage(@RequestBody GarageRequest garageRequest){
        return createSuccessResponse("Tạo xe mới", mechanicService.createNewGarage(garageRequest));
    }

    @PostMapping("/upgrade-info-garage")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Upgrade garage for mechanic")
    public ResponseEntity<?> upgradeGarageForMechanic(@RequestBody GarageRequest garageRequest){
        return createSuccessResponse("Cập nhập garage của mechanic", mechanicService.updateInfoGarage(garageRequest));
    }

    @DeleteMapping("/delete-info-garage")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Delete garage for mechanic")
    public ResponseEntity<?> deleteGarageForMechanic(){
        return createSuccessResponse("Xóa garage của mechanic ", mechanicService.deleteInfoGarage());
    }

    @PostMapping("/{id}/confirm-appointment")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Confirm appointment for driver")
    public ResponseEntity<?> confirmAppointmentForDriver(@PathVariable Long id){
        mechanicService.confirmAppointment(id);
        return createSuccessResponse("Xác nhận cuộc hẹn.");
    }

    @PostMapping("/{id}/send-vehicle-inspection-results")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Add vehicle inspection results")
    public ResponseEntity<?> addVehicleInspectionResults(@PathVariable Long id, @RequestBody InspectionResultsRequest inspectionResultsRequest) {
        return createSuccessResponse("Add vehicle inspection results", mechanicService.addVehicleInspectionResults(id, inspectionResultsRequest));
    }

    @PutMapping("/{id}/upgrade-vehicle-inspection-results")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Edit for vehicle inspection results")
    public ResponseEntity<?> editForVehicleInspectionResults(@PathVariable Long id, @RequestBody InspectionResultsRequest inspectionResultsRequest){
        return createSuccessResponse("Chỉnh sửa thông tin kết quả khám", mechanicService.editInfoOfVehicleInspectionResults(id, inspectionResultsRequest));
    }

    @PostMapping("/{id}/send-repair-quote")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Send repair quote for customer")
    public ResponseEntity<?> sendRepairQuote(@PathVariable Long id, @RequestBody RepairQuoteRequest repairQuoteRequest){
        return createSuccessResponse("Send repair quote for customer", mechanicService.sendRepairQuoteForCustomer(id, repairQuoteRequest));
    }

    @PutMapping("/{id}/upgrade-repair-quote")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Edit for repair quote")
    public ResponseEntity<?> editForRepairQuote(@PathVariable Long id, @RequestBody RepairQuoteRequest repairQuoteRequest){
        return createSuccessResponse("Chỉnh sửa thông tin kết quả khám", mechanicService.editInfoOfRepairQuote(id, repairQuoteRequest));
    }

    @PostMapping("/{id}/moto-repair-process")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Moto repair process for garage")
    public ResponseEntity<?> motoRepairProcess(@PathVariable Long id) {
        return createSuccessResponse("Xe đang sửa chữa.", mechanicService.motoRepairProcess(id));
    }

    @PostMapping("/{id}/fixed")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Fixed moto")
    public ResponseEntity<?> fixedMoto(@PathVariable Long id) {
        return createSuccessResponse("Đã sửa xong.", mechanicService.fixedMoto(id));
    }

    @PostMapping("/{id}/garage-handover-moto")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Garage handover moto")
    public ResponseEntity<?> garageHandoverMoto(@PathVariable Long id){
        return createSuccessResponse("Bàn giao xe", mechanicService.garageHandoverMoto(id));
    }

    @PostMapping("/{id}/confirm-payment")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Confirm payment")
    public ResponseEntity<?> confirmPayment(@PathVariable Long id) {
        return createSuccessResponse("Xác nhận thanh toán", mechanicService.confirmPayment(id));
    }

    @PostMapping("/{id}/canceled")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Garage canceled for request ticket")
    public ResponseEntity<?> garageCanceled(@PathVariable Long id) {
        return createSuccessResponse("Mechanic hủy phiếu yêu cầu", mechanicService.garageCanceledForRequestTicket(id));
    }

}
