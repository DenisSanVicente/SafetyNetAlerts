package com.safetynetalerts.SafetyNetAlerts.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data // LOMBOK -- Génère Getters & Setters
public class SafetyNetData {

    private List<Person> persons;
    private List<FireStation> firestations;
    private List<MedicalRecord> medicalrecords;
}