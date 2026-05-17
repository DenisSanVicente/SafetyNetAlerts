package com.safetynetalerts.SafetyNetAlerts.service;

import com.safetynetalerts.SafetyNetAlerts.DTO.PhoneAlertDTO;
import com.safetynetalerts.SafetyNetAlerts.model.FireStation;
import com.safetynetalerts.SafetyNetAlerts.model.Person;
import com.safetynetalerts.SafetyNetAlerts.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PhoneAlertServiceImpl implements PhoneAlertService {

    private final DataRepository dataRepository;

    @Override
    public PhoneAlertDTO getPhonesByStation(int stationNumber) {

        List<String> addresses = getAddressesByStation(stationNumber);
        List<Person> allPersons = dataRepository.getData().getPersons();

        Set<String> phones = new HashSet<>();

        for (Person person : allPersons) {

            if (addresses.contains(person.getAddress())) {
                phones.add(person.getPhone());
            }
        }

        return new PhoneAlertDTO(new ArrayList<>(phones));
    }

    /**
     * Récupère les adresses associées à une station
     */
    private List<String> getAddressesByStation(int stationNumber) {

        List<String> addresses = new ArrayList<>();
        List<FireStation> fireStations = dataRepository.getData().getFirestations();

        for (FireStation fs : fireStations) {
            if (fs.getStation() == stationNumber) {
                addresses.add(fs.getAddress());
            }
        }

        return addresses;
    }
}