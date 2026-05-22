package com.safetynetalerts.SafetyNetAlerts.service;

import com.safetynetalerts.SafetyNetAlerts.DTO.PhoneAlertDTO;
import com.safetynetalerts.SafetyNetAlerts.model.Person;
import com.safetynetalerts.SafetyNetAlerts.repository.DataRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PhoneAlertServiceImpl implements PhoneAlertService {

    private final DataRepository dataRepository;

    private final FireStationService fireStationService;

    public PhoneAlertServiceImpl(DataRepository dataRepository, FireStationService service) {
        this.dataRepository = dataRepository;
        this.fireStationService = service;
    }

    @Override
    public PhoneAlertDTO getPhonesByStation(int stationNumber) {


        List<String> addresses = fireStationService.getAddressesByStation(stationNumber);
        List<Person> allPersons = dataRepository.getData().getPersons();

        Set<String> phones = new HashSet<>();

        for (Person person : allPersons) {

            if (addresses.contains(person.getAddress())) {
                phones.add(person.getPhone());
            }
        }

        return new PhoneAlertDTO(new ArrayList<>(phones));
    }

}