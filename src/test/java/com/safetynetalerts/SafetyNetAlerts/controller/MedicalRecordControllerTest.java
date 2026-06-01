package com.safetynetalerts.SafetyNetAlerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynetalerts.SafetyNetAlerts.model.MedicalRecord;
import com.safetynetalerts.SafetyNetAlerts.service.MedicalRecordService;
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

@WebMvcTest(MedicalRecordController.class)
class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MedicalRecordService medicalRecordService;

    // 1. GET /medicalRecord
    @Test
    void shouldReturnAllMedicalRecords() throws Exception {

        MedicalRecord mr = new MedicalRecord();
        mr.setFirstName("John");
        mr.setLastName("Doe");
        mr.setBirthdate("01/01/2010");
        mr.setMedications(List.of("med1"));
        mr.setAllergies(List.of("peanut"));

        when(medicalRecordService.getAllMedicalRecords())
                .thenReturn(List.of(mr));

        mockMvc.perform(get("/medicalRecord"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].birthdate").value("01/01/2010"))
                .andExpect(jsonPath("$[0].medications[0]").value("med1"))
                .andExpect(jsonPath("$[0].allergies[0]").value("peanut"));

        verify(medicalRecordService).getAllMedicalRecords();
    }

    // 2. POST /medicalRecord
    @Test
    void shouldAddMedicalRecord() throws Exception {

        MedicalRecord mr = new MedicalRecord();
        mr.setFirstName("John");
        mr.setLastName("Doe");
        mr.setBirthdate("01/01/2010");
        mr.setMedications(List.of());
        mr.setAllergies(List.of());

        when(medicalRecordService.addMedicalRecord(any(MedicalRecord.class)))
                .thenReturn(mr);

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mr)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(medicalRecordService).addMedicalRecord(any(MedicalRecord.class));
    }

    // 3. PUT /medicalRecord
    @Test
    void shouldUpdateMedicalRecord() throws Exception {

        MedicalRecord mr = new MedicalRecord();
        mr.setFirstName("John");
        mr.setLastName("Doe");
        mr.setBirthdate("01/01/2011");

        when(medicalRecordService.updateMedicalRecord(any(MedicalRecord.class)))
                .thenReturn(mr);

        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mr)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.birthdate").value("01/01/2011"));

        verify(medicalRecordService).updateMedicalRecord(any(MedicalRecord.class));
    }

    // 4. DELETE /medicalRecord
    @Test
    void shouldDeleteMedicalRecord() throws Exception {

        when(medicalRecordService.deleteMedicalRecord("John", "Doe"))
                .thenReturn(true);

        mockMvc.perform(delete("/medicalRecord")
                        .param("firstName", "John")
                        .param("lastName", "Doe"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(medicalRecordService)
                .deleteMedicalRecord("John", "Doe");
    }
}