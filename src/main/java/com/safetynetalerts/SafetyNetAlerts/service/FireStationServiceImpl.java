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

    private final PersonService personService;

    private final MedicalRecordService medicalRecordService;

    public FireStationServiceImpl(DataRepository dataRepository, PersonService personService, MedicalRecordService medicalRecordService) {
        this.dataRepository = dataRepository;
        this.personService = personService;
        this.medicalRecordService = medicalRecordService;
    }


    // ===================== ENDPOINTS METIER ===================== //

    /**
     * Récupère toutes les personnes couvertes par une station spécifique et compte le nombre d'enfants et d'adultes
     *
     * @param stationNumber le numéro de la station
     * @return un DTO contenant la liste des personnes couvertes, le nombre d'enfants et le nombre d'adultes
     */
    @Override
    public FireStationCoverageDTO getPersonsCoveredByStation(int stationNumber) {

        log.info("Request received: getPersonsCoveredByStation(stationNumber={})", stationNumber);

        try {

            List<String> addresses = getFireStationAddresses(stationNumber);

            log.debug(
                    "Found {} addresses linked to station {}",
                    addresses.size(),
                    stationNumber
            );

            List<PersonCoveredDTO> personsCovered = new ArrayList<>();

            int adultCount = 0;
            int childCount = 0;

            for (String address : addresses) {

                List<Person> personsAtAddress = personService.getPersonsByAddress(address);

                for (Person person : personsAtAddress) {

                    MedicalRecord mr = medicalRecordService.getMedicalRecordByPerson(person);

                    if (mr == null) {
                        log.warn(
                                "No medical record found for {} {}",
                                person.getFirstName(),
                                person.getLastName()
                        );
                        continue;
                    }

                    int age = AgeCalculator.calculateAge(mr.getBirthdate());

                    log.debug(
                            "Calculated age {} for {} {}",
                            age,
                            person.getFirstName(),
                            person.getLastName()
                    );

                    if (age <= 18) {
                        childCount++;
                    } else {
                        adultCount++;
                    }

                    PersonCoveredDTO dto = new PersonCoveredDTO();
                    dto.setFirstName(person.getFirstName());
                    dto.setLastName(person.getLastName());
                    dto.setAddress(person.getAddress());
                    dto.setPhone(person.getPhone());

                    personsCovered.add(dto);
                }
            }

            log.info(
                    "Station {} coverage retrieved: {} persons ({} adults, {} children)",
                    stationNumber,
                    personsCovered.size(),
                    adultCount,
                    childCount
            );

            return new FireStationCoverageDTO(personsCovered, adultCount, childCount);

        } catch (Exception e) {

            log.error(
                    "Error while retrieving coverage for station {}",
                    stationNumber,
                    e
            );

            throw e;
        }
    }


    /**
     * Récupère les informations d'une station et des résidents vivant à cette adresse
     *
     * @param address : l'adresse recherchée
     * @return un DTO le numéro de station et les informations sur les résidents
     */
    @Override
    public FireDTO getFireInfoByAddress(String address) {

        log.info("Request received: getFireInfoByAddress(address={})", address);

        try {

            int stationNumber = getStationNumberByAddress(address);
            log.debug("Station number {} found for address {}", stationNumber, address);

            List<Person> persons = personService.getPersonsByAddress(address);
            log.debug("{} persons found at address {}", persons.size(), address);

            List<FirePersonDTO> residents = new ArrayList<>();

            for (Person person : persons) {

                MedicalRecord mr = medicalRecordService.getMedicalRecordByPerson(person);

                if (mr == null) {
                    log.warn(
                            "No medical record found for {} {}",
                            person.getFirstName(),
                            person.getLastName()
                    );
                    continue;
                }

                FirePersonDTO dto = new FirePersonDTO();
                dto.setFirstName(person.getFirstName());
                dto.setLastName(person.getLastName());
                dto.setPhoneNumber(person.getPhone());
                dto.setAge(AgeCalculator.calculateAge(mr.getBirthdate()));
                dto.setMedications(mr.getMedications());
                dto.setAllergies(mr.getAllergies());

                residents.add(dto);
            }

            FireDTO fireDTO = new FireDTO();
            fireDTO.setStationNumber(stationNumber);
            fireDTO.setResidents(residents);

            log.info(
                    "Fire info retrieved for address {} : station {}, {} residents",
                    address,
                    stationNumber,
                    residents.size()
            );

            return fireDTO;

        } catch (Exception e) {

            log.error(
                    "Error while retrieving fire information for address {}",
                    address,
                    e
            );

            throw e;
        }
    }

    // ===================== QUERIES SIMPLES ===================== //

    /**
     * Récupère toutes les stations
     *
     * @return la liste de toutes les stations
     */
    @Override
    public List<FireStation> getAllFireStations() {

        log.info("Request received: getAllFireStations()");

        try {

            List<FireStation> fireStations =
                    dataRepository.getData().getFirestations();

            log.info(
                    "{} fire stations retrieved",
                    fireStations.size()
            );

            return fireStations;

        } catch (Exception e) {

            log.error("Error while retrieving fire stations", e);
            throw e;
        }
    }


    /**
     * Récupère toutes les stations qui matchent avec le numéro de station spécifié
     *
     * @param stationNumber
     * @return liste des stations correspondant au numéro spécifié
     */
    @Override
    public List<FireStation> getFireStationsByStationNumber(int stationNumber) {

        log.info(
                "Request received: getFireStationsByStationNumber(stationNumber={})",
                stationNumber
        );

        try {

            List<FireStation> result = new ArrayList<>();

            for (FireStation fs : getAllFireStations()) {
                if (fs.getStation() == stationNumber) {
                    result.add(fs);
                }
            }

            log.info(
                    "{} fire stations found for station number {}",
                    result.size(),
                    stationNumber
            );

            return result;

        } catch (Exception e) {

            log.error(
                    "Error while retrieving fire stations for station number {}",
                    stationNumber,
                    e
            );

            throw e;
        }
    }

    // ===================== HELPERS =====================

    /**
     * Récupère toutes les adresses uniques couvertes par une station.
     *
     * @param stationNumber le numéro de la station
     * @return la liste des adresses couvertes par la station
     */
    private List<String> getFireStationAddresses(int stationNumber) {

        List<String> addresses = new ArrayList<>();

        for (FireStation fs : getFireStationsByStationNumber(stationNumber)) {
            if (!addresses.contains(fs.getAddress())) {
                addresses.add(fs.getAddress());
            }
        }

        log.debug(
                "{} addresses found for station {}",
                addresses.size(),
                stationNumber
        );

        return addresses;
    }


    /**
     * Récupère le numéro de station correspondant à une adresse.
     *
     * @param address l'adresse recherchée
     * @return le numéro de station ou 0 si aucune station n'est trouvée
     */
    @Override
    public int getStationNumberByAddress(String address) {

        for (FireStation fs : dataRepository.getData().getFirestations()) {
            if (fs.getAddress().equals(address)) {
                return fs.getStation();
            }
        }

        log.warn("No station found for address: {}", address);
        return 0;
    }


    /**
     * Récupère toutes les adresses couvertes par une station
     *
     * @param stationNumber
     * @return liste des adresses couvertes
     */
    @Override
    public List<String> getAddressesByStation(int stationNumber) {

        List<String> addresses = new ArrayList<>();
        List<FireStation> fireStations = dataRepository.getData().getFirestations();

        log.debug(
                "Retrieving addresses for station {} from {} fire station entries",
                stationNumber,
                fireStations.size()
        );

        for (FireStation fs : fireStations) {
            if (fs.getStation() == stationNumber) {
                addresses.add(fs.getAddress());
            }
        }

        log.info(
                "{} addresses found for station {}",
                addresses.size(),
                stationNumber
        );

        return addresses;
    }


    // ===================== CRUD ===================== //

    /**
     * Ajoute une nouvelle station de pompiers.
     *
     * @param fireStation la station à ajouter
     * @return la station ajoutée
     */
    @Override
    public FireStation addFireStation(FireStation fireStation) {

        log.debug("Adding new fire station: {}", fireStation);

        if (fireStation == null) {
            log.error("Attempt to add null fire station");
            throw new IllegalArgumentException("Fire station cannot be null");
        }

        dataRepository.getData().getFirestations().add(fireStation);

        log.info(
                "Fire station added : address={}, station={}",
                fireStation.getAddress(),
                fireStation.getStation()
        );

        return fireStation;
    }


    /**
     * Met à jour une station existante.
     *
     * @param updatedFireStation la station avec les nouvelles informations
     * @return la station mise à jour, ou null si elle n'est pas trouvée
     */
    @Override
    public FireStation updateFireStation(FireStation updatedFireStation) {

        log.debug(
                "Updating fire station for address={}",
                updatedFireStation.getAddress()
        );

        for (FireStation fs : dataRepository.getData().getFirestations()) {

            if (fs.getAddress().equals(updatedFireStation.getAddress())) {

                fs.setStation(updatedFireStation.getStation());

                log.info(
                        "Fire station updated : address={}, newStation={}",
                        fs.getAddress(),
                        fs.getStation()
                );

                return fs;
            }
        }

        log.warn(
                "No fire station found for address {}",
                updatedFireStation.getAddress()
        );

        return null;
    }


    /**
     * Supprime une station de pompiers associée à une adresse.
     *
     * @param address l'adresse de la station à supprimer
     * @return true si la suppression a été effectuée, false si aucune station n'a été trouvée
     */
    @Override
    public boolean deleteFireStation(String address) {

        log.debug("Attempting to delete fire station for address={}", address);

        FireStation toDelete = null;

        for (FireStation fs : dataRepository.getData().getFirestations()) {

            if (fs.getAddress().equals(address)) {
                toDelete = fs;
                break;
            }
        }

        if (toDelete != null) {

            dataRepository.getData().getFirestations().remove(toDelete);

            log.info(
                    "Fire station deleted for address {}",
                    address
            );

            return true;
        }

        log.warn(
                "No fire station found for address {}",
                address
        );

        return false;
    }
}