package com.safetynetalerts.SafetyNetAlerts.service;

import com.safetynetalerts.SafetyNetAlerts.DTO.PhoneAlertDTO;

/// ===== ETAPE 3 - URL 3 ===== ///
public interface PhoneAlertService {

    PhoneAlertDTO getPhonesByStation(int stationNumber);
}