package com.safetynetalerts.SafetyNetAlerts.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecord {

    private String firstName;
    private String lastName;
    private String birthdate;

    private List<String> medications;
    private List<String> allergies;
}