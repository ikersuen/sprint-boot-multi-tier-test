package com.oocl.web.sampleWebApp.domain;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Table(name = "parking_lot")
public class ParkingLot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "parkinglot_id", length = 64, unique = true, nullable = false)
    private String parkingLotId;

    @Max(100)
    @Min(1)
    @Column(name = "capacity")
    private int capacity;

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Long getId() {
        return id;
    }

    public String getparkingLotId() {
        return parkingLotId;
    }

    public void setparkingLotId(String parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    protected ParkingLot() {}

    public ParkingLot(String parkingLotId, int capacity) {
        this.parkingLotId = parkingLotId;
        this.capacity = capacity;
    }
}