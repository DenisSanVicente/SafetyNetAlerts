package com.safetynetalerts.SafetyNetAlerts.service;

import com.safetynetalerts.SafetyNetAlerts.DTO.FireDTO;
import com.safetynetalerts.SafetyNetAlerts.DTO.FireStationCoverageDTO;
import com.safetynetalerts.SafetyNetAlerts.model.FireStation;
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

// Activer Mockito
@ExtendWith(MockitoExtension.class)
class FireStationServiceImplTest {

    @Mock
    private DataRepository dataRepository;

    @Mock
    private PersonService personService;

    @Mock
    private MedicalRecordService medicalRecordService;

    @InjectMocks
    private FireStationServiceImpl service;


    @Test
    void getFireStationsByStationNumberTest() {

        // GIVEN
        SafetyNetData data = new SafetyNetData();

        // Création des firestations
        FireStation fs1 = new FireStation();
        fs1.setAddress("644 Gershwin Cir");
        fs1.setStation(1);

        FireStation fs2 = new FireStation();
        fs2.setAddress("892 Downing Ct");
        fs2.setStation(2);

        FireStation fs3 = new FireStation();
        fs3.setAddress("1509 Culver St");
        fs3.setStation(3);

        // On ajoute ces fs à la BDD
        data.setFirestations(List.of(fs1, fs2, fs3));

        // On mocke getData
        when(dataRepository.getData()).thenReturn(data);

        // WHEN
        // Appel de la méthode
        List<FireStation> result = service.getFireStationsByStationNumber(3); // On appelle la station n°3

        // THEN
        assertEquals(1, result.size()); // On vérifie qu'une seule fs est bien appellée
        assertEquals("1509 Culver St", result.get(0).getAddress()); // On vérifie que l'adresse de la fs appellée correspond à assert
    }

    @Test
    void getStationNumberByAddressTest() {

        // GIVEN
        SafetyNetData data = new SafetyNetData();

        // Création des firestations
        FireStation fs1 = new FireStation();
        fs1.setStation(1);
        fs1.setAddress("644 Gershwin Cir");

        // On ajoute la fs à data
        data.setFirestations(List.of(fs1));

        when(dataRepository.getData()).thenReturn(data);

        // WHEN
        int result = service.getStationNumberByAddress("644 Gershwin Cir");

        // THEN
        assertEquals(1, result);
    }


    /// ===== CAS NEGATIFS ===== //
    // Adresse introuvée
    @Test
    void shouldReturnZeroWhenAddressNotFoundTest() {

        // GIVEN
        SafetyNetData data = new SafetyNetData();

        FireStation fs1 = new FireStation();
        fs1.setStation(1);
        fs1.setAddress("1509 Culver St");

        data.setFirestations(List.of(fs1));

        when(dataRepository.getData()).thenReturn(data);

        // WHEN
        int result = service.getStationNumberByAddress("644 Gershwin Cir");

        // THEN
        assertEquals(0, result);
    }

    // Numéro de station introuvé
    @Test
    void shouldReturnZeroWhenStationNumberNotFoundTest() {

        // GIVEN
        SafetyNetData data = new SafetyNetData();

        FireStation fs1 = new FireStation();
        fs1.setAddress("644 Gershwin Cir");
        fs1.setStation(1);

        FireStation fs2 = new FireStation();
        fs2.setAddress("1509 Culver St");
        fs2.setStation(2);

        data.setFirestations(List.of(fs1, fs2));

        when(dataRepository.getData()).thenReturn(data);

        // WHEN
        List<FireStation> result = service.getFireStationsByStationNumber(5);

        // THEN
        assertTrue(result.isEmpty());
    }


    /// ===== CRUD ===== ///
    @Test
    void addFireStationTest() {

        // GIVEN
        FireStation fs = new FireStation();
        fs.setStation(1);
        fs.setAddress("1509 Culver St");

        SafetyNetData data = new SafetyNetData();
        data.setFirestations(new ArrayList<>());

        when(dataRepository.getData()).thenReturn(data);

        // WHEN
        FireStation result = service.addFireStation(fs);

        // THEN
        assertEquals(1, data.getFirestations().size());
        assertEquals("1509 Culver St", result.getAddress());
        assertEquals(1, result.getStation());
    }

