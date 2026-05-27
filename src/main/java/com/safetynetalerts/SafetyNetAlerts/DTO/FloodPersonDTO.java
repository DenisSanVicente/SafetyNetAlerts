package com.safetynetalerts.SafetyNetAlerts.DTO;

import lombok.*;

import java.util.List;

/// ===== ETAPE 3 - Endpoint 5 ===== ///

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FloodPersonDTO {

    private String firstName;
    private String lastName;
    private String phone;
    private int age;
    private List<String> medications;
    private List<String> allergies;
}
