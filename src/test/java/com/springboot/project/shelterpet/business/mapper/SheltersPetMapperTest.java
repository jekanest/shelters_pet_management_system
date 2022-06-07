package com.springboot.project.shelterpet.business.mapper;

import com.springboot.project.shelterpet.business.repository.entity.SheltersPetDAO;
import com.springboot.project.shelterpet.model.Gender;
import com.springboot.project.shelterpet.model.SheltersPet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.springboot.project.shelterpet.model.Gender.MALE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SheltersPetMapperTest {

    private SheltersPetMapperImpl sheltersPetMapper;
    private SheltersPet sheltersPet;
    private SheltersPetDAO sheltersPetDAO;

    @BeforeEach
    public void setUp(){
        sheltersPetMapper = new SheltersPetMapperImpl();
        sheltersPet = createSheltersPet(1L,"Tom", "2022-02-01",4L,"2022-06-02", "cat", MALE,"adolescent", "Schedule one visit to the vet per year");
        sheltersPetDAO = createSheltersPetDAO(1L,"Tom", "2022-02-01",4L,"2022-06-02", "cat", MALE,"adolescent", "Schedule one visit to the vet per year");
    }

    @Test
    void sheltersPetToSheltersDAOTest () {
        sheltersPetMapper.sheltersPetToSheltersPetDAO(sheltersPet);
        assertEquals(sheltersPetDAO.getId(),sheltersPet.getId());
        assertEquals(sheltersPetDAO.getName(),sheltersPet.getName());
        assertEquals(sheltersPetDAO.getPetDateOfBirth(),sheltersPet.getPetDateOfBirth());
        assertEquals(sheltersPetDAO.getAge(),sheltersPet.getAge());
        assertEquals(sheltersPetDAO.getRegistrationDate(),sheltersPet.getRegistrationDate());
        assertEquals(sheltersPetDAO.getType(),sheltersPet.getType());
        assertEquals(sheltersPetDAO.getGender(),sheltersPet.getGender());
        assertEquals(sheltersPetDAO.getAgePhase(),sheltersPet.getAgePhase());
        assertEquals(sheltersPetDAO.getDescription(),sheltersPet.getDescription());
    }

    @Test
    void sheltersPetDAOToSheltersTest () {
        sheltersPetMapper.sheltersPetDAOToSheltersPet(sheltersPetDAO);
        assertEquals(sheltersPet.getId(),sheltersPetDAO.getId());
        assertEquals(sheltersPet.getName(),sheltersPetDAO.getName());
        assertEquals(sheltersPet.getPetDateOfBirth(),sheltersPetDAO.getPetDateOfBirth());
        assertEquals(sheltersPet.getAge(),sheltersPetDAO.getAge());
        assertEquals(sheltersPet.getRegistrationDate(),sheltersPetDAO.getRegistrationDate());
        assertEquals(sheltersPet.getType(),sheltersPetDAO.getType());
        assertEquals(sheltersPet.getGender(),sheltersPetDAO.getGender());
        assertEquals(sheltersPet.getAgePhase(),sheltersPetDAO.getAgePhase());
        assertEquals(sheltersPet.getDescription(),sheltersPetDAO.getDescription());
    }

    @Test
    void sheltersPetDAOToSheltersPetInvalidTest(){
        SheltersPetDAO sheltersPetDAO = sheltersPetMapper.sheltersPetToSheltersPetDAO(null);
        assertEquals(null, sheltersPetDAO);
    }

    @Test
    void sheltersPetToSheltersPetDAOInvalidTest(){
        SheltersPet sheltersPet = sheltersPetMapper.sheltersPetDAOToSheltersPet(null);
        assertEquals(null, sheltersPet);
    }

    private SheltersPet createSheltersPet(Long id, String name, String petDateOfBirth, Long age, String registrationDate, String type, Gender gender, String agePhase, String description) {
        SheltersPet sheltersPet = new SheltersPet();
        sheltersPet.setId(id);
        sheltersPet.setName(name);
        sheltersPet.setPetDateOfBirth(petDateOfBirth);
        sheltersPet.setAge(age);
        sheltersPet.setRegistrationDate(registrationDate);
        sheltersPet.setType(type);
        sheltersPet.setGender(gender);
        sheltersPet.setAgePhase(agePhase);
        sheltersPet.setDescription(description);
        return sheltersPet;
    }

    private SheltersPetDAO createSheltersPetDAO(Long id, String name, String petDateOfBirth, Long age, String registrationDate, String type, Gender gender, String agePhase, String description) {
        SheltersPetDAO sheltersPetDAO = new SheltersPetDAO();
        sheltersPetDAO.setId(id);
        sheltersPetDAO.setName(name);
        sheltersPetDAO.setPetDateOfBirth(petDateOfBirth);
        sheltersPetDAO.setAge(age);
        sheltersPetDAO.setRegistrationDate(registrationDate);
        sheltersPetDAO.setType(type);
        sheltersPetDAO.setGender(gender);
        sheltersPetDAO.setAgePhase(agePhase);
        sheltersPetDAO.setDescription(description);
        return sheltersPetDAO;
    }
}