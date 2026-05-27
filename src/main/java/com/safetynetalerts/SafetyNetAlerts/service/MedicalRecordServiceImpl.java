package com.safetynetalerts.SafetyNetAlerts.service;

import com.safetynetalerts.SafetyNetAlerts.model.MedicalRecord;
import com.safetynetalerts.SafetyNetAlerts.model.Person;
import com.safetynetalerts.SafetyNetAlerts.repository.DataRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private static final Logger log = LogManager.getLogger(MedicalRecordServiceImpl.class);

    private final DataRepository dataRepository;

    public MedicalRecordServiceImpl(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public List<MedicalRecord> getAllMedicalRecords() {
        return dataRepository.getData().getMedicalrecords();
    }

    // ===================== CREATE =====================

    @Override
    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {

        List<MedicalRecord> medicalRecords = dataRepository.getData().getMedicalrecords();

        medicalRecords.add(medicalRecord);

        log.info("addMedicalRecord OK");

        return medicalRecord;
    }

    @Override
    public MedicalRecord getMedicalRecordByPerson(Person person) {

        for (MedicalRecord mr : dataRepository.getData().getMedicalrecords()) {
            if (mr.getFirstName().equals(person.getFirstName())
                    && mr.getLastName().equals(person.getLastName())) {
                return mr;
            }
        }

        return null;
    }

    // ===================== UPDATE =====================

    @Override
    public MedicalRecord updateMedicalRecord(MedicalRecord updatedMedicalRecord) {

        List<MedicalRecord> medicalRecords = dataRepository.getData().getMedicalrecords();

        for (MedicalRecord mr : medicalRecords) {

            if (mr.getFirstName().equals(updatedMedicalRecord.getFirstName())
                    && mr.getLastName().equals(updatedMedicalRecord.getLastName())) {

                mr.setBirthdate(updatedMedicalRecord.getBirthdate());
                mr.setMedications(updatedMedicalRecord.getMedications());
                mr.setAllergies(updatedMedicalRecord.getAllergies());

                log.info("updateMedicalRecord OK");

                return mr;
            }
        }

        log.warn("updateMedicalRecord NOT FOUND for {} {}",
                updatedMedicalRecord.getFirstName(),
                updatedMedicalRecord.getLastName());

        return null;
    }

    // ===================== DELETE =====================

    @Override
    public boolean deleteMedicalRecord(String firstName, String lastName) {

        List<MedicalRecord> medicalRecords = dataRepository.getData().getMedicalrecords();

        MedicalRecord toDelete = null;

        for (MedicalRecord mr : medicalRecords) {

            if (mr.getFirstName().equals(firstName)
                    && mr.getLastName().equals(lastName)) {

                toDelete = mr;
                break;
            }
        }

        if (toDelete != null) {
            medicalRecords.remove(toDelete);
            log.info("deleteMedicalRecord OK for {} {}", firstName, lastName);
            return true;
        }

        log.warn("deleteMedicalRecord NOT FOUND for {} {}", firstName, lastName);
        return false;
    }
}