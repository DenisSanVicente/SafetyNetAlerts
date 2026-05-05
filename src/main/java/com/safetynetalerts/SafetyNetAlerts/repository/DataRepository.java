package com.safetynetalerts.SafetyNetAlerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynetalerts.SafetyNetAlerts.model.SafetyNetData;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

@Repository
public class DataRepository {

    private SafetyNetData safetyNetData;

    public DataRepository() {
        loadData();
    }

    private void loadData() {
        try {
            ObjectMapper mapper = new ObjectMapper();

            safetyNetData = mapper.readValue(
                    new ClassPathResource("data.json").getInputStream(),
                    SafetyNetData.class
            );

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du chargement du JSON", e);
        }
    }

    public SafetyNetData getData() {
        return safetyNetData;
    }
}