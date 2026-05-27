package com.safetynetalerts.SafetyNetAlerts.service;

import com.safetynetalerts.SafetyNetAlerts.model.MedicalRecord;
import com.safetynetalerts.SafetyNetAlerts.model.Person;

import java.util.List;

public interface MedicalRecordService {

    List<MedicalRecord> getAllMedicalRecords();

    MedicalRecord getMedicalRecordByPerson(Person person);

    /// ===== ETAPE 3 - ENDPOINT 3 ===== ///
    // POST - Ajout
    MedicalRecord addMedicalRecord(MedicalRecord medicalRecord);

    // PUT - Update
    MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord);

    // DELETE - Supprimer
    boolean deleteMedicalRecord(String firstName, String lastName);

}