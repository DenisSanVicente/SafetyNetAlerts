package com.safetynetalerts.SafetyNetAlerts.controller;

import com.safetynetalerts.SafetyNetAlerts.model.Person;
import com.safetynetalerts.SafetyNetAlerts.service.PersonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // SPRING MVC -- Définit une API REST
@RequestMapping("/persons") // Définit une route de base (LOMBOK)
public class PersonController {

    private final PersonService service;


    public PersonController(PersonService service) {
        this.service = service;
    }

    @GetMapping // endpoints HTTP GET
    public List<Person> getAllPersons() {
        return service.getAllPersons();
    }
}
