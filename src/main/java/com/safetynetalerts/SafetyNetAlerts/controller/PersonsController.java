package com.safetynetalerts.SafetyNetAlerts.controller;

import com.safetynetalerts.SafetyNetAlerts.model.Persons;
import com.safetynetalerts.SafetyNetAlerts.service.PersonsService;
import com.sun.net.httpserver.Request;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // Définit une API REST
@RequestMapping("/persons") // Définit une route de base
public class PersonsController {

    private PersonsService service;


    public PersonsController(PersonsService service) {
        this.service = service;
    }

    @GetMapping // endpoints HTTP GET
    public List<Persons> getAllPersons() {
        return service.getAllPersons();
    }
}
