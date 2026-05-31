package com.safetynetalerts.SafetyNetAlerts.service;

import com.safetynetalerts.SafetyNetAlerts.DTO.PhoneAlertDTO;
import com.safetynetalerts.SafetyNetAlerts.model.Person;
import com.safetynetalerts.SafetyNetAlerts.model.SafetyNetData;
import com.safetynetalerts.SafetyNetAlerts.repository.DataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PhoneAlertServiceImplTest {

    @Mock
    private DataRepository dataRepository;

    @Mock
    private FireStationService fireStationService;

    @InjectMocks
    private PhoneAlertServiceImpl phoneAlertService;

    @Test
    void getPhonesByStationTest() {

        // GIVEN
        int stationNumber = 1;

        String address1 = "1509 Culver St";
        String address2 = "644 Gershwin Cir";

        Person ps1 = new Person();
        ps1.setFirstName("John");
        ps1.setPhone("123-456");
        ps1.setAddress(address1);

        Person ps2 = new Person();
        ps2.setFirstName("Jane");
        ps2.setPhone("123-456");
        ps2.setAddress(address2);

        Person ps3 = new Person();
        ps3.setFirstName("Bill");
        ps3.setPhone("456-789");
        ps3.setAddress(address1);

        SafetyNetData data = new SafetyNetData();
        data.setPersons(List.of(ps1, ps2, ps3));

        when(dataRepository.getData()).thenReturn(data);

        when(fireStationService.getAddressesByStation(stationNumber))
                .thenReturn(List.of(address1, address2));

        // WHEN
        PhoneAlertDTO result = phoneAlertService.getPhonesByStation(stationNumber);

        // THEN
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(2, result.getPhones().size()),
                () -> assertTrue(result.getPhones().contains("123-456")),
                () -> assertTrue(result.getPhones().contains("456-789"))
        );
    }
}
