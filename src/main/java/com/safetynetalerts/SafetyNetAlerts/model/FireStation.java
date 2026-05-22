package com.safetynetalerts.SafetyNetAlerts.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter // LOMBOK
@Setter // LOMBOK
@NoArgsConstructor // LOMBOK -- Génère un constructeur sans paramètre
@AllArgsConstructor // LOMBOK -- Génère un constructeur avec paramètres
public class FireStation {

    private String address;
    private int station;
}