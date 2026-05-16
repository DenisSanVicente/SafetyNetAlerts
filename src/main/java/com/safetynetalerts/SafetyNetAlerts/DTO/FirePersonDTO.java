package com.safetynetalerts.SafetyNetAlerts.DTO;

import com.safetynetalerts.SafetyNetAlerts.model.MedicationModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FirePersonDTO {

    private String lastName;
    private String firstName;
    private String phoneNumber;
    private int age;
    private List<String> medications;
    private List<String> allergies;
}
