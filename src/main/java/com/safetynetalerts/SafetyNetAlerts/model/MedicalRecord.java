package com.safetynetalerts.SafetyNetAlerts.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity // JAKARTA -- Crée une table dans la BDD
@Getter // LOMBOK -- Getter sur tous les attributs
@Setter
@AllArgsConstructor // LOMBOK -- Constructeur pour tous les attributs
@NoArgsConstructor // LOMBOK -- Constructeur vide
public class MedicalRecord {

    @Id // JAKARTA -- Définit la clé primaire de la table
    @GeneratedValue(strategy = GenerationType.IDENTITY) // JAKARTA -- La valeur de la clé primaire est définie automatiquement
    private Integer id;

    private String firstName;
    private String lastName;
    private String birthdate;

    @ElementCollection // JAKARTA -- Liste de valeurs simples à stocker en base dans une table séparée
    private List<String> medications;

    @ElementCollection
    private List<String> allergies;
}