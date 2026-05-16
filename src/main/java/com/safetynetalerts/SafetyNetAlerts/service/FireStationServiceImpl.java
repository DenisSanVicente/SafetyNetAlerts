package com.safetynetalerts.SafetyNetAlerts.service;

import com.safetynetalerts.SafetyNetAlerts.DTO.FireDTO;
import com.safetynetalerts.SafetyNetAlerts.DTO.FirePersonDTO;
import com.safetynetalerts.SafetyNetAlerts.DTO.FireStationCoverageDTO;
import com.safetynetalerts.SafetyNetAlerts.DTO.PersonCoveredDTO;
import com.safetynetalerts.SafetyNetAlerts.Util.AgeCalculator;
import com.safetynetalerts.SafetyNetAlerts.model.FireStation;
import com.safetynetalerts.SafetyNetAlerts.model.MedicalRecord;
import com.safetynetalerts.SafetyNetAlerts.model.Person;
import com.safetynetalerts.SafetyNetAlerts.repository.DataRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class FireStationServiceImpl implements FireStationService {

    private static final Logger log = LogManager.getLogger(FireStationServiceImpl.class);

    private final DataRepository dataRepository;

    public FireStationServiceImpl(DataRepository dataRepository) {

        this.dataRepository = dataRepository;
    }

    @Override
    public List<FireStation> getAllFireStations() {
        log.info("getAllFireStations OK");
        return dataRepository.getData().getFirestations();
    }

    /// ===== ETAPE 3 ===== ///
    // Créer la liste des casernes triées par numéro
    @Override
    public List<FireStation> getFireStationsByStationNumber(int stationNumber) {
        List<FireStation> allStations = getAllFireStations();
        List<FireStation> stationsFilteredByNumber = new ArrayList<>();
        for (FireStation fs : allStations) {
            if (fs.getStation() == stationNumber) {
                stationsFilteredByNumber.add(fs);
            }
        }
        log.info("getFireStationsByStationNumber OK");
        return stationsFilteredByNumber;
    }

    /// Récupération des adresses des stations en fonction de leurs numéros

    private List<String> getFireStationAdress(int stationNumber) {
        List<String> fireStationsAddresses = new ArrayList<>();
        for (FireStation fs : getFireStationsByStationNumber(stationNumber)) {
            fireStationsAddresses.add(fs.getAddress());
        }
        log.info("getFireStationAddress OK");
        return fireStationsAddresses;
    }

    ///  Récupération des personnes associées à l'adresse de la station
    @Override
    public FireStationCoverageDTO getPersonsCoveredByStation(int stationNumber) {

        List<String> addresses = getFireStationAddresses(stationNumber);
        List<Person> allPersons = dataRepository.getData().getPersons();
        List<MedicalRecord> medicalRecords = dataRepository.getData().getMedicalrecords();

        List<PersonCoveredDTO> personsCovered = new ArrayList<>();

        int adultCount = 0;
        int childCount = 0;

        for (Person ps : allPersons) {

            if (addresses.contains(ps.getAddress())) {

                // DTO personne
                PersonCoveredDTO dto = new PersonCoveredDTO();
                dto.setFirstName(ps.getFirstName());
                dto.setLastName(ps.getLastName());
                dto.setAddress(ps.getAddress());
                dto.setPhone(ps.getPhone());

                personsCovered.add(dto);

                // recherche medical record
                for (MedicalRecord mr : medicalRecords) {

                    if (mr.getFirstName().equals(ps.getFirstName())
                            && mr.getLastName().equals(ps.getLastName())) {

                        int age = AgeCalculator.calculateAge(mr.getBirthdate());

                        if (age <= 18) {
                            childCount++;
                        } else {
                            adultCount++;
                        }

                        break;
                    }
                }
            }
        }

        log.info("getPersonsCoveredByStation OK");
        return new FireStationCoverageDTO(personsCovered, adultCount, childCount);
    }


    private List<String> getFireStationAddresses(int stationNumber) {

        List<String> addresses = new ArrayList<>();

        for (FireStation fs : getFireStationsByStationNumber(stationNumber)) {
            addresses.add(fs.getAddress());
        }

        log.info("getFireStationAddresses OK");
        return addresses;
    }


    /// ===== ETAPE 3 - URL 4 ===== ///
    @Override
    public FireDTO getFireInfoByAddress(String address) {

        // 1. récupérer le numéro de station
        int stationNumber = getStationNumberByAddress(address);

        // 2. récupérer les personnes vivant à cette adresse
        List<Person> personsByAddress = getPersonsByAddress(address);

        // 3. liste des habitants enrichis
        List<FirePersonDTO> residents = new ArrayList<>();

        // 4. boucle sur les personnes
        for (Person person : personsByAddress) {

            // récupérer le medical record
            MedicalRecord mr = getMedicalRecordByPerson(person);

            if (mr != null) {

                // calcul âge
                int age = AgeCalculator.calculateAge(mr.getBirthdate());

                // création DTO
                FirePersonDTO dto = new FirePersonDTO();

                dto.setFirstName(person.getFirstName());
                dto.setLastName(person.getLastName());
                dto.setPhoneNumber(person.getPhone());
                dto.setAge(age);
                dto.setMedications(mr.getMedications());
                dto.setAllergies(mr.getAllergies());

                // ajout à la liste
                residents.add(dto);
            }
        }

        // 5. construction du DTO final
        FireDTO fireDTO = new FireDTO();
        fireDTO.setStationNumber(stationNumber);
        fireDTO.setResidents(residents);

        log.info("getFireInfoByAddress OK");
        return fireDTO;
    }

    // Endpoint 4 - Récupérer numéro de station par l'adresse

    /**
     *
     * @param address
     * @return
     */
    @Override
    public int getStationNumberByAddress(String address) {
        List<FireStation> firestations = dataRepository.getData().getFirestations();

        for (FireStation fs : firestations) {
            if (fs.getAddress().equals(address)) {
                return fs.getStation();
            }
        }

        log.info("getStationNumberByAddress OK");
        return -1;
    }

    // Récupérer les personnes vivant à cette adresse
    public List<Person> getPersonsByAddress(String address) {

        List<Person> allPersons = dataRepository.getData().getPersons();

        List<Person> personsByAddress = new ArrayList<>();

        for (Person person : allPersons) {

            if (person.getAddress().equals(address)) {
                personsByAddress.add(person);
            }
        }

        log.info("getPersonsByAddress OK");
        return personsByAddress;
    }

    // Récupérer les medical records par personne
    private MedicalRecord getMedicalRecordByPerson(Person person) {

        List<MedicalRecord> allMedicalRecords =
                dataRepository.getData().getMedicalrecords();

        for (MedicalRecord mr : allMedicalRecords) {

            if (mr.getFirstName().equals(person.getFirstName())
                    && mr.getLastName().equals(person.getLastName())) {

                return mr;
            }
        }

        log.info("getMedicalRecordsByPerson OK");
        return null;
    }

    /// ===== ETAPE 3 - URL 5 ===== ///
    /// Récupérer les adresses des firestations par le numéro des stations
    private List<String> getAddressesByStations(List<Integer> stations) {
        List<String> addresses = new ArrayList<>();
        List<FireStation> allFireStations = dataRepository.getData().getFirestations();

        for (Integer station : stations) {
            for (FireStation fs : allFireStations) {
                if (fs.getStation() == station) {

                    // Eviter les doublons d'adresse
                    if (!addresses.contains(fs.getAddress())) {
                        addresses.add(fs.getAddress());
                    }
                }
            }
        }

        log.info("getAddressesByStations OK");
        return addresses;
    }
}