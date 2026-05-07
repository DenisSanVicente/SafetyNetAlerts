package com.safetynetalerts.SafetyNetAlerts.Util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class AgeCalculator {

    public static int calculateAge(String birthdate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birth = LocalDate.parse(birthdate, formatter);

        return Period.between(birth, LocalDate.now()).getYears();
    }
}
