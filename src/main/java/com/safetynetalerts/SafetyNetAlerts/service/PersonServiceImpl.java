package com.safetynetalerts.SafetyNetAlerts.service;

import com.safetynetalerts.SafetyNetAlerts.DTO.ChildAlertDTO;
import com.safetynetalerts.SafetyNetAlerts.DTO.HouseHoldMemberDTO;
import com.safetynetalerts.SafetyNetAlerts.DTO.PersonCoveredDTO;
import com.safetynetalerts.SafetyNetAlerts.Util.AgeCalculator;
import com.safetynetalerts.SafetyNetAlerts.model.MedicalRecord;
import com.safetynetalerts.SafetyNetAlerts.model.Person;
import com.safetynetalerts.SafetyNetAlerts.repository.DataRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    private final DataRepository dataRepository;

    public PersonServiceImpl(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public List<Person> getAllPersons() {
        return dataRepository.getData().getPersons();
    }

    /// ===== ETAPE 3 - Endpoint 2 ===== ///
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
        return personsByAddress;
    }

    /// ===== Endpoint 2 ===== ///
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

        return children;
    }

}