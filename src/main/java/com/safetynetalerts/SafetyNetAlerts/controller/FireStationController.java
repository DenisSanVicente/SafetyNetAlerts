package com.safetynetalerts.SafetyNetAlerts.controller;

import com.safetynetalerts.SafetyNetAlerts.DTO.FireDTO;
import com.safetynetalerts.SafetyNetAlerts.DTO.FireStationCoverageDTO;
import com.safetynetalerts.SafetyNetAlerts.model.FireStation;
import com.safetynetalerts.SafetyNetAlerts.model.Person;
import com.safetynetalerts.SafetyNetAlerts.service.FireStationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class FireStationController {

    private final FireStationService fireStationService;

    public FireStationController(FireStationService service) {
        this.fireStationService = service;
    }

    @GetMapping("/firestations")
    public List<FireStation> getAllFireStations() {
        return fireStationService.getAllFireStations();
    }

    @GetMapping("/firestation")
    public FireStationCoverageDTO getCoverage(@RequestParam int stationNumber) {
        return fireStationService.getPersonsCoveredByStation(stationNumber);
    }

    @GetMapping("/firestation")
    public FireDTO getFireInfo(@RequestParam String address) {

        return fireStationService.getFireInfoByAddress(address);
    }

    /// ===== ETAPE 3 - ENDPOINT 2 ===== ///
    @PostMapping("/firestation")
    public FireStation addFireStation(
            @RequestBody FireStation fireStation) {

        return fireStationService.addFireStation(fireStation);
    }

    @PutMapping("/firestation")
    public FireStation updatedFireStation(
            @RequestBody FireStation fireStation) {

        return fireStationService.updateFireStation(fireStation);
    }

    @DeleteMapping("/firestation")
    public boolean deleteFireStation(
            @RequestParam String address) {

        return fireStationService.deleteFireStation(address);
    }
}
