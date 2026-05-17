package com.safetynetalerts.SafetyNetAlerts.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/// ===== ETAPE 3 - URL 5 ===== ///
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FloodAddressDTO {

    private String address;
    private List<FloodPersonDTO> residents;
}
