package com.oocl.web.sampleWebApp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oocl.web.sampleWebApp.domain.ParkingLot;

import java.util.Objects;

public class ParkingLotResponse {
    private String parkingLotId;
    private int capacity;

    public String getparkingLotId() {
        return parkingLotId;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setparkingLotId(String parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public static ParkingLotResponse create(String parkingLotId, int capacity) {
        Objects.requireNonNull(parkingLotId);

        final ParkingLotResponse response = new ParkingLotResponse();
        response.setparkingLotId(parkingLotId);
        response.setCapacity(capacity);

        return response;
    }

    public static ParkingLotResponse create(ParkingLot entity) {
        return create(entity.getparkingLotId(), entity.getCapacity());
    }

    @JsonIgnore
    public boolean isValid() {
        return parkingLotId != null;
    }

    public int getCapacity() {
        return capacity;
    }
}
