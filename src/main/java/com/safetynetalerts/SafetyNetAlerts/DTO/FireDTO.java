package com.safetynetalerts.SafetyNetAlerts.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/// ===== ETAPE 3 - Endpoint 4 ===== ///
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FireDTO {

    private int stationNumber;
    private List<FirePersonDTO> residents;
}
