package com.safetynetalerts.SafetyNetAlerts.service;

import com.safetynetalerts.SafetyNetAlerts.DTO.ChildAlertDTO;
import com.safetynetalerts.SafetyNetAlerts.DTO.HouseholdMemberDTO;
import com.safetynetalerts.SafetyNetAlerts.DTO.PersonInfoDTO;
import com.safetynetalerts.SafetyNetAlerts.Util.AgeCalculator;
import com.safetynetalerts.SafetyNetAlerts.model.MedicalRecord;
import com.safetynetalerts.SafetyNetAlerts.model.Person;
import com.safetynetalerts.SafetyNetAlerts.repository.DataRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    private static final Logger log = LogManager.getLogger(PersonServiceImpl.class);

    private final DataRepository dataRepository;

    public PersonServiceImpl(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    /**
     * Récupère toutes les personnes.
     *
     * @return la liste de toutes les personnes
     */
    @Override
    public List<Person> getAllPersons() {

        log.info("Request received: getAllPersons()");

        try {

            List<Person> allPersons = dataRepository.getData().getPersons();

            log.info("{} persons retrieved", allPersons.size());

            return allPersons;

        } catch (Exception e) {

            log.error("Error while retrieving persons", e);
            throw e;
        }
    }


    // ===== ETAPE 3 - URL 2 ===== //

    /**
     * Récupère la liste des personnes vivant à une adresse spécifiée.
     *
     * @param address l'adresse spécifiée
     * @return la liste des personnes vivant à cette adresse
     */
    @Override
    public List<Person> getPersonsByAddress(String address) {

        log.info("Request received: getPersonsByAddress(address={})", address);

        List<Person> allPersons = getAllPersons();
        List<Person> personsByAddress = new ArrayList<>();

        log.debug("Filtering {} persons by address {}", allPersons.size(), address);

        for (Person ps : allPersons) {
            if (ps.getAddress().equals(address)) {
                personsByAddress.add(ps);
            }
        }

        log.info(
                "{} persons found for address {}",
                personsByAddress.size(),
                address
        );

        return personsByAddress;
    }

    // ===== ETAPE 3 - URL 2 ===== //

    /**
     * Récupère la liste des enfants vivant à une adresse spécifique.
     * Un enfant est une personne de moins de 18 ans.
     * Inclut également les personnes vivant dans le même foyer.
     *
     * @param address l'adresse recherchée
     * @return la liste des enfants avec les membres du foyer associés
     */
    @Override
    public List<ChildAlertDTO> getChildrenByAddress(String address) {

        log.info("Request received: getChildrenByAddress(address={})", address);

        List<Person> personsByAddress = getPersonsByAddress(address);
        List<MedicalRecord> medicalRecords = dataRepository.getData().getMedicalrecords();

        log.debug(
                "Processing {} persons and {} medical records for address {}",
                personsByAddress.size(),
                medicalRecords.size(),
                address
        );

        List<ChildAlertDTO> children = new ArrayList<>();

        for (Person ps : personsByAddress) {

            for (MedicalRecord mr : medicalRecords) {

                if (mr.getFirstName().equals(ps.getFirstName())
                        && mr.getLastName().equals(ps.getLastName())) {

                    int age = AgeCalculator.calculateAge(mr.getBirthdate());

                    log.debug(
                            "Calculated age {} for {} {}",
                            age,
                            ps.getFirstName(),
                            ps.getLastName()
                    );

                    if (age <= 18) {

                        log.debug(
                                "Child detected: {} {}",
                                ps.getFirstName(),
                                ps.getLastName()
                        );

                        List<HouseholdMemberDTO> householdMembers = new ArrayList<>();

                        for (Person otherPerson : personsByAddress) {

                            if (!(otherPerson.getFirstName().equals(ps.getFirstName())
                                    && otherPerson.getLastName().equals(ps.getLastName()))) {

                                HouseholdMemberDTO memberDTO = new HouseholdMemberDTO();
                                memberDTO.setFirstName(otherPerson.getFirstName());
                                memberDTO.setLastName(otherPerson.getLastName());

                                householdMembers.add(memberDTO);
                            }
                        }

                        ChildAlertDTO childDTO = new ChildAlertDTO();
                        childDTO.setFirstName(ps.getFirstName());
                        childDTO.setLastName(ps.getLastName());
                        childDTO.setAge(age);
                        childDTO.setHouseholdMembers(householdMembers);

                        children.add(childDTO);
                    }

                    break;
                }
            }
        }

        log.info(
                "{} children found for address {}",
                children.size(),
                address
        );

        return children;
    }


    // ===== ETAPE 3 - URL 6 ===== //

    /**
     * Récupère les informations détaillées de toutes les personnes ayant le même nom de famille.
     * Ces informations incluent les données personnelles, l'âge, les allergies et les traitements médicaux.
     *
     * @param lastName le nom de famille recherché
     * @return une liste de DTO contenant les informations détaillées par personne
     */
    @Override
    public List<PersonInfoDTO> getPersonInfoByLastName(String lastName) {

        log.info("Request received: getPersonInfoByLastName(lastName={})", lastName);

        List<Person> allPersons = dataRepository.getData().getPersons();
        List<MedicalRecord> allMedicalRecords = dataRepository.getData().getMedicalrecords();

        log.debug(
                "Processing {} persons and {} medical records",
                allPersons.size(),
                allMedicalRecords.size()
        );

        List<PersonInfoDTO> result = new ArrayList<>();

        for (Person ps : allPersons) {

            if (ps.getLastName().equalsIgnoreCase(lastName)) {

                log.debug(
                        "Matching person found: {} {}",
                        ps.getFirstName(),
                        ps.getLastName()
                );

                int age = 0;
                List<String> medications = new ArrayList<>();
                List<String> allergies = new ArrayList<>();

                for (MedicalRecord mr : allMedicalRecords) {

                    if (mr.getFirstName().equals(ps.getFirstName())
                            && mr.getLastName().equals(ps.getLastName())) {

                        age = AgeCalculator.calculateAge(mr.getBirthdate());

                        log.debug(
                                "Medical record found for {} {} (age={})",
                                ps.getFirstName(),
                                ps.getLastName(),
                                age
                        );

                        medications = mr.getMedications();
                        allergies = mr.getAllergies();

                        break;
                    }
                }

                PersonInfoDTO dto = new PersonInfoDTO();
                dto.setFirstName(ps.getFirstName());
                dto.setLastName(ps.getLastName());
                dto.setAddress(ps.getAddress());
                dto.setEmail(ps.getEmail());
                dto.setAge(age);
                dto.setMedications(medications);
                dto.setAllergies(allergies);

                result.add(dto);
            }
        }

        log.info(
                "{} persons found for last name {}",
                result.size(),
                lastName
        );

        return result;
    }

    // ===== ETAPE 3 - URL 7 ===== ///

    /**
     * Récupère les adresses email des personnes vivant dans une ville spécifiée.
     *
     * @param city la ville recherchée
     * @return la liste des emails des personnes vivant dans cette ville
     */
    @Override
    public List<String> getEmailsByCity(String city) {

        log.info("Request received: getEmailsByCity(city={})", city);

        List<Person> allPersons = dataRepository.getData().getPersons();
        List<String> emails = new ArrayList<>();

        log.debug(
                "Filtering {} persons for city {}",
                allPersons.size(),
                city
        );

        for (Person ps : allPersons) {
            if (ps.getCity().equals(city)) {
                emails.add(ps.getEmail());
            }
        }

        log.info(
                "{} emails found for city {}",
                emails.size(),
                city
        );

        return emails;
    }

    // ================== CRUD =============== //

    // ===== ETAPE 3 - ENDPOINT 1 ===== //

    /**
     * Ajoute une personne.
     *
     * @param person la personne à ajouter
     * @return la personne ajoutée
     */
    @Override
    public Person addPerson(Person person) {

        log.debug(
                "Adding person: {} {}",
                person != null ? person.getFirstName() : "null",
                person != null ? person.getLastName() : "null"
        );

        if (person == null) {
            log.error("Attempt to add null person");
            throw new IllegalArgumentException("Person cannot be null");
        }

        List<Person> persons = dataRepository.getData().getPersons();
        persons.add(person);

        log.info(
                "Person added: {} {}",
                person.getFirstName(),
                person.getLastName()
        );

        return person;
    }


    /**
     * Met à jour les informations d'une personne.
     * L'identification se fait via le prénom et le nom.
     *
     * @param updatedPerson la personne contenant les nouvelles informations
     * @return la personne mise à jour, ou null si elle n'est pas trouvée
     */
    @Override
    public Person updatePerson(Person updatedPerson) {

        log.debug(
                "Updating person: {} {}",
                updatedPerson.getFirstName(),
                updatedPerson.getLastName()
        );

        List<Person> allPersons = dataRepository.getData().getPersons();

        for (Person person : allPersons) {

            if (person.getFirstName().equals(updatedPerson.getFirstName())
                    && person.getLastName().equals(updatedPerson.getLastName())) {

                person.setAddress(updatedPerson.getAddress());
                person.setCity(updatedPerson.getCity());
                person.setZip(updatedPerson.getZip());
                person.setPhone(updatedPerson.getPhone());
                person.setEmail(updatedPerson.getEmail());

                log.info(
                        "Person updated: {} {}",
                        person.getFirstName(),
                        person.getLastName()
                );

                return person;
            }
        }

        log.warn(
                "Person not found for update: {} {}",
                updatedPerson.getFirstName(),
                updatedPerson.getLastName()
        );

        return null;
    }


    /**
     * Supprime une personne identifiée par son prénom et son nom.
     *
     * @param firstName le prénom de la personne
     * @param lastName  le nom de la personne
     * @return true si la personne a été supprimée, false si elle n'a pas été trouvée
     */
    @Override
    public boolean deletePerson(String firstName, String lastName) {

        log.debug(
                "Attempting to delete person: {} {}",
                firstName,
                lastName
        );

        List<Person> allPersons = dataRepository.getData().getPersons();

        Iterator<Person> iterator = allPersons.iterator();

        while (iterator.hasNext()) {

            Person person = iterator.next();

            if (person.getFirstName().equals(firstName)
                    && person.getLastName().equals(lastName)) {

                iterator.remove();

                log.info(
                        "Person deleted: {} {}",
                        firstName,
                        lastName
                );

                return true;
            }
        }

        log.warn(
                "No person found to delete: {} {}",
                firstName,
                lastName
        );

        return false;
    }
}