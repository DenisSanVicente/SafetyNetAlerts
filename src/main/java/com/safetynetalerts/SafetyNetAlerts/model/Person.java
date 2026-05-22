package com.safetynetalerts.SafetyNetAlerts.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter // LOMBOK
@Setter // LOMBOK
@NoArgsConstructor // LOMBOK
@AllArgsConstructor // LOMBOK
public class Person {

    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;
}