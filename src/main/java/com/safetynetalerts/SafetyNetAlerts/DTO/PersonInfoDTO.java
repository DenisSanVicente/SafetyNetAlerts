package com.safetynetalerts.SafetyNetAlerts.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonInfoDTO {

    private String lastName;
    private String firstName;
    private String address;
    private int age;
    private String email;
    private List<String> medications;
    private List<String> allergies;
}
