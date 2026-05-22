package com.safetynetalerts.SafetyNetAlerts.controller;

import com.safetynetalerts.SafetyNetAlerts.model.MedicalRecord;
import com.safetynetalerts.SafetyNetAlerts.service.MedicalRecordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService service) {

        this.medicalRecordService = service;
    }

    @GetMapping
    public List<MedicalRecord> getAllMedicalRecords() {

        return medicalRecordService.getAllMedicalRecords();
    }

    /// ===== ETAPE 3 - ENDPOINT 3 ===== ///
    @PostMapping
    public MedicalRecord addMedicalRecord(
            @RequestBody MedicalRecord medicalRecord) {

        return medicalRecordService.addMedicalRecord(medicalRecord);
    }

    @PutMapping
    public MedicalRecord updateMedicalRecord(
            @RequestBody MedicalRecord medicalRecord) {

        return medicalRecordService.updateMedicalRecord(medicalRecord);
    }

    @DeleteMapping
    public boolean deleteMedicalRecord(
            @RequestParam String firstName,
            @RequestParam String lastName) {

        return medicalRecordService.deleteMedicalRecord(firstName, lastName);
    }
}