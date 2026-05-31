package com.safetynetalerts.SafetyNetAlerts.integration;

import com.safetynetalerts.SafetyNetAlerts.DTO.FireStationCoverageDTO;
import com.safetynetalerts.SafetyNetAlerts.model.FireStation;
import com.safetynetalerts.SafetyNetAlerts.model.MedicalRecord;
import com.safetynetalerts.SafetyNetAlerts.model.Person;
import com.safetynetalerts.SafetyNetAlerts.model.SafetyNetData;
import com.safetynetalerts.SafetyNetAlerts.repository.DataRepository;
import com.safetynetalerts.SafetyNetAlerts.service.FireStationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class FireStationCoverageIT {

    @Autowired
    private FireStationServiceImpl service;

    @Autowired
    private DataRepository dataRepository;

    @BeforeEach
    void setup() {

        SafetyNetData data = new SafetyNetData();

        // FireStation
        FireStation fs = new FireStation();
        fs.setStation(1);
        fs.setAddress("1509 Culver St");

        data.setFirestations(List.of(fs));

        // Person
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("1509 Culver St");
        person.setPhone("123-456");
        person.setCity("Culver");

        data.setPersons(List.of(person));

        // Medical Record
        MedicalRecord mr = new MedicalRecord();
        mr.setFirstName("John");
        mr.setLastName("Doe");
        mr.setBirthdate("01/01/2010");
        mr.setMedications(List.of());
        mr.setAllergies(List.of());

        data.setMedicalrecords(List.of(mr));

        dataRepository.setData(data);
    }

    @Test
    void shouldReturnPersonsCoveredByStation() {

        // WHEN
        FireStationCoverageDTO result = service.getPersonsCoveredByStation(1);

        // THEN
        assertAll(
                () -> assertNotNull(result),

                () -> assertEquals(1, result.getPersons().size()),

                () -> assertEquals("John",
                        result.getPersons().get(0).getFirstName()),

                () -> assertEquals("Doe",
                        result.getPersons().get(0).getLastName()),

                () -> assertEquals(0, result.getAdultCount()),
                () -> assertEquals(1, result.getChildCount())
        );
    }
}