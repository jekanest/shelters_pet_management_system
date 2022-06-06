package com.springboot.project.shelterpet.business.service.impl;

import com.springboot.project.shelterpet.business.mapper.SheltersPetMapper;
import com.springboot.project.shelterpet.business.repository.SheltersPetRepository;
import com.springboot.project.shelterpet.business.repository.entity.SheltersPetDAO;
import com.springboot.project.shelterpet.model.Gender;
import com.springboot.project.shelterpet.model.SheltersPet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.springboot.project.shelterpet.model.Gender.MALE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class SheltersPetServiceImplTest {

    @InjectMocks
    private SheltersPetServiceImpl sheltersPetService;

    @Mock
    private SheltersPetRepository sheltersPetRepository;

    @Mock
    private SheltersPetMapper sheltersPetMapper;
    private SheltersPet sheltersPet;
    private SheltersPetDAO sheltersPetDAO;
    private List<SheltersPetDAO> sheltersPetDAOList;

    @BeforeEach
    private void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    public void beforeEach(){
        sheltersPet = createSheltersPet(1L,"Tom", "2022-02-01",4L,"2022-06-02", "cat", MALE);
        sheltersPetDAO = createSheltersPetDAO(1L,"Tom", "2022-02-01",4L,"2022-06-02", "cat", MALE);
        sheltersPetDAOList = createSheltersPetDAOList(sheltersPetDAO);
    }

    @Test
    void findAllSheltersPetTest() {
        when(sheltersPetRepository.findAll()).thenReturn(sheltersPetDAOList);
        when(sheltersPetMapper.sheltersPetDAOToSheltersPet(sheltersPetDAO)).thenReturn(sheltersPet);
        List<SheltersPet> sheltersPetList = sheltersPetService.findAllSheltersPet();
        assertEquals(3,sheltersPetList.size());
        verify(sheltersPetRepository, times(1)).findAll();
    }

    @Test
    void findSheltersPetEmptyListTest() {
        when(sheltersPetRepository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(sheltersPetService.findAllSheltersPet().isEmpty());
        verify(sheltersPetRepository, times(1)).findAll();
    }

    @Test
    void saveSheltersPetCorrectParametersTest() {
        when(sheltersPetRepository.save(any(SheltersPetDAO.class))).thenReturn(sheltersPetDAO);
        when(sheltersPetMapper.sheltersPetToSheltersPetDAO(sheltersPet)).thenReturn(sheltersPetDAO);
        when(sheltersPetMapper.sheltersPetDAOToSheltersPet(sheltersPetDAO)).thenReturn(sheltersPet);
        SheltersPet sheltersPetSaved = sheltersPetService.saveSheltersPet(sheltersPet);
        assertEquals(sheltersPet, sheltersPetSaved);
        verify(sheltersPetRepository, times(1)).save(sheltersPetDAO);
    }

    @Test
    void saveSheltersPetInvalidParametersTest() {
        when(sheltersPetRepository.save(sheltersPetDAO)).thenThrow(new IllegalArgumentException());
        when(sheltersPetMapper.sheltersPetToSheltersPetDAO(sheltersPet)).thenReturn(sheltersPetDAO);
        assertThrows(IllegalArgumentException.class, () -> sheltersPetService.saveSheltersPet(sheltersPet));
        verify(sheltersPetRepository).save(sheltersPetDAO);
    }

    @Test
    void saveSheltersPetParametersConflictTest() {
        SheltersPet sheltersPetSaved = createSheltersPet(1L,"Tom", "2022-02-01",4L,"2022-06-02", "cat", MALE);
        when(sheltersPetRepository.findAll()).thenReturn(sheltersPetDAOList);
        assertThrows(HttpClientErrorException.class, () -> sheltersPetService.saveSheltersPet(sheltersPetSaved));
        verify(sheltersPetRepository, times(0)).save(sheltersPetDAO);
    }

    @Test
    void findSheltersPetByCorrectIdTest() {
        when(sheltersPetRepository.findById(anyLong())).thenReturn(Optional.of(sheltersPetDAO));
        when(sheltersPetMapper.sheltersPetDAOToSheltersPet(sheltersPetDAO)).thenReturn(sheltersPet);
        Optional<SheltersPet> sheltersPetFound = sheltersPetService.findSheltersPetById(sheltersPet.getId());
        assertEquals(sheltersPet.getId(), sheltersPetFound.get().getId());
        assertEquals(sheltersPet.getName(), sheltersPetFound.get().getName());
        assertEquals(sheltersPet.getPetDateOfBirth(), sheltersPetFound.get().getPetDateOfBirth());
        assertEquals(sheltersPet.getRegistrationDate(), sheltersPetFound.get().getRegistrationDate());
        assertEquals(sheltersPet.getType(), sheltersPetFound.get().getType());
        assertEquals(sheltersPet.getGender(), sheltersPetFound.get().getGender());
        verify(sheltersPetRepository, times(1)).findById(anyLong());
    }

    @Test
    void findSheltersPetByInvalidIdTest() {
        when(sheltersPetRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertFalse(sheltersPetService.findSheltersPetById(anyLong()).isPresent());
        verify(sheltersPetRepository, times(1)).findById(anyLong());
    }

    @Test
    void updateSheltersPetByCorrectIdTest(){
        when(sheltersPetRepository.save(sheltersPetDAO)).thenReturn(sheltersPetDAO);
        when(sheltersPetMapper.sheltersPetToSheltersPetDAO(sheltersPet)).thenReturn(sheltersPetDAO);
        when(sheltersPetMapper.sheltersPetDAOToSheltersPet(sheltersPetDAO)).thenReturn(sheltersPet);
        SheltersPet sheltersPetUpdated= sheltersPetService.updateSheltersPet(sheltersPet);
        assertEquals(sheltersPet, sheltersPetUpdated);
        verify(sheltersPetRepository, times(1)).save(sheltersPetDAO);
    }

    @Test
    void updateSheltersPetByInvalidIdTest() {
        when(sheltersPetRepository.save(sheltersPetDAO)).thenThrow(new IllegalArgumentException());
        when(sheltersPetMapper.sheltersPetToSheltersPetDAO(sheltersPet)).thenReturn(sheltersPetDAO);
        Assertions.assertThrows(IllegalArgumentException.class, () -> sheltersPetService.updateSheltersPet(sheltersPet));
        verify(sheltersPetRepository, times(1)).save(sheltersPetDAO);
    }

    @Test
    void deleteSheltersPetByCorrectIdTest() {
        sheltersPetService.deleteSheltersPetById(anyLong());
        verify(sheltersPetRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteSheltersPetByInvalidIdTest() {
        doThrow(new IllegalArgumentException()).when(sheltersPetRepository).deleteById(anyLong());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> sheltersPetService.deleteSheltersPetById(anyLong()));
    }

    private SheltersPet createSheltersPet(Long id, String name, String petDateOfBirth, Long Age, String registrationDate, String type, Gender gender) {
        SheltersPet sheltersPet = new SheltersPet();
        sheltersPet.setId(id);
        sheltersPet.setName(name);
        sheltersPet.setPetDateOfBirth(petDateOfBirth);
        sheltersPet.setAge(Long.valueOf(sheltersPet.calculateAgeOfTheShelterPet(LocalDate.parse(sheltersPet.getPetDateOfBirth()),
                LocalDate.now())));
        sheltersPet.setRegistrationDate(registrationDate);
        sheltersPet.setType(type);
        sheltersPet.setGender(gender);
        return sheltersPet;
    }

    private SheltersPetDAO createSheltersPetDAO(Long id, String name, String petDateOfBirth, Long age, String registrationDate, String type, Gender gender) {
        SheltersPetDAO sheltersPetDAO = new SheltersPetDAO();
        sheltersPetDAO.setId(id);
        sheltersPetDAO.setName(name);
        sheltersPetDAO.setPetDateOfBirth(petDateOfBirth);
        sheltersPetDAO.setAge(age);
        sheltersPetDAO.setRegistrationDate(registrationDate);
        sheltersPetDAO.setType(type);
        sheltersPetDAO.setGender(gender);
        return sheltersPetDAO;
    }

    private List<SheltersPetDAO> createSheltersPetDAOList(SheltersPetDAO sheltersPetDAO) {
        List<SheltersPetDAO> sheltersPetDAOList = new ArrayList<>();
        sheltersPetDAOList.add(sheltersPetDAO);
        sheltersPetDAOList.add(sheltersPetDAO);
        sheltersPetDAOList.add(sheltersPetDAO);
        return sheltersPetDAOList;
    }





}