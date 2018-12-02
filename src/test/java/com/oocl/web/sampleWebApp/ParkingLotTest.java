package com.oocl.web.sampleWebApp;

import com.oocl.web.sampleWebApp.domain.ParkingLot;
import com.oocl.web.sampleWebApp.domain.ParkingLotRepository;
import com.oocl.web.sampleWebApp.models.ParkingLotResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.oocl.web.sampleWebApp.WebTestUtil.getContentAsObject;
import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ParkingLotTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Test
    public void should_get_parking_lots() throws Exception {
        // Given a new parking lot
        final ParkingLot parkinglot = parkingLotRepository.save(new ParkingLot("Jason", 30));

        // When GET to /parkinglots
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/parkinglots"))
                .andReturn();

        // Then it should return list of parkinglots
        assertEquals(200, result.getResponse().getStatus());

        final ParkingLotResponse[] parkingLots = getContentAsObject(result, ParkingLotResponse[].class);

        assertEquals(1, parkingLots.length);
        assertEquals("Jason", parkingLots[0].getparkingLotId());
        assertEquals(30, parkingLots[0].getCapacity());
    }
}
