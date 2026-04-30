package com.safetynetalerts.SafetyNetAlerts.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity // JAKARTA -- Crée une table dans la BDD
@Getter // LOMBOK
@Setter // LOMBOK
@AllArgsConstructor // LOMBOK -- Constructeur pour tous les attributs
@NoArgsConstructor // LOMBOK -- Constructeur vide
public class Person {

    @Id // JAKARTA -- Sert à définir la clé primaire de la table (Souvent utilisé avec @GeneratedValue)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // JAKARTA -- La valeur de la clé primaire est générée automatiquement
    private Integer id; // Integer au lieu de int car Integer est un objet (wrapper) et peut être null

    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;
}
