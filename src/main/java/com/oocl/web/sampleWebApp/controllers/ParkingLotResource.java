package com.oocl.web.sampleWebApp.controllers;

import com.oocl.web.sampleWebApp.domain.ParkingBoy;
import com.oocl.web.sampleWebApp.domain.ParkingBoyRepository;
import com.oocl.web.sampleWebApp.domain.ParkingLot;
import com.oocl.web.sampleWebApp.domain.ParkingLotRepository;
import com.oocl.web.sampleWebApp.models.ParkingBoyResponse;
import com.oocl.web.sampleWebApp.models.ParkingLotResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/parkinglots")
public class ParkingLotResource {

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @GetMapping
    public ResponseEntity<ParkingLotResponse[]> getAll() {
        final ParkingLotResponse[] parkingLots = parkingLotRepository.findAll().stream()
                .map(ParkingLotResponse::create)
                .toArray(ParkingLotResponse[]::new);
        return ResponseEntity.ok(parkingLots);
    }

    @PostMapping
    public ResponseEntity<String> addNewParkingLot(@RequestBody @Valid ParkingLot parkingLot, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return buildCreateErrorResponse(bindingResult);
        }
        if (parkingLotRepository.save(parkingLot) != null){
            return buildCreateResponse(parkingLot);
        }
        return buildCreateFailResponse();
    }

    private ResponseEntity<String> buildCreateResponse(ParkingLot parkingLot){
        String parkingLotId = parkingLot.getparkingLotId();
        URI location = URI.create("/parkinglots/" + parkingLotId);
        return ResponseEntity.created(location).build();
    }

    private ResponseEntity<String> buildCreateFailResponse(){
        return ResponseEntity.badRequest().build();
    }

    private ResponseEntity<String> buildCreateErrorResponse(BindingResult bindingResult){
        String defaultMessage = bindingResult.getFieldError().getDefaultMessage();
        return ResponseEntity.badRequest().body(defaultMessage);
    }

}
