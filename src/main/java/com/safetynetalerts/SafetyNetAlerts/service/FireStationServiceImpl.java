package com.safetynetalerts.SafetyNetAlerts.service;

import com.safetynetalerts.SafetyNetAlerts.model.FireStation;
import com.safetynetalerts.SafetyNetAlerts.repository.FireStationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // SPRING
public class FireStationServiceImpl implements FireStationService {

    private final FireStationRepository repository;

    public FireStationServiceImpl(FireStationRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<FireStation> getAllFireStations() {
        return repository.findAll();
    }
}
