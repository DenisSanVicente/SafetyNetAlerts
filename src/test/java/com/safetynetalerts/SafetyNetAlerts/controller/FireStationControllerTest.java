package com.safetynetalerts.SafetyNetAlerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynetalerts.SafetyNetAlerts.DTO.FireDTO;
import com.safetynetalerts.SafetyNetAlerts.DTO.FireStationCoverageDTO;
import com.safetynetalerts.SafetyNetAlerts.model.FireStation;
import com.safetynetalerts.SafetyNetAlerts.service.FireStationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FireStationController.class)
class FireStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FireStationService fireStationService;

    // 1. GET /firestations
    @Test
    void shouldReturnAllFireStations() throws Exception {

        FireStation fs = new FireStation();
        fs.setAddress("1509 Culver St");
        fs.setStation(1);

        when(fireStationService.getAllFireStations())
                .thenReturn(List.of(fs));

        mockMvc.perform(get("/firestations"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].address").value("1509 Culver St"))
                .andExpect(jsonPath("$[0].station").value(1));

        verify(fireStationService).getAllFireStations();
    }

    // 2. GET /firestation?stationNumber=
    @Test
    void shouldReturnCoverageByStation() throws Exception {

        FireStationCoverageDTO dto = new FireStationCoverageDTO();

        when(fireStationService.getPersonsCoveredByStation(1))
                .thenReturn(dto);

        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(fireStationService).getPersonsCoveredByStation(1);
    }

    // 3. GET /fire?address=
    @Test
    void shouldReturnFireInfoByAddress() throws Exception {

        FireDTO dto = new FireDTO();

        when(fireStationService.getFireInfoByAddress("1509 Culver St"))
                .thenReturn(dto);

        mockMvc.perform(get("/fire")
                        .param("address", "1509 Culver St"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(fireStationService).getFireInfoByAddress("1509 Culver St");
    }

    // 4. POST /firestation
    @Test
    void shouldAddFireStation() throws Exception {

        FireStation fs = new FireStation();
        fs.setAddress("1509 Culver St");
        fs.setStation(1);

        when(fireStationService.addFireStation(any(FireStation.class)))
                .thenReturn(fs);

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fs)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("1509 Culver St"))
                .andExpect(jsonPath("$.station").value(1));

        verify(fireStationService).addFireStation(any(FireStation.class));
    }

    // 5. PUT /firestation
    @Test
    void shouldUpdateFireStation() throws Exception {

        FireStation fs = new FireStation();
        fs.setAddress("1509 Culver St");
        fs.setStation(2);

        when(fireStationService.updateFireStation(any(FireStation.class)))
                .thenReturn(fs);

        mockMvc.perform(put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fs)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.station").value(2));

        verify(fireStationService).updateFireStation(any(FireStation.class));
    }

    // 6. DELETE /firestation
    @Test
    void shouldDeleteFireStation() throws Exception {

        when(fireStationService.deleteFireStation("1509 Culver St"))
                .thenReturn(true);

        mockMvc.perform(delete("/firestation")
                        .param("address", "1509 Culver St"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(fireStationService).deleteFireStation("1509 Culver St");
    }
}