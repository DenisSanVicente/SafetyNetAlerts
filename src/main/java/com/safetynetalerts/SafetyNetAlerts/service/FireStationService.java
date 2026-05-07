package com.safetynetalerts.SafetyNetAlerts.service;

import com.safetynetalerts.SafetyNetAlerts.DTO.FireStationCoverageDTO;
import com.safetynetalerts.SafetyNetAlerts.DTO.PersonCoveredDTO;
import com.safetynetalerts.SafetyNetAlerts.model.FireStation;
import com.safetynetalerts.SafetyNetAlerts.model.Person;

import java.util.List;

public interface FireStationService {

    List<FireStation> getAllFireStations();

    /// ===== ETAPE 3 ===== ///
    List<FireStation> getFireStationsByStationNumber(int stationNumber); // On classe les stations selon leurs numéros

    FireStationCoverageDTO getPersonsCoveredByStation(int stationNumber);

    }