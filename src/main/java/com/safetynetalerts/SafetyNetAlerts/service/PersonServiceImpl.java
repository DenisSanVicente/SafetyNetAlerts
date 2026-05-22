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

    @Override
    public List<Person> getAllPersons() {
        return dataRepository.getData().getPersons();
    }

    /// ===== ETAPE 3 - URL 2 ===== ///
    // Liste de personnes habitant à une adresse
    @Override
    public List<Person> getPersonsByAddress(String address) {

        List<Person> allPersons = getAllPersons();
        List<Person> personsByAddress = new ArrayList<>();

        for (Person ps : allPersons) {
            if (ps.getAddress().equals(address)) {
                personsByAddress.add(ps);
            }
        }

        log.info("getPersonsByAddress OK");
        return personsByAddress;
    }

    /// ===== ETAPE 3 - URL 2 ===== ///
    @Override
    public List<ChildAlertDTO> getChildrenByAddress(String address) {

        List<Person> personsByAddress = getPersonsByAddress(address);
        List<MedicalRecord> medicalRecords = dataRepository.getData().getMedicalrecords();

        List<ChildAlertDTO> children = new ArrayList<>();

        for (Person ps : personsByAddress) {

            for (MedicalRecord mr : medicalRecords) {

                if (mr.getFirstName().equals(ps.getFirstName())
                        && mr.getLastName().equals(ps.getLastName())) {

                    int age = AgeCalculator.calculateAge(mr.getBirthdate());

                    if (age <= 18) {

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

        log.info("getChildrenByAddress OK");
        return children;
    }

    /// ===== ETAPE 3 - URL 6 ===== ///
    @Override
    public List<PersonInfoDTO> getPersonInfoByLastName(String lastName) {

        List<Person> allPersons = dataRepository.getData().getPersons();
        List<MedicalRecord> allMedicalRecords = dataRepository.getData().getMedicalrecords();

        List<PersonInfoDTO> result = new ArrayList<>();

        for (Person ps : allPersons) {

            if (ps.getLastName().equalsIgnoreCase(lastName)) {

                int age = 0;
                List<String> medications = new ArrayList<>();
                List<String> allergies = new ArrayList<>();

                for (MedicalRecord mr : allMedicalRecords) {

                    if (mr.getFirstName().equals(ps.getFirstName())
                            && mr.getLastName().equals(ps.getLastName())) {

                        age = AgeCalculator.calculateAge(mr.getBirthdate());
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

        log.info("getPersonInfoByLastName OK");
        return result;
    }

    /// ===== ETAPE 3 - URL 7 ===== ///
    @Override
    public List<String> getEmailsByCity(String city) {

        List<Person> allPersons = dataRepository.getData().getPersons();
        List<String> emails = new ArrayList<>();

        for (Person ps : allPersons) {
            if (ps.getCity().equals(city)) {
                emails.add(ps.getEmail());
            }
        }

        log.info("getEmailsByCity OK");
        return emails;
    }

    /// ===== ETAPE 3 - ENDPOINT 1 ===== ///
    // POST - Ajouter une personne
    @Override
    public Person addPerson(Person person) {

        List<Person> persons = dataRepository.getData().getPersons();
        persons.add(person);

        log.info("addPerson OK");
        return person;
    }

    // PUT - Modifier une personne
    @Override
    public Person updatePerson(Person updatedPerson) {

        List<Person> allPersons = dataRepository.getData().getPersons();

        for (Person person : allPersons) {

            if (person.getFirstName().equals(updatedPerson.getFirstName())
                    && person.getLastName().equals(updatedPerson.getLastName())) {

                person.setAddress(updatedPerson.getAddress());
                person.setCity(updatedPerson.getCity());
                person.setZip(updatedPerson.getZip());
                person.setPhone(updatedPerson.getPhone());
                person.setEmail(updatedPerson.getEmail());

                log.info("updatePerson OK");
                return person;
            }
        }

        log.warn("updatePerson NOT FOUND");
        return null;
    }

    // DELETE - Supprimer une personne
    @Override
    public boolean deletePerson(String firstName, String lastName) {

        List<Person> allPersons = dataRepository.getData().getPersons();

        Iterator<Person> iterator = allPersons.iterator();

        while (iterator.hasNext()) {

            Person person = iterator.next();

            if (person.getFirstName().equals(firstName)
                    && person.getLastName().equals(lastName)) {

                iterator.remove();

                log.info("deletePerson OK");
                return true;
            }
        }

        log.warn("deletePerson NOT FOUND");
        return false;
    }
}