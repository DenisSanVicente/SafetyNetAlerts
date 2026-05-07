package com.safetynetalerts.SafetyNetAlerts.service;

import com.safetynetalerts.SafetyNetAlerts.DTO.FireStationCoverageDTO;
import com.safetynetalerts.SafetyNetAlerts.DTO.PersonCoveredDTO;
import com.safetynetalerts.SafetyNetAlerts.Util.AgeCalculator;
import com.safetynetalerts.SafetyNetAlerts.model.FireStation;
import com.safetynetalerts.SafetyNetAlerts.model.MedicalRecord;
import com.safetynetalerts.SafetyNetAlerts.model.Person;
import com.safetynetalerts.SafetyNetAlerts.repository.DataRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class FireStationServiceImpl implements FireStationService {

    private final DataRepository dataRepository;

    public FireStationServiceImpl(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public List<FireStation> getAllFireStations() {
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
        return stationsFilteredByNumber;
    }

    /// Récupération des adresses des stations en fonction de leurs numéros

    private List<String> getFireStationAdress(int stationNumber) {
        List<String> fireStationsAddresses = new ArrayList<>();
        for (FireStation fs : getFireStationsByStationNumber(stationNumber)) {
            fireStationsAddresses.add(fs.getAddress());
        }
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

        return new FireStationCoverageDTO(personsCovered, adultCount, childCount);
    }



    private List<String> getFireStationAddresses(int stationNumber) {

        List<String> addresses = new ArrayList<>();

        for (FireStation fs : getFireStationsByStationNumber(stationNumber)) {
            addresses.add(fs.getAddress());
        }

        return addresses;
    }

}