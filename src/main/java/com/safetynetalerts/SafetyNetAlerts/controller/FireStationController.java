package com.safetynetalerts.SafetyNetAlerts.controller;

import com.safetynetalerts.SafetyNetAlerts.DTO.FireDTO;
import com.safetynetalerts.SafetyNetAlerts.DTO.FireStationCoverageDTO;
import com.safetynetalerts.SafetyNetAlerts.model.FireStation;
import com.safetynetalerts.SafetyNetAlerts.model.Person;
import com.safetynetalerts.SafetyNetAlerts.service.FireStationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class FireStationController {

    private final FireStationService service;

    public FireStationController(FireStationService service) {
        this.service = service;
    }

    @GetMapping("/firestations")
    public List<FireStation> getAllFireStations() {
        return service.getAllFireStations();
    }

    @GetMapping("/firestation")
    public FireStationCoverageDTO getCoverage(@RequestParam int stationNumber) {
        return service.getPersonsCoveredByStation(stationNumber);
    }

    @GetMapping
    public FireDTO getFireInfo(@RequestParam String address) {
        return service.getFireInfoByAddress(address);
    }

}
