package com.oocl.web.sampleWebApp.controllers;

import com.oocl.web.sampleWebApp.domain.ParkingBoy;
import com.oocl.web.sampleWebApp.domain.ParkingBoyRepository;
import com.oocl.web.sampleWebApp.models.ParkingBoyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/parkingboys")
public class ParkingBoyResource {

    @Autowired
    private ParkingBoyRepository parkingBoyRepository;

    @GetMapping
    public ResponseEntity<ParkingBoyResponse[]> getAll() {
        final ParkingBoyResponse[] parkingBoys = parkingBoyRepository.findAll().stream()
            .map(ParkingBoyResponse::create)
            .toArray(ParkingBoyResponse[]::new);
        return ResponseEntity.ok(parkingBoys);
    }

    @GetMapping(value = "/{employeeId}")
    public ResponseEntity<ParkingBoyResponse> getByEmployeeId(@PathVariable String employeeId) {
        ParkingBoy parkingBoy = parkingBoyRepository.findByEmployeeId(employeeId);
        if(parkingBoy == null){
             return ResponseEntity.badRequest().build();
        }
        ParkingBoyResponse parkingBoyResponse = ParkingBoyResponse.create(parkingBoy);
        return ResponseEntity.ok(parkingBoyResponse);
    }

    @PostMapping
    public ResponseEntity<String> addNewParkingBoy(@RequestBody ParkingBoy parkingBoy){
        if (parkingBoyRepository.save(parkingBoy) != null){
            return buildCreateResponse(parkingBoy);
        }
            return buildCreateFailResponse();
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
