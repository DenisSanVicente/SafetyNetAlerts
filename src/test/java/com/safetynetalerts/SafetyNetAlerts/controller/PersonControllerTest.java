package com.safetynetalerts.SafetyNetAlerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynetalerts.SafetyNetAlerts.DTO.ChildAlertDTO;
import com.safetynetalerts.SafetyNetAlerts.DTO.PersonInfoDTO;
import com.safetynetalerts.SafetyNetAlerts.DTO.PhoneAlertDTO;
import com.safetynetalerts.SafetyNetAlerts.model.Person;
import com.safetynetalerts.SafetyNetAlerts.service.PersonService;
import com.safetynetalerts.SafetyNetAlerts.service.PhoneAlertService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonService personService;

    @MockBean
    private PhoneAlertService phoneAlertService;

    // 1. GET /persons
    @Test
    void shouldReturnAllPersons() throws Exception {

        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");

        when(personService.getAllPersons())
                .thenReturn(List.of(person));

        mockMvc.perform(get("/persons"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"));

        verify(personService).getAllPersons();
    }

    // 2. GET /childAlert
    @Test
    void shouldReturnChildrenByAddress() throws Exception {

        ChildAlertDTO child = new ChildAlertDTO();
        child.setFirstName("Tom");
        child.setLastName("Doe");
        child.setAge(10);

        when(personService.getChildrenByAddress("1509 Culver St"))
                .thenReturn(List.of(child));

        mockMvc.perform(get("/childAlert")
                        .param("address", "1509 Culver St"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].firstName").value("Tom"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].age").value(10));

        verify(personService).getChildrenByAddress("1509 Culver St");
    }

    // 3. GET /phoneAlert
    @Test
    void shouldReturnPhonesByStation() throws Exception {

        PhoneAlertDTO dto = new PhoneAlertDTO();
        dto.setPhones(List.of("123456789"));

        when(phoneAlertService.getPhonesByStation(1))
                .thenReturn(dto);

        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.phones[0]").value("123456789"));

        verify(phoneAlertService).getPhonesByStation(1);
    }

    // 4. GET /personInfolastName
    @Test
    void shouldReturnPersonInfoByLastName() throws Exception {

        PersonInfoDTO dto = new PersonInfoDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setAddress("1509 Culver St");
        dto.setAge(36);
        dto.setEmail("john.doe@mail.com");

        when(personService.getPersonInfoByLastName("Doe"))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/personInfolastName")
                        .param("lastName", "Doe"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].address").value("1509 Culver St"))
                .andExpect(jsonPath("$[0].age").value(36))
                .andExpect(jsonPath("$[0].email").value("john.doe@mail.com"));

        verify(personService).getPersonInfoByLastName("Doe");
    }

    // 5. GET /communityEmail
    @Test
    void shouldReturnEmailsByCity() throws Exception {

        when(personService.getEmailsByCity("Lille"))
                .thenReturn(List.of("john@mail.com"));

        mockMvc.perform(get("/communityEmail")
                        .param("city", "Lille"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").value("john@mail.com"));

        verify(personService).getEmailsByCity("Lille");
    }

    // 6. POST /person
    @Test
    void shouldAddPerson() throws Exception {

        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");

        when(personService.addPerson(any(Person.class)))
                .thenReturn(person);

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(personService).addPerson(any(Person.class));
    }

    // 7. PUT /person
    @Test
    void shouldUpdatePerson() throws Exception {

        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");

        when(personService.updatePerson(any(Person.class)))
                .thenReturn(person);

        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(personService).updatePerson(any(Person.class));
    }

    // 8. DELETE /person
    @Test
    void shouldDeletePerson() throws Exception {

        when(personService.deletePerson("John", "Doe"))
                .thenReturn(true);

        mockMvc.perform(delete("/person")
                        .param("firstName", "John")
                        .param("lastName", "Doe"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(personService).deletePerson("John", "Doe");
    }
}