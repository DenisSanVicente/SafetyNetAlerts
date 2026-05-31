package com.safetynetalerts.SafetyNetAlerts.integration;

import com.safetynetalerts.SafetyNetAlerts.DTO.PhoneAlertDTO;
import com.safetynetalerts.SafetyNetAlerts.model.FireStation;
import com.safetynetalerts.SafetyNetAlerts.model.Person;
import com.safetynetalerts.SafetyNetAlerts.model.SafetyNetData;
import com.safetynetalerts.SafetyNetAlerts.repository.DataRepository;
import com.safetynetalerts.SafetyNetAlerts.service.FireStationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class PhoneAlertIT {

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

        Person p1 = new Person();
        p1.setFirstName("John");
        p1.setLastName("Doe");
        p1.setAddress("1509 Culver St");
        p1.setPhone("111-111");

        Person p2 = new Person();
        p2.setFirstName("Jane");
        p2.setLastName("Smith");
        p2.setAddress("1509 Culver St");
        p2.setPhone("222-222");

        data.setFirestations(List.of(fs));
        data.setPersons(List.of(p1, p2));
        data.setMedicalrecords(List.of());

        dataRepository.setData(data);
    }

    @Test
    void shouldReturnPhoneNumbersByStation() {

        PhoneAlertDTO result = service.getPhonesByStation(1);

        assertAll(
                () -> assertEquals(2, result.getPhones().size()),
                () -> assertTrue(result.getPhones().contains("111-111")),
                () -> assertTrue(result.getPhones().contains("222-222"))
        );
    }
}
