package com.safetynetalerts.SafetyNetAlerts.service;

import com.safetynetalerts.SafetyNetAlerts.DTO.ChildAlertDTO;
import com.safetynetalerts.SafetyNetAlerts.DTO.PersonInfoDTO;
import com.safetynetalerts.SafetyNetAlerts.model.MedicalRecord;
import com.safetynetalerts.SafetyNetAlerts.model.Person;

import java.util.List;

public interface PersonService {

    List<Person> getAllPersons();

    List<Person> getPersonsByAddress(String address);

    List<ChildAlertDTO> getChildrenByAddress(String address);


    List<PersonInfoDTO> getPersonInfoByLastName(String lastName);

    List<String> getEmailsByCity(String city);

    Person addPerson(Person person);

    Person updatePerson(Person person);

    boolean deletePerson(String firstName, String lastName);
}
