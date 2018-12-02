package com.oocl.web.sampleWebApp;

import com.oocl.web.sampleWebApp.domain.ParkingLot;
import com.oocl.web.sampleWebApp.domain.ParkingLotRepository;
import com.oocl.web.sampleWebApp.models.ParkingLotResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;

import static com.oocl.web.sampleWebApp.WebTestUtil.getContentAsObject;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional

public class ParkingLotTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Test
    public void should_create_parking_lot_with_non_empty_string_and_valid_capacity_range() throws Exception{
        //Given a parkinglot {"parkingLotID":"String", "capacity": "Integer"}
        //which parkingLotID is a non-empty string and unique ID, capacity is range from 1 - 100
        String parkingLotId = "14120";
        int parkingLotCapacity = 10;
        String createTestParkingLotJson = "{\"parkingLotId\":" + parkingLotId + ", \"capacity\":" + parkingLotCapacity + "}";

        //When POST to /parkinglots
        mvc.perform(post("/parkinglots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createTestParkingLotJson)
        )
                //Then it should return 201 Created
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/parkinglots/" + parkingLotId)));
    }

    @Test
    public void should_not_create_parking_lot_with_empty_string_and_valid_capacity_range() throws Exception{
        //Given a parkinglot {"parkingLotID":"String", "capacity": "Integer"}
        //which parkingLotID is an empty string, capacity is range from 1 - 100
        int parkingLotCapacity = 10;
        String createTestParkingLotJson = "{\"parkingLotId\": , \"capacity\":" + parkingLotCapacity + "}";

        //When POST to /parkinglots
        mvc.perform(post("/parkinglots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createTestParkingLotJson)
        )
                //Then it should return 400 Bad Request
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_not_create_parking_lot_with_too_long_string_and_valid_capacity_range() throws Exception{
        //Given a parkinglot {"parkingLotID":"String", "capacity": "Integer"}
        //which parkingLotID is a too long string, capacity is range from 1 - 100
        String parkingLotId = "0123456789 0123456789 0123456789 0123456789 0123456789 0123456789 0123456789";
        int parkingLotCapacity = 10;
        String createTestParkingLotJson = "{\"parkingLotId\":" + parkingLotId + ", \"capacity\":" + parkingLotCapacity + "}";

        //When POST to /parkinglots
        mvc.perform(post("/parkinglots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createTestParkingLotJson)
        )
                //Then it should return 400 Bad Request
                .andExpect(status().isBadRequest());
    }

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
