package com.oocl.web.sampleWebApp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oocl.web.sampleWebApp.domain.ParkingLot;

import java.util.Objects;

public class ParkingLotResponse {
    private String parkingLotId;
    private int capacity;
    private int availablePositionCount;

    public String getparkingLotId() {
        return parkingLotId;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setparkingLotId(String parkingLotId) {
        this.parkingLotId = parkingLotId;
    }
    public void setAvailablePositionCount(int availablePositionCount) {this.availablePositionCount = availablePositionCount;}

    public static ParkingLotResponse create(String parkingLotId, int capacity, int availablePositionCount) {
        Objects.requireNonNull(parkingLotId);

        final ParkingLotResponse response = new ParkingLotResponse();
        response.setparkingLotId(parkingLotId);
        response.setCapacity(capacity);
        response.setAvailablePositionCount(availablePositionCount);

        return response;
    }

    public static ParkingLotResponse create(ParkingLot entity) {
        return create(entity.getparkingLotId(), entity.getCapacity(), entity.getAvailablePositionCount());
    }

    @JsonIgnore
    public boolean isValid() {
        return parkingLotId != null;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getAvailablePositionCount() {
        return availablePositionCount;
    }
}
