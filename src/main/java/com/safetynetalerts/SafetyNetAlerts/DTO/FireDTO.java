package com.safetynetalerts.SafetyNetAlerts.DTO;

import lombok.*;

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
