package com.safetynetalerts.SafetyNetAlerts.integration;

import com.safetynetalerts.SafetyNetAlerts.DTO.FireDTO;
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

@SpringBootTest
class FireInfoIT {

    @Autowired
    private FireStationServiceImpl service;

    @Autowired
    private DataRepository dataRepository;

    @BeforeEach
    void setup() {

        SafetyNetData data = new SafetyNetData();

        FireStation fs = new FireStation();
        fs.setStation(1);
        fs.setAddress("1509 Culver St");

        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("1509 Culver St");
        person.setPhone("123-456");

        MedicalRecord mr = new MedicalRecord();
        mr.setFirstName("John");
        mr.setLastName("Doe");
        mr.setBirthdate("01/01/2010");
        mr.setMedications(List.of("med1"));
        mr.setAllergies(List.of("allergy1"));

        data.setFirestations(List.of(fs));
        data.setPersons(List.of(person));
        data.setMedicalrecords(List.of(mr));

        dataRepository.setData(data);
    }

    @Test
    void shouldReturnFireInfoByAddress() {

        FireDTO result = service.getFireInfoByAddress("1509 Culver St");

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.getStationNumber()),
                () -> assertEquals(1, result.getResidents().size()),

                () -> assertEquals("John",
                        result.getResidents().get(0).getFirstName()),

                () -> assertEquals(10,
                        result.getResidents().get(0).getAge()),

                () -> assertEquals(1,
                        result.getResidents().get(0).getMedications().size())
        );
    }
}
