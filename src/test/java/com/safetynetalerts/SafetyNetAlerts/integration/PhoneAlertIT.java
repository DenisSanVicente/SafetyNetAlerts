package com.safetynetalerts.SafetyNetAlerts.integration;

import com.safetynetalerts.SafetyNetAlerts.DTO.PhoneAlertDTO;
import com.safetynetalerts.SafetyNetAlerts.service.PhoneAlertService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PhoneAlertIT {

    @Autowired
    private PhoneAlertService phoneAlertService;

    @Test
    void shouldReturnPhoneNumbersByStation() {

        // WHEN
        PhoneAlertDTO result = phoneAlertService.getPhonesByStation(1);

        // THEN
        assertAll(
                () -> assertNotNull(result),
                () -> assertNotNull(result.getPhones()),
                () -> assertFalse(result.getPhones().isEmpty())
        );
    }
}