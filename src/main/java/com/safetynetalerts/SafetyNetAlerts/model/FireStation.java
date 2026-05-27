package com.safetynetalerts.SafetyNetAlerts.model;

import lombok.*;

@Getter // LOMBOK
@Setter // LOMBOK
@NoArgsConstructor // LOMBOK -- Génère un constructeur sans paramètre
@AllArgsConstructor // LOMBOK -- Génère un constructeur avec paramètres
public class FireStation {

    private String address;
    private int station;
}