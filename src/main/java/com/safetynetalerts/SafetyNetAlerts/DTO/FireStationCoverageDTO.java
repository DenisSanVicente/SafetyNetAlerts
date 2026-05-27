package com.safetynetalerts.SafetyNetAlerts.DTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FireStationCoverageDTO {

    private List<PersonCoveredDTO> persons;
    private int adultCount;
    private int childCount;
}
