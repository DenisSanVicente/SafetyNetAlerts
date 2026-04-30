package com.safetynetalerts.SafetyNetAlerts.service;

import com.safetynetalerts.SafetyNetAlerts.model.MedicalRecords;
import com.safetynetalerts.SafetyNetAlerts.repository.MedicalRecordsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordsServiceImpl implements MedicalRecordsService {

    private MedicalRecordsRepository repository;


    public MedicalRecordsServiceImpl(MedicalRecordsRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<MedicalRecords> getAllMedicalRecords() {
        return repository.findAll();
    }
}
