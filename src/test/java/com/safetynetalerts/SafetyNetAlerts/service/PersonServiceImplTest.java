package com.safetynetalerts.SafetyNetAlerts.service;

import com.safetynetalerts.SafetyNetAlerts.DTO.ChildAlertDTO;
import com.safetynetalerts.SafetyNetAlerts.DTO.HouseholdMemberDTO;
import com.safetynetalerts.SafetyNetAlerts.DTO.PersonInfoDTO;
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
public class PersonServiceImplTest {

    @Mock
    private DataRepository dataRepository;

    @InjectMocks
    private PersonServiceImpl personService;

    /// 1. ENDPOINT METIERS
    // getChildrenByAddress
    // getPersonInfoByLastName
    // getEmailsByCity

    /// 2. CRUD mais avec des tests simples
    // addPerson
    // updatePerson
    // deletePerson

    /// 3. QUERIES SIMPLES
    // getAllPersons
    // getPersonsByAddress

    /// ===== HELPERS ===== ///


    /// ===== QUERIES ===== ///

    @Test
    void getAllPersonsTest() {

        // GIVEN
        SafetyNetData data = new SafetyNetData();

        Person ps1 = new Person();
        ps1.setFirstName("John");
        ps1.setLastName("Doe");

        Person ps2 = new Person();
        ps2.setFirstName("Denis");
        ps2.setLastName("SV");

        // on injecte la liste dans l'objet data
        data.setPersons(List.of(ps1, ps2));

        // mock du repository
        when(dataRepository.getData()).thenReturn(data);

        // WHEN
        List<Person> result = personService.getAllPersons();

        // THEN
        assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.stream()
                        .anyMatch(p -> p.getFirstName().equals("John")
                                && p.getLastName().equals("Doe")))
        );
    }


    @Test
    void getPersonsByAddress() {

        // GIVEN
        String address = "1509 Culver St";

        Person ps1 = new Person();
        ps1.setFirstName("John");
        ps1.setLastName("Doe");
        ps1.setAddress(address);

        Person ps2 = new Person();
        ps2.setFirstName("Phil");
        ps2.setLastName("Doe");
        ps2.setAddress(address);

        Person ps3 = new Person();
        ps3.setFirstName("Denis");
        ps3.setLastName("SV");
        ps3.setAddress("644 Gershwin Cir");

        SafetyNetData data = new SafetyNetData();
        data.setPersons(List.of(ps1, ps2, ps3));

        when(dataRepository.getData()).thenReturn(data);

        // WHEN
        List<Person> result = personService.getPersonsByAddress(address);

        // THEN
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.stream()
                        .allMatch(p -> p.getAddress().equals(address))),
                () -> assertTrue(result.stream()
                        .anyMatch(p -> p.getFirstName().equals("John"))),
                () -> assertTrue(result.stream()
                        .anyMatch(p -> p.getLastName().equals("Doe")))
        );
    }


    /// ===== ENDPOINTS METIER ===== ///
    @Test
    void getChildrenByAddressTest() {

        // GIVEN
        String address = "1509 Culver St";

        Person child = new Person();
        child.setFirstName("John");
        child.setLastName("Doe");
        child.setAddress(address);

        Person adult = new Person();
        adult.setFirstName("Jane");
        adult.setLastName("Doe");
        adult.setAddress(address);

        MedicalRecord childRecord = new MedicalRecord();
        childRecord.setFirstName("John");
        childRecord.setLastName("Doe");
        childRecord.setBirthdate("01/01/2010");

        MedicalRecord adultRecord = new MedicalRecord();
        adultRecord.setFirstName("Jane");
        adultRecord.setLastName("Doe");
        adultRecord.setBirthdate("01/01/1980");

        SafetyNetData data = new SafetyNetData();
        data.setPersons(List.of(child, adult));
        data.setMedicalrecords(List.of(childRecord, adultRecord));

        when(dataRepository.getData()).thenReturn(data);

        // WHEN
        List<ChildAlertDTO> result =
                personService.getChildrenByAddress(address);

        ChildAlertDTO childResult = result.get(0);

        HouseholdMemberDTO householdMember =
                childResult.getHouseholdMembers().get(0);

        // THEN
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("John", childResult.getFirstName()),
                () -> assertEquals("Doe", childResult.getLastName()),
                () -> assertTrue(childResult.getAge() <= 18),
                () -> assertEquals(1, childResult.getHouseholdMembers().size()),
                () -> assertEquals("Jane", householdMember.getFirstName()),
                () -> assertEquals("Doe", householdMember.getLastName())
        );
    }


    @Test
    void getPersonInfoByLastNameTest() {

        // GIVEN
        String lastName = "Doe";

        Person ps1 = new Person();
        ps1.setFirstName("John");
        ps1.setLastName(lastName);
        ps1.setAddress("1509 Culver St");
        ps1.setPhone("123-456");

        Person ps2 = new Person();
        ps2.setFirstName("Jane");
        ps2.setLastName(lastName);
        ps2.setAddress("1509 Culver St");
        ps2.setPhone("456-789");

        MedicalRecord record1 = new MedicalRecord();
        record1.setFirstName("John");
        record1.setLastName(lastName);
        record1.setBirthdate("01/01/1990");

        MedicalRecord record2 = new MedicalRecord();
        record2.setFirstName("Jane");
        record2.setLastName(lastName);
        record2.setBirthdate("01/01/1985");

        SafetyNetData data = new SafetyNetData();
        data.setPersons(List.of(ps1, ps2));
        data.setMedicalrecords(List.of(record1, record2));

        when(dataRepository.getData()).thenReturn(data);

        // WHEN
        List<PersonInfoDTO> result =
                personService.getPersonInfoByLastName(lastName);

        // THEN
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.stream()
                        .anyMatch(p -> p.getFirstName().equals("John"))),
                () -> assertTrue(result.stream()
                        .anyMatch(p -> p.getFirstName().equals("Jane")))
        );
    }


    @Test
    void getEmailsByCityTest() {

        // GIVEN
        String city = "Lille";

        Person ps1 = new Person();
        ps1.setLastName("Doe");
        ps1.setCity("Lille");
        ps1.setEmail("johndoe@mail.com");

        Person ps2 = new Person();
        ps2.setLastName("Doe");
        ps2.setCity("Paris");
        ps2.setEmail("janedoe@mail.com");

        SafetyNetData data = new SafetyNetData();
        data.setPersons(List.of(ps1, ps2));

        when(dataRepository.getData()).thenReturn(data);

        // WHEN
        List<String> result = personService.getEmailsByCity(city);

        // THEN
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("johndoe@mail.com", result.get(0))
        );
    }


    /// ===== CRUD ===== ///
    @Test
    void addPersonTest() {

        // GIVEN
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");

        SafetyNetData data = new SafetyNetData();
        data.setPersons(new ArrayList<>());

        when(dataRepository.getData()).thenReturn(data);

        // WHEN
        personService.addPerson(person);

        // THEN
        assertAll(
                () -> assertEquals(1, data.getPersons().size()),
                () -> assertTrue(data.getPersons().stream()
                        .anyMatch(p -> p.getFirstName().equals("John")
                                && p.getLastName().equals("Doe")))
        );
    }


    @Test
    void updatePersonTest() {

        // GIVEN
        Person ps = new Person();
        ps.setFirstName("John");
        ps.setLastName("Doe");
        ps.setAddress("1509 Culver St");
        ps.setCity("Lille");
        ps.setZip("59000");
        ps.setPhone("123-456");
        ps.setEmail("johndoe@mail.com");

        Person updated = new Person();
        updated.setFirstName("John");
        updated.setLastName("Doe");
        updated.setAddress("644 Gershwin Cir");
        updated.setCity("Paris");
        updated.setZip("95000");
        updated.setPhone("456-789");
        updated.setEmail("johndoe@mail.com");

        SafetyNetData data = new SafetyNetData();
        data.setPersons(new ArrayList<>(List.of(ps)));

        when(dataRepository.getData()).thenReturn(data);

        // WHEN
        Person result = personService.updatePerson(updated);

        // THEN
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("John", result.getFirstName()),
                () -> assertEquals("Doe", result.getLastName()),
                () -> assertEquals("644 Gershwin Cir", result.getAddress()),
                () -> assertEquals("Paris", result.getCity()),
                () -> assertEquals("95000", result.getZip()),
                () -> assertEquals("456-789", result.getPhone()),
                () -> assertEquals("johndoe@mail.com", result.getEmail())
        );
    }

    @Test
    void deletePersonTest() {

        // Paramètre String firstName
        // Paramètre String lastName
        // List allPersons
        // Iterator allPersons

        // GIVEN
        String firstName = "John";
        String lastName = "Doe";

        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");

        SafetyNetData data = new SafetyNetData();
        data.setPersons(new ArrayList<>(List.of(person)));

        when(dataRepository.getData()).thenReturn(data);

        // WHEN
        boolean result = personService.deletePerson(firstName, lastName);

        // THEN
        assertAll(
                () -> assertTrue(result),
                () -> assertEquals(0, data.getPersons().size())
        );
    }
}
