package com.safetynetalerts.SafetyNetAlerts.service;

import com.safetynetalerts.SafetyNetAlerts.DTO.PhoneAlertDTO;
import com.safetynetalerts.SafetyNetAlerts.model.FireStation;
import com.safetynetalerts.SafetyNetAlerts.model.Person;
import com.safetynetalerts.SafetyNetAlerts.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/// ===== ETAPE 3 - URL 3 ===== ///
@Service
@RequiredArgsConstructor
public class PhoneAlertServiceImpl implements PhoneAlertService {

    private final DataRepository dataRepository;

    @Override
    public PhoneAlertDTO getPhonesByStation(int stationNumber) {

        // Récupération des adresses couvertes par la station
        List<String> addresses = getFireStationAddresses(stationNumber);

        // Récupération de toutes les personnes
        List<Person> allPersons = dataRepository.getData().getPersons();

        // Liste des téléphones
        List<String> phones = new ArrayList<>();

        // Parcours des personnes
        for (Person person : allPersons) {

            // Vérifie si l'adresse est couverte
            if (addresses.contains(person.getAddress())) {

                // Évite les doublons
                if (!phones.contains(person.getPhone())) {
                    phones.add(person.getPhone());
                }
            }
        }

        return new PhoneAlertDTO(phones);
    }

    /**
     * Récupération des adresses associées à une station
     */
    private List<String> getFireStationAddresses(int stationNumber) {

        List<String> addresses = new ArrayList<>();

        List<FireStation> fireStations =
                dataRepository.getData().getFirestations();

        for (FireStation fs : fireStations) {

            if (fs.getStation() == stationNumber) {
                addresses.add(fs.getAddress());
            }
        }

        return addresses;
    }
}