package com.safetynetalerts.SafetyNetAlerts.service;

import com.safetynetalerts.SafetyNetAlerts.DTO.ChildAlertDTO;
import com.safetynetalerts.SafetyNetAlerts.model.Person;

import java.util.List;

public interface PersonService {

    List<Person> getAllPersons();

    /// ===== ETAPE 3 ===== /// Endpoint 2
    // Récupérer les personnes par adresse
    List<Person> getAllPersonsByAddress(String address);

    // Récupérer les enfants de -18 ans
    List<ChildAlertDTO> getChildrenByAddress(String address);
}
