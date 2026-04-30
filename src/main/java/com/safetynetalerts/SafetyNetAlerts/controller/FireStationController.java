package com.safetynetalerts.SafetyNetAlerts.controller;

import com.safetynetalerts.SafetyNetAlerts.model.FireStation;
import com.safetynetalerts.SafetyNetAlerts.service.FireStationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/firestations")
public class FireStationController {

    private final FireStationService service;

    public FireStationController(FireStationService service) {
        this.service = service;
    }

    @GetMapping
    public List<FireStation> getAllFireStations() {
        return service.getAllFireStations();
    }
}
