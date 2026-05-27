package com.safetynetalerts.SafetyNetAlerts.DTO;

import lombok.*;

import java.util.List;

/// ===== ETAPE 3 - Endpoint 3 ===== ///
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhoneAlertDTO {

    private List<String> phones;
}
