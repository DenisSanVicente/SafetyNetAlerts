package com.safetynetalerts.SafetyNetAlerts.service;

import com.safetynetalerts.SafetyNetAlerts.DTO.*;
import com.safetynetalerts.SafetyNetAlerts.Util.AgeCalculator;
import com.safetynetalerts.SafetyNetAlerts.model.MedicalRecord;
import com.safetynetalerts.SafetyNetAlerts.model.MedicationModel;
import com.safetynetalerts.SafetyNetAlerts.model.Person;
import com.safetynetalerts.SafetyNetAlerts.repository.DataRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public List<Person> getAllPersonsByAddress(String address) {
        List<Person> allPersons = getAllPersons();
        List<Person> personsByAddress = new ArrayList<>();

        for (Person ps : allPersons) {
            if (ps.getAddress().equals(address)) {
                personsByAddress.add(ps);
            }
        }

        log.info("geAllPersonsByAddress OK");
        return personsByAddress;

    }

    /// ===== ETAPE 3 - URL 2 ===== ///
    @Override
    public List<ChildAlertDTO> getChildrenByAddress(String address) {

        List<Person> personsByAddress = getAllPersonsByAddress(address);
        List<MedicalRecord> medicalRecords = dataRepository.getData().getMedicalrecords();

        // liste finale DTO
        List<ChildAlertDTO> children = new ArrayList<>();


        for (Person ps : personsByAddress) {
            // retrouver le medical record correspondant
            for (MedicalRecord mr : medicalRecords) {
                if (mr.getFirstName().equals(ps.getFirstName())
                        && mr.getLastName().equals(ps.getLastName())) {
                    // calcul âge
                    int age = AgeCalculator.calculateAge(mr.getBirthdate());

                    // garder uniquement les enfants
                    if (age <= 18) {
                        // liste des autres membres du foyer
                        List<HouseHoldMemberDTO> householdMembers = new ArrayList<>();

                        for (Person otherPerson : personsByAddress) {
                            // éviter d'ajouter l'enfant lui-même
                            if (!(otherPerson.getFirstName().equals(ps.getFirstName())
                                    && otherPerson.getLastName().equals(ps.getLastName()))) {

                                HouseHoldMemberDTO memberDTO =
                                        new HouseHoldMemberDTO();

                                memberDTO.setFirstName(otherPerson.getFirstName());
                                memberDTO.setLastName(otherPerson.getLastName());

                                householdMembers.add(memberDTO);
                            }
                        }

                        // création DTO enfant
                        ChildAlertDTO childDTO = new ChildAlertDTO();

                        childDTO.setFirstName(ps.getFirstName());
                        childDTO.setLastName(ps.getLastName());
                        childDTO.setAge(age);
                        childDTO.setHouseHoldMembers(householdMembers);

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
        List<PersonInfoDTO> personInfoList = new ArrayList<>();
        List<MedicalRecord> allMedicalRecords = dataRepository.getData().getMedicalrecords();

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
                dto.setMail(ps.getEmail());
                dto.setAge(age);
                dto.setMedications(medications);
                dto.setAllergies(allergies);

                personInfoList.add(dto);
            }
        }

        log.info("getPersonInfoByLastName OK");
        return personInfoList;
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
}