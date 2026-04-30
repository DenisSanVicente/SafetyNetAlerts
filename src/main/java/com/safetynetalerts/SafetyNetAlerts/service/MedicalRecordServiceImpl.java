package com.safetynetalerts.SafetyNetAlerts.service;

import com.safetynetalerts.SafetyNetAlerts.model.MedicalRecord;
import com.safetynetalerts.SafetyNetAlerts.repository.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // SPRING
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository repository;


    public MedicalRecordServiceImpl(MedicalRecordRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<MedicalRecord> getAllMedicalRecords() {
        return repository.findAll();
    }
}
