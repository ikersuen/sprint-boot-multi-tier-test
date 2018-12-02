package com.oocl.web.sampleWebApp.controllers;

import com.oocl.web.sampleWebApp.domain.ParkingBoy;
import com.oocl.web.sampleWebApp.domain.ParkingBoyRepository;
import com.oocl.web.sampleWebApp.domain.ParkingLot;
import com.oocl.web.sampleWebApp.domain.ParkingLotRepository;
import com.oocl.web.sampleWebApp.models.ParkingBoyResponse;
import com.oocl.web.sampleWebApp.models.ParkingBoyWithLotResponse;
import com.oocl.web.sampleWebApp.models.ParkingLotResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/parkingboys")
public class ParkingBoyResource {

    @Autowired
    private ParkingBoyRepository parkingBoyRepository;

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @GetMapping
    public ResponseEntity<ParkingBoyResponse[]> getAll() {
        final ParkingBoyResponse[] parkingBoys = parkingBoyRepository.findAll().stream()
            .map(ParkingBoyResponse::create)
            .toArray(ParkingBoyResponse[]::new);
        return ResponseEntity.ok(parkingBoys);
    }

    @GetMapping(value = "/{employeeId}")
    public ResponseEntity<ParkingBoyWithLotResponse> getByEmployeeId(@PathVariable String employeeId) {
        ParkingBoy parkingBoy = parkingBoyRepository.findByEmployeeId(employeeId);
        if(parkingBoy == null){
             return ResponseEntity.notFound().build();
        }
        List<ParkingLot> parkingLots = parkingLotRepository.findByParkingBoy(parkingBoy);
        final ParkingBoyWithLotResponse response = ParkingBoyWithLotResponse.create(
                parkingBoy.getEmployeeId(),
                parkingLots.stream().map(pl ->
                        ParkingLotResponse.create(pl.getParkingLotId(), pl.getCapacity(), pl.getAvailablePositionCount())).collect(Collectors.toList())
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<String> addNewParkingBoy(@RequestBody ParkingBoy parkingBoy){
        if (parkingBoyRepository.save(parkingBoy) != null){
            return buildCreateResponse(parkingBoy);
        }
            return buildCreateFailResponse();
    }

    @PostMapping("/{employeeId}/parkinglots")
    public ResponseEntity associateParkingBoyWithParkingLot(
            @PathVariable String employeeId,
            @RequestBody ParkingLotResponse request) {

        final ParkingBoy parkingBoy = parkingBoyRepository.findByEmployeeId(employeeId);

        final ParkingLot parkingLot = parkingLotRepository.findOneByParkingLotId(request.getParkingLotId());
        parkingLot.setParkingBoy(parkingBoy);
        parkingLotRepository.saveAndFlush(parkingLot);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private ResponseEntity<String> buildCreateResponse(ParkingBoy parkingBoy){
        String parkingBoyId = parkingBoy.getEmployeeId();
        URI location = URI.create("/parkingboys/" + parkingBoyId);
        return ResponseEntity.created(location).build();
    }

    private ResponseEntity<String> buildCreateFailResponse(){
        return ResponseEntity.badRequest().build();
    }

}
