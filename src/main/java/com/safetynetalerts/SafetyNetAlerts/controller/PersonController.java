package com.safetynetalerts.SafetyNetAlerts.controller;

import com.safetynetalerts.SafetyNetAlerts.DTO.ChildAlertDTO;
import com.safetynetalerts.SafetyNetAlerts.DTO.CommunityEmailDTO;
import com.safetynetalerts.SafetyNetAlerts.DTO.PersonInfoDTO;
import com.safetynetalerts.SafetyNetAlerts.DTO.PhoneAlertDTO;
import com.safetynetalerts.SafetyNetAlerts.model.Person;
import com.safetynetalerts.SafetyNetAlerts.service.PersonService;
import com.safetynetalerts.SafetyNetAlerts.service.PhoneAlertService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class PersonController {

    private final PersonService personService;
    private final PhoneAlertService phoneAlertService;

    public PersonController(PersonService personService,
                            PhoneAlertService phoneAlertService) {

        this.personService = personService;
        this.phoneAlertService = phoneAlertService;
    }

    @GetMapping("/persons")
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    /// ===== ETAPE 3 - URL 2 ===== ///
    @GetMapping("/childAlert")
    public List<ChildAlertDTO> getChildrenByAddress(
            @RequestParam String address) {

        return personService.getChildrenByAddress(address);
    }

    /// ===== ETAPE 3 - URL 3 ===== ///
    @GetMapping("/phoneAlert")
    public PhoneAlertDTO getPhonesByStation(
            @RequestParam int firestation) {

        return phoneAlertService.getPhonesByStation(firestation);
    }

    /// ===== ETAPE 3 - URL 6 ===== ///
    @GetMapping("/personInfo")
    public List<PersonInfoDTO> getPersonInfoByLastName(
            @RequestParam String lastName) {

        return personService.getPersonInfoByLastName(lastName);
    }

    /// ===== ETAPE 3 - URL 7 ===== ///
    @GetMapping("/communityEmail")
    public List<String> getCommunityEmails(
            @RequestParam String city) {

        return personService.getEmailsByCity(city);
    }
}