package com.oocl.web.sampleWebApp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oocl.web.sampleWebApp.domain.ParkingBoy;

import java.util.List;
import java.util.Objects;

public class ParkingBoyResponse {
    private String employeeId;

    public String getEmployeeId() {
        return employeeId;
    }

    private List<ParkingLotResponse> parkingLots;

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public static ParkingBoyResponse create(String employeeId) {
        Objects.requireNonNull(employeeId);

        final ParkingBoyResponse response = new ParkingBoyResponse();
        response.setEmployeeId(employeeId);
        return response;
    }

    public static ParkingBoyResponse create(ParkingBoy entity) {
        return create(entity.getEmployeeId());
    }

    @JsonIgnore
    public boolean isValid() {
        return employeeId != null;
    }

    public List<ParkingLotResponse> getParkingLots() {
        return parkingLots;
    }

    public void setParkingLots(List<ParkingLotResponse> parkingLots) {
        this.parkingLots = parkingLots;
    }

    public static ParkingBoyResponse create(String employeeId, List<ParkingLotResponse> parkingLots) {
        Objects.requireNonNull(employeeId);

        final ParkingBoyResponse response = new ParkingBoyResponse();
        response.setEmployeeId(employeeId);
        response.setParkingLots(parkingLots);
        return response;
    }
}
