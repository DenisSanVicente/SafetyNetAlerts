package com.safetynetalerts.SafetyNetAlerts.service;

import com.safetynetalerts.SafetyNetAlerts.model.Persons;
import com.safetynetalerts.SafetyNetAlerts.repository.PersonsRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonsServiceImpl implements PersonsService {

    private PersonsRepository repository;

    public PersonsServiceImpl(PersonsRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Persons> getAllPersons() {
        return repository.findAll();
    }


}
