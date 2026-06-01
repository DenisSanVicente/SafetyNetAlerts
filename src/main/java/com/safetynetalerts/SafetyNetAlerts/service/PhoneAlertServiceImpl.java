package com.safetynetalerts.SafetyNetAlerts.service;

import com.safetynetalerts.SafetyNetAlerts.DTO.PhoneAlertDTO;
import com.safetynetalerts.SafetyNetAlerts.model.Person;
import com.safetynetalerts.SafetyNetAlerts.repository.DataRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PhoneAlertServiceImpl implements PhoneAlertService {

    private static final Logger log = LogManager.getLogger(PhoneAlertServiceImpl.class);

    private final DataRepository dataRepository;

    private final FireStationService fireStationService;

    public PhoneAlertServiceImpl(DataRepository dataRepository, FireStationService service) {
        this.dataRepository = dataRepository;
        this.fireStationService = service;
    }

    /**
     * Récupère les numéros de téléphone des personnes associées à une station.
     *
     * @param stationNumber le numéro de la station
     * @return une liste de numéros de téléphone associés à la station
     */
    @Override
    public PhoneAlertDTO getPhonesByStation(int stationNumber) {

        log.info("Request received: getPhonesByStation(stationNumber={})", stationNumber);

        List<String> addresses = fireStationService.getAddressesByStation(stationNumber);
        List<Person> allPersons = dataRepository.getData().getPersons();

        log.debug(
                "Processing {} addresses and {} persons for station {}",
                addresses.size(),
                allPersons.size(),
                stationNumber
        );

        Set<String> phones = new HashSet<>();

        for (Person person : allPersons) {

            if (addresses.contains(person.getAddress())) {
                phones.add(person.getPhone());
            }
        }

        log.info(
                "{} phone numbers found for station {}",
                phones.size(),
                stationNumber
        );

        return new PhoneAlertDTO(new ArrayList<>(phones));
    }
}