package com.oocl.web.sampleWebApp;

import com.oocl.web.sampleWebApp.domain.ParkingBoy;
import com.oocl.web.sampleWebApp.domain.ParkingBoyRepository;
import com.oocl.web.sampleWebApp.domain.ParkingLot;
import com.oocl.web.sampleWebApp.models.ParkingBoyResponse;
import com.oocl.web.sampleWebApp.models.ParkingBoyWithLotResponse;
import com.oocl.web.sampleWebApp.models.ParkingLotResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.List;

import static com.oocl.web.sampleWebApp.WebTestUtil.getContentAsObject;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ParkingBoyTest {

    @Autowired
    private ParkingBoyRepository parkingBoyRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void should_create_parking_boy_with_non_empty_string() throws Exception{
	    //Given a parkingboy {"employeeID":"String"} which employeeID is a non-empty string and unique ID
        String employeeId = "14120";
        String createTestParkingBoyJson = "{\"employeeId\":" + employeeId + "}";

        //When POST to /parkingboys
        mvc.perform(post("/parkingboys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createTestParkingBoyJson)
        )
        //Then it should return 201 Created
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/parkingboys/" + employeeId)));
    }

    @Test
    public void should_not_create_parking_boy_with_input_empty_string() throws Exception{
        //Given a parkingboy {"employeeID":"String"} which employeeID is an empty string
        String createTestParkingBoyJson = "{\"employeeId\":}";

        //When POST to /parkingboys
        mvc.perform(post("/parkingboys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createTestParkingBoyJson)
        )
                //Then it should return 400 Bad Request
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_not_create_parking_boy_with_string_exceeds_length_limit() throws Exception{
        //Given a parkingboy {"employeeID":"String"} which employeeID is a too long string
        String employeeId = "0123456789 0123456789 0123456789 0123456789 0123456789 0123456789 0123456789";
        String createTestParkingBoyJson = "{\"employeeId\":" + employeeId + "}";

        //When POST to /parkingboys
        mvc.perform(post("/parkingboys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createTestParkingBoyJson)
        )
                //Then it should return 400 Bad Request
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_get_parking_boys() throws Exception {
        // Given
        final ParkingBoy boy = parkingBoyRepository.save(new ParkingBoy("boy"));

        // When
        final MvcResult result = mvc.perform(get("/parkingboys"))
                .andReturn();

        // Then
        assertEquals(200, result.getResponse().getStatus());

        final ParkingBoyResponse[] parkingBoys = getContentAsObject(result, ParkingBoyResponse[].class);

        assertEquals(1, parkingBoys.length);
        assertEquals("boy", parkingBoys[0].getEmployeeId());
    }



    @Test
    public void should_get_parking_boy_with_parking_lots() throws Exception {
        // Given a parking boy and two parking lots
        final ParkingBoy parkingBoyOne = new ParkingBoy("parkingBoyOne");
        final ParkingLot parkingLotOne = new ParkingLot("parkingLotOne", 40);
        final ParkingLot parkingLotTwo = new ParkingLot("parkingLotTwo", 10);
        parkingLotOne.setParkingBoy(parkingBoyOne);
        parkingLotTwo.setParkingBoy(parkingBoyOne);
        entityManager.persist(parkingLotOne);
        entityManager.persist(parkingLotTwo);
        entityManager.flush();

        // When POST to /parkingboys/{employeeId}
        final MvcResult result = mvc.perform(get("/parkingboys/parkingBoyOne"))
                .andExpect(status().isOk())
                .andReturn();

        //Then should return info of parking boy of corresponding employeeId
        final ParkingBoyWithLotResponse response = getContentAsObject(
                result, ParkingBoyWithLotResponse.class);
        assertEquals("parkingBoyOne", response.getEmployeeId());
        List<ParkingLotResponse> parkingLots = response.getParkingLots();
        assertEquals(2, parkingLots.size());
        assertTrue(parkingLots.stream().anyMatch(pl -> pl.getParkingLotId().equals("parkingLotOne")));
        assertTrue(parkingLots.stream().anyMatch(pl -> pl.getParkingLotId().equals("parkingLotTwo")));
    }

}
