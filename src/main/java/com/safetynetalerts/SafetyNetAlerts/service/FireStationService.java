package com.safetynetalerts.SafetyNetAlerts.service;

import com.safetynetalerts.SafetyNetAlerts.DTO.FireDTO;
import com.safetynetalerts.SafetyNetAlerts.DTO.FireStationCoverageDTO;
import com.safetynetalerts.SafetyNetAlerts.model.FireStation;

import java.util.List;

public interface FireStationService {

    List<FireStation> getAllFireStations();

    /// ===== ETAPE 3 ===== ///
    List<FireStation> getFireStationsByStationNumber(int stationNumber); // On classe les stations selon leurs numéros

    FireStationCoverageDTO getPersonsCoveredByStation(int stationNumber);

    /// ===== ETAPE 3 - URL 4 ===== ///
    FireDTO getFireInfoByAddress(String address);

    int getStationNumberByAddress(String address);


    /// ===== ETAPE 3 - ENDPOINT 2 ///
    FireStation addFireStation(FireStation fireStation);

    FireStation updateFireStation(FireStation fireStation);

    boolean deleteFireStation(String address);

    List<String> getAddressesByStation(int stationNumber);

}