    @Test
    void updateFireStationTest() {

        // GIVEN
        FireStation existingFs1 = new FireStation();
        existingFs1.setAddress("1509 Culver St");
        existingFs1.setStation(1);

        FireStation existingFs2 = new FireStation();
        existingFs2.setAddress("644 Gershwin Cir");
        existingFs2.setStation(2);

        SafetyNetData data = new SafetyNetData();
        data.setFirestations(new ArrayList<>(List.of(existingFs1, existingFs2)));

        when(dataRepository.getData()).thenReturn(data);

        // Objet contenant les nouvelles valeurs à appliquer
        FireStation fireStationToUpdate = new FireStation();
        fireStationToUpdate.setAddress("1509 Culver St");
        fireStationToUpdate.setStation(3);

        // WHEN
        FireStation result = service.updateFireStation(fireStationToUpdate);

        // THEN

        // Vérifie l'objet retourné
        assertNotNull(result);
        assertEquals("1509 Culver St", result.getAddress());
        assertEquals(3, result.getStation());

        // Vérifie que la firestation existante a bien été mise à jour
        FireStation updatedFireStation = data.getFirestations().get(0);

        assertEquals("1509 Culver St", updatedFireStation.getAddress());
        assertEquals(3, updatedFireStation.getStation());

        // Vérifie que l'autre firestation n'a pas été modifiée
        FireStation untouchedFireStation = data.getFirestations().get(1);

        assertEquals("644 Gershwin Cir", untouchedFireStation.getAddress());
        assertEquals(2, untouchedFireStation.getStation());
    }

    @Test
    void deleteFireStationTest() {

        // GIVEN
        FireStation fs1 = new FireStation();
        fs1.setStation(1);
        fs1.setAddress("1509 Culver St");

        FireStation fs2 = new FireStation();
        fs2.setStation(2);
        fs2.setAddress("644 Gershwin Cir");

        SafetyNetData data = new SafetyNetData();
        data.setFirestations(new ArrayList<>(List.of(fs1, fs2)));

        when(dataRepository.getData()).thenReturn(data);

        // WHEN
        boolean result = service.deleteFireStation("1509 Culver St");

        // THEN
        assertAll(
                () -> assertTrue(result), // On vérifie que la méthode a bien trouvé et supprimé une firestation (boolean true = suppression réussie)
                () -> assertEquals(1, data.getFirestations().size()), // On vérifie qu'il ne reste qu'une seule firestation au lieu de 2
                () -> assertEquals("644 Gershwin Cir", data.getFirestations().get(0).getAddress()) // On vérifie que c'est bien "1509 Culver St qui a été supprimé
        );
    }

    @Test
    void getPersonsCoveredByStationTest() {

        // GIVEN
        FireStation fs = new FireStation();
        fs.setStation(1);
        fs.setAddress("1509 Culver St");

        SafetyNetData data = new SafetyNetData();
        data.setFirestations(new ArrayList<>(List.of(fs)));

        // Création de la personne
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("1509 Culver St");
        person.setPhone("123-456");

        data.setPersons(new ArrayList<>(List.of(person)));

        when(dataRepository.getData()).thenReturn(data);

        // Mock de MedicalRecord
        MedicalRecord mr = new MedicalRecord();
        mr.setBirthdate("01/01/2010"); // Changer la date pour simuler un adulte

        when(medicalRecordService.getMedicalRecordByPerson(person)).thenReturn(mr);

        // WHEN
        FireStationCoverageDTO result = service.getPersonsCoveredByStation(1);

        // THEN
        assertAll(
                () -> assertEquals("John", result.getPersons().get(0).getFirstName()),
                () -> assertEquals("Doe", result.getPersons().get(0).getLastName()),
                () -> assertEquals("1509 Culver St", result.getPersons().get(0).getAddress()),
                () -> assertEquals("123-456", result.getPersons().get(0).getPhone()),
                () -> assertEquals(0, result.getAdultCount()),
                () -> assertEquals(1, result.getChildCount())
                );
    }

    @Test
    void getFireInfoByAddressTest() {

        // GIVEN
        String address = "1509 Culver St";

        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setPhone("123-456");

        when(personService.getPersonsByAddress(address)).thenReturn(List.of(person));

        MedicalRecord mr = new MedicalRecord();
        mr.setBirthdate("01/01/2010");

        when(medicalRecordService.getMedicalRecordByPerson(person)).thenReturn(mr);

        FireStation fs = new FireStation();
        fs.setStation(1);
        fs.setAddress(address);

        SafetyNetData data = new SafetyNetData();
        data.setFirestations(List.of(fs));

        when(dataRepository.getData()).thenReturn(data);

        // WHEN
        FireDTO result = service.getFireInfoByAddress(address);

        // THEN
        assertEquals("John", result.getResidents().get(0).getFirstName());
        assertEquals("Doe", result.getResidents().get(0).getLastName());
        assertEquals("123-456", result.getResidents().get(0).getPhoneNumber());
    }



    /// METHODES A TESTER DANS L'ORDRE
    /// ===== CRUD ===== ///
    // addFireStation
    // updateFireStation
    // deleteFireStation

    /// ===== ENDPOINT PRINCIPAL ///
    // getPersonsCoveredByStation(int stationNumber)

    /// ===== ENDPOINT FIRE ===== ///
    // getFireInfoByAddress(String address)

}