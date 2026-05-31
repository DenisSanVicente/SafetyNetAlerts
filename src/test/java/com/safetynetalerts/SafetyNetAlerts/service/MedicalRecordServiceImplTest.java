package com.safetynetalerts.SafetyNetAlerts.service;

import com.safetynetalerts.SafetyNetAlerts.model.MedicalRecord;
import com.safetynetalerts.SafetyNetAlerts.model.Person;
import com.safetynetalerts.SafetyNetAlerts.model.SafetyNetData;
import com.safetynetalerts.SafetyNetAlerts.repository.DataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordServiceImplTest {

    @Mock
    private DataRepository dataRepository;

    @Mock
    private PersonService personService;

    @Mock
    private FireStationService fireStationService;

    @InjectMocks
    private MedicalRecordServiceImpl medicalRecordService;


    /// ===== QUERIES ===== ///
    @Test
    void getAllMedicalRecordsTest() {

        // GIVEN
        MedicalRecord mr1 = new MedicalRecord();
        mr1.setFirstName("John");
        mr1.setLastName("Doe");
        mr1.setBirthdate("01/01/2010");
        mr1.setMedications(List.of("med1"));
        mr1.setAllergies(List.of("allergieJohn"));

        MedicalRecord mr2 = new MedicalRecord();
        mr2.setFirstName("Jane");
        mr2.setLastName("Doe");
        mr2.setBirthdate("10/10/2000");
        mr2.setMedications(List.of("med2"));
        mr2.setAllergies(List.of("allergiesJane"));

        SafetyNetData data = new SafetyNetData();
        data.setMedicalrecords(List.of(mr1, mr2));

        when(dataRepository.getData()).thenReturn(data);

        // WHEN
        List<MedicalRecord> result = medicalRecordService.getAllMedicalRecords();

        // THEN
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.stream()
                        .anyMatch(p -> p.getFirstName().equals("John")
                        && p.getLastName().equals("Doe"))),
                () -> assertTrue(result.stream()
                        .anyMatch(p -> p.getFirstName().equals("Jane")
                        && p.getLastName().equals("Doe")))
        );
    }

    @Test
    void getMedicalRecordByPersonTest() {

        // GIVEN
        Person ps = new Person();
        ps.setFirstName("John");
        ps.setLastName("Doe");

        MedicalRecord mr = new MedicalRecord();
        mr.setFirstName("John");
        mr.setLastName("Doe");

        SafetyNetData data = new SafetyNetData();
        data.setMedicalrecords(List.of(mr));

        when(dataRepository.getData()).thenReturn(data);

        // THEN
        MedicalRecord result = medicalRecordService.getMedicalRecordByPerson(ps);

        // WHEN
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("John", result.getFirstName()),
                () -> assertEquals("Doe", result.getLastName())
        );
    }


    /// ===== CRUD ===== ///
    @Test
    void addMedicalRecordTest() {

        // GIVEN
        MedicalRecord mr = new MedicalRecord();
        mr.setFirstName("John");
        mr.setLastName("Doe");
        mr.setAllergies(List.of("allergieJohn"));

        SafetyNetData data = new SafetyNetData();
        data.setMedicalrecords(new ArrayList<>());

        when(dataRepository.getData()).thenReturn(data);

        // WHEN
        medicalRecordService.addMedicalRecord(mr);

        // THEN
        assertAll(
                () -> assertEquals(1, data.getMedicalrecords().size()),
                () -> assertTrue(data.getMedicalrecords().stream()
                        .anyMatch(p -> p.getFirstName().equals("John")
                                && p.getLastName().equals("Doe")))

        );
    }

    @Test
    void updateMedicalRecordTest() {

        // GIVEN
        MedicalRecord mr = new MedicalRecord();
        mr.setFirstName("John");
        mr.setLastName("Doe");
        mr.setAllergies(List.of("allergie1"));

        MedicalRecord updatedMr = new MedicalRecord();
        updatedMr.setFirstName("John");
        updatedMr.setLastName("Doe");
        updatedMr.setAllergies(List.of("allergie2"));

        SafetyNetData data = new SafetyNetData();
        data.setMedicalrecords(new ArrayList<>(List.of(mr)));

        when(dataRepository.getData()).thenReturn(data);

        // WHEN
        MedicalRecord result = medicalRecordService.updateMedicalRecord(updatedMr);

        // THEN
        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.getFirstName().equals("John")
                && result.getLastName().equals("Doe")),
                () -> assertEquals(List.of("allergie2"), result.getAllergies())
        );
    }

    @Test
    void deleteMedicalRecordTest() {

        // GIVEN
        String firstName = "John";
        String lastName = "Doe";

        MedicalRecord mr = new MedicalRecord();
        mr.setFirstName("John");
        mr.setLastName("Doe");
        mr.setBirthdate("01/01/2000");

        SafetyNetData data = new SafetyNetData();
        data.setMedicalrecords(new ArrayList<>(List.of(mr)));

        when(dataRepository.getData()).thenReturn(data);

        // WHEN
        boolean result = medicalRecordService.deleteMedicalRecord(firstName, lastName);

        // THEN
        assertAll(
                () -> assertTrue(result),
                () -> assertEquals(0, data.getMedicalrecords().size())
        );
    }


}
