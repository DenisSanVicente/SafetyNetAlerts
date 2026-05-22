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
        return dataRepository.getData().getFirestations();
    }

    @Override
    public List<FireStation> getFireStationsByStationNumber(int stationNumber) {

        List<FireStation> result = new ArrayList<>();

        for (FireStation fs : getAllFireStations()) {
            if (fs.getStation() == stationNumber) {
                result.add(fs);
            }
        }

        return result;
    }

    // ===================== HELPERS =====================

    private List<String> getFireStationAddresses(int stationNumber) {

        List<String> addresses = new ArrayList<>();

        for (FireStation fs : getFireStationsByStationNumber(stationNumber)) {
            if (!addresses.contains(fs.getAddress())) {
                addresses.add(fs.getAddress());
            }
        }

        return addresses;
    }

    private MedicalRecord getMedicalRecordByPerson(Person person) {

        for (MedicalRecord mr : dataRepository.getData().getMedicalrecords()) {
            if (mr.getFirstName().equals(person.getFirstName())
                    && mr.getLastName().equals(person.getLastName())) {
                return mr;
            }
        }

        return null;
    }

    public List<Person> getPersonsByAddress(String address) {

        List<Person> result = new ArrayList<>();

        for (Person person : dataRepository.getData().getPersons()) {
            if (person.getAddress().equals(address)) {
                result.add(person);
            }
        }

        return result;
    }

    @Override
    public int getStationNumberByAddress(String address) {

        for (FireStation fs : dataRepository.getData().getFirestations()) {
            if (fs.getAddress().equals(address)) {
                return fs.getStation();
            }
        }

        return 0;
    }

    // ===================== ENDPOINTS =====================

    @Override
    public FireStationCoverageDTO getPersonsCoveredByStation(int stationNumber) {

        List<String> addresses = getFireStationAddresses(stationNumber);
        List<PersonCoveredDTO> personsCovered = new ArrayList<>();

        int adultCount = 0;
        int childCount = 0;

        for (Person ps : dataRepository.getData().getPersons()) {

            if (addresses.contains(ps.getAddress())) {

                PersonCoveredDTO dto = new PersonCoveredDTO();
                dto.setFirstName(ps.getFirstName());
                dto.setLastName(ps.getLastName());
                dto.setAddress(ps.getAddress());
                dto.setPhone(ps.getPhone());

                personsCovered.add(dto);

                MedicalRecord mr = getMedicalRecordByPerson(ps);

                if (mr != null) {
                    int age = AgeCalculator.calculateAge(mr.getBirthdate());

                    if (age <= 18) {
                        childCount++;
                    } else {
                        adultCount++;
                    }
                }
            }
        }

        return new FireStationCoverageDTO(personsCovered, adultCount, childCount);
    }

    @Override
    public FireDTO getFireInfoByAddress(String address) {

        int stationNumber = getStationNumberByAddress(address);
        List<Person> persons = getPersonsByAddress(address);

        List<FirePersonDTO> residents = new ArrayList<>();

        for (Person person : persons) {

            MedicalRecord mr = getMedicalRecordByPerson(person);

            if (mr != null) {

                FirePersonDTO dto = new FirePersonDTO();
                dto.setFirstName(person.getFirstName());
                dto.setLastName(person.getLastName());
                dto.setPhoneNumber(person.getPhone());
                dto.setAge(AgeCalculator.calculateAge(mr.getBirthdate()));
                dto.setMedications(mr.getMedications());
                dto.setAllergies(mr.getAllergies());

                residents.add(dto);
            }
        }

        FireDTO fireDTO = new FireDTO();
        fireDTO.setStationNumber(stationNumber);
        fireDTO.setResidents(residents);

        return fireDTO;
    }

    // ===================== CRUD =====================

    @Override
    public FireStation addFireStation(FireStation fireStation) {

        dataRepository.getData().getFirestations().add(fireStation);
        return fireStation;
    }

    @Override
    public FireStation updateFireStation(FireStation updatedFireStation) {

        for (FireStation fs : dataRepository.getData().getFirestations()) {
            if (fs.getAddress().equals(updatedFireStation.getAddress())) {
                fs.setStation(updatedFireStation.getStation());
                return fs;
            }
        }

        return null;
    }

    @Override
    public boolean deleteFireStation(String address) {

        FireStation toDelete = null;

        for (FireStation fs : dataRepository.getData().getFirestations()) {
            if (fs.getAddress().equals(address)) {
                toDelete = fs;
                break;
            }
        }

        if (toDelete != null) {
            dataRepository.getData().getFirestations().remove(toDelete);
            return true;
        }

        return false;
    }


    /**
     * Récupère les adresses associées à une station
     */
    @Override
    public  List<String> getAddressesByStation(int stationNumber) {

        List<String> addresses = new ArrayList<>();
        List<FireStation> fireStations = dataRepository.getData().getFirestations();

        for (FireStation fs : fireStations) {
            if (fs.getStation() == stationNumber) {
                addresses.add(fs.getAddress());
            }
        }

        return addresses;
    }
}