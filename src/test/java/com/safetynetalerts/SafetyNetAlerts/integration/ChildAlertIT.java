package com.safetynetalerts.SafetyNetAlerts.integration;

import com.safetynetalerts.SafetyNetAlerts.DTO.ChildAlertDTO;
import com.safetynetalerts.SafetyNetAlerts.model.MedicalRecord;
import com.safetynetalerts.SafetyNetAlerts.model.Person;
import com.safetynetalerts.SafetyNetAlerts.model.SafetyNetData;
import com.safetynetalerts.SafetyNetAlerts.repository.DataRepository;
import com.safetynetalerts.SafetyNetAlerts.service.PersonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ChildAlertIT {

    @Autowired
    private PersonServiceImpl personService;

    @Autowired
    private DataRepository dataRepository;

    @BeforeEach
    void setup() {

        SafetyNetData data = new SafetyNetData();

        Person child = new Person();
        child.setFirstName("John");
        child.setLastName("Doe");
        child.setAddress("1509 Culver St");

        Person adult = new Person();
        adult.setFirstName("Jane");
        adult.setLastName("Doe");
        adult.setAddress("1509 Culver St");

        MedicalRecord childMr = new MedicalRecord();
        childMr.setFirstName("John");
        childMr.setLastName("Doe");
        childMr.setBirthdate("01/01/2015");

        MedicalRecord adultMr = new MedicalRecord();
        adultMr.setFirstName("Jane");
        adultMr.setLastName("Doe");
        adultMr.setBirthdate("01/01/1980");

        data.setPersons(List.of(child, adult));
        data.setMedicalrecords(List.of(childMr, adultMr));
        data.setFirestations(List.of());

        dataRepository.setData(data);
    }

    @Test
    void shouldReturnChildWithHouseholdMembers() {

        List<ChildAlertDTO> result =
                personService.getChildrenByAddress("1509 Culver St");

        assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertEquals("John", result.get(0).getFirstName()),
                () -> assertEquals(1, result.get(0).getHouseholdMembers().size())
        );
    }
}
