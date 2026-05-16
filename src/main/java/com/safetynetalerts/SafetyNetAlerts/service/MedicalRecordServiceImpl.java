package com.safetynetalerts.SafetyNetAlerts.service;


import com.safetynetalerts.SafetyNetAlerts.model.MedicalRecord;
import com.safetynetalerts.SafetyNetAlerts.repository.DataRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final DataRepository dataRepository;

    private static final Logger log = LogManager.getLogger(MedicalRecordServiceImpl.class);

    public MedicalRecordServiceImpl(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public List<MedicalRecord> getAllMedicalRecords() {
        log.info("getAllMedicalRecords OK");
        return dataRepository.getData().getMedicalrecords();
    }
}