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
public class ChildAlertDTO {

    private String firstName;
    private String lastName;
    private int age;
    private List<HouseholdMemberDTO> householdMembers;
}
