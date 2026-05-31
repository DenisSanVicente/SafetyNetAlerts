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

    /**
     * Récupère tous les dossiers médicaux.
     *
     * @return la liste de tous les dossiers médicaux
     */
    @Override
    public List<MedicalRecord> getAllMedicalRecords() {

        log.info("Request received: getAllMedicalRecords()");

        try {

            List<MedicalRecord> medicalRecords =
                    dataRepository.getData().getMedicalrecords();

            log.info(
                    "{} medical records retrieved",
                    medicalRecords.size()
            );

            return medicalRecords;

        } catch (Exception e) {

            log.error("Error while retrieving medical records", e);
            throw e;
        }
    }


    /**
     * Récupère le dossier médical associé à une personne à partir de son prénom et de son nom.
     *
     * @param person la personne recherchée
     * @return le dossier médical correspondant ou null si aucune personne n'est trouvée
     */
    @Override
    public MedicalRecord getMedicalRecordByPerson(Person person) {

        log.debug(
                "Searching medical record for {} {}",
                person.getFirstName(),
                person.getLastName()
        );

        for (MedicalRecord mr : dataRepository.getData().getMedicalrecords()) {
            if (mr.getFirstName().equals(person.getFirstName())
                    && mr.getLastName().equals(person.getLastName())) {

                log.info(
                        "Medical record found for {} {}",
                        person.getFirstName(),
                        person.getLastName()
                );

                return mr;
            }
        }

        log.warn(
                "No medical record found for {} {}",
                person.getFirstName(),
                person.getLastName()
        );

        return null;
    }


    // ===================== CRUD =====================

    /**
     * Ajoute un nouveau dossier médical.
     *
     * @param medicalRecord le dossier médical à ajouter
     * @return le dossier médical ajouté
     */
    @Override
    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {

        log.debug(
                "Adding medical record for {} {}",
                medicalRecord != null ? medicalRecord.getFirstName() : "null",
                medicalRecord != null ? medicalRecord.getLastName() : "null"
        );

        if (medicalRecord == null) {
            log.error("Attempt to add null medical record");
            throw new IllegalArgumentException("Medical record cannot be null");
        }

        List<MedicalRecord> medicalRecords = dataRepository.getData().getMedicalrecords();

        medicalRecords.add(medicalRecord);

        log.info(
                "Medical record added for {} {}",
                medicalRecord.getFirstName(),
                medicalRecord.getLastName()
        );

        return medicalRecord;
    }


    /**
     * Met à jour un dossier médical existant en utilisant le prénom et le nom.
     * <p>
     * Si un dossier médical est trouvé, met à jour la date de naissance, les allergies et les médicaments.
     *
     * @param updatedMedicalRecord le dossier médical avec les nouvelles informations
     * @return le dossier médical mis à jour, ou null si aucun dossier n'est trouvé
     */
    @Override
    public MedicalRecord updateMedicalRecord(MedicalRecord updatedMedicalRecord) {

        log.debug(
                "Updating medical record for {} {}",
                updatedMedicalRecord.getFirstName(),
                updatedMedicalRecord.getLastName()
        );

        List<MedicalRecord> medicalRecords = dataRepository.getData().getMedicalrecords();

        for (MedicalRecord mr : medicalRecords) {

            if (mr.getFirstName().equals(updatedMedicalRecord.getFirstName())
                    && mr.getLastName().equals(updatedMedicalRecord.getLastName())) {

                mr.setBirthdate(updatedMedicalRecord.getBirthdate());
                mr.setMedications(updatedMedicalRecord.getMedications());
                mr.setAllergies(updatedMedicalRecord.getAllergies());

                log.info(
                        "Medical record updated for {} {}",
                        mr.getFirstName(),
                        mr.getLastName()
                );

                return mr;
            }
        }

        log.warn(
                "updateMedicalRecord NOT FOUND for {} {}",
                updatedMedicalRecord.getFirstName(),
                updatedMedicalRecord.getLastName()
        );

        return null;
    }


    /**
     * Supprime un dossier médical identifié par le prénom et le nom.
     *
     * @param firstName le prénom de la personne
     * @param lastName  le nom de la personne
     * @return true si le dossier médical est supprimé, false s'il n'est pas trouvé
     */
    @Override
    public boolean deleteMedicalRecord(String firstName, String lastName) {

        log.debug(
                "Attempting to delete medical record for {} {}",
                firstName,
                lastName
        );

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

            log.info(
                    "deleteMedicalRecord OK for {} {}",
                    firstName,
                    lastName
            );

            return true;
        }

        log.warn(
                "deleteMedicalRecord NOT FOUND for {} {}",
                firstName,
                lastName
        );

        return false;
    }
}