package com.safetynetalerts.SafetyNetAlerts.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/// ===== ETAPE 3 - Endpoint 3 ===== ///
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhoneAlertDTO {

    private List<String> phones;
}
