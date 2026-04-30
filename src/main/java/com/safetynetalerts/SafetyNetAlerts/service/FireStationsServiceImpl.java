package com.safetynetalerts.SafetyNetAlerts.service;

import com.safetynetalerts.SafetyNetAlerts.model.FireStations;
import com.safetynetalerts.SafetyNetAlerts.repository.FireStationsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FireStationsServiceImpl implements FireStationsService {

    private FireStationsRepository repository;

    public FireStationsServiceImpl(FireStationsRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<FireStations> getAllFireStations() {
        return repository.findAll();
    }
}
