package com.oocl.web.sampleWebApp.models;

import java.util.List;
import java.util.Objects;

public class ParkingBoyWithLotResponse {
    private String employeeId;
    private List<ParkingLotResponse> parkingLots;

    public String getEmployeeId() {
        return employeeId;
    }

    public List<ParkingLotResponse> getParkingLots() {
        return parkingLots;
    }

    public static ParkingBoyWithLotResponse create(String employeeId, List<ParkingLotResponse> parkingLots) {
        Objects.requireNonNull(employeeId);
        Objects.requireNonNull(parkingLots);

        final ParkingBoyWithLotResponse response = new ParkingBoyWithLotResponse();
        response.employeeId = employeeId;
        response.parkingLots = parkingLots;
        return response;

    }
}
