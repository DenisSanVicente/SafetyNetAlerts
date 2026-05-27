package com.safetynetalerts.SafetyNetAlerts.DTO;

import lombok.*;

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
