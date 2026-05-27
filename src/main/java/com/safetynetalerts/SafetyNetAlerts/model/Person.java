package com.safetynetalerts.SafetyNetAlerts.model;

import lombok.*;

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