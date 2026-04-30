package com.safetynetalerts.SafetyNetAlerts.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity // JAKARTA -- Crée une table dans la BDD
@Getter // LOMBOK -- Getter pour tous les attributs
@Setter
@AllArgsConstructor // LOMBOK -- Constructeur pour tous les attributs
@NoArgsConstructor // LOMBOK --Constructeur vide
public class FireStation {

    @Id // JAKARTA -- Définit la clé primaire de la table
    @GeneratedValue(strategy = GenerationType.IDENTITY) // JAKARTA -- La valeur de la clé est générée automatiquement
    private Integer id;

    private String address;
    private int station;
}
