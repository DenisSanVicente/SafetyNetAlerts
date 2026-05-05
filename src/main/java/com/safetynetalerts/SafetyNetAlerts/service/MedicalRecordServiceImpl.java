package com.safetynetalerts.SafetyNetAlerts.service;


import com.safetynetalerts.SafetyNetAlerts.model.MedicalRecord;
import com.safetynetalerts.SafetyNetAlerts.repository.DataRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final DataRepository dataRepository;

    public MedicalRecordServiceImpl(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public List<MedicalRecord> getAllMedicalRecords() {
        return dataRepository.getData().getMedicalrecords();
    }
}