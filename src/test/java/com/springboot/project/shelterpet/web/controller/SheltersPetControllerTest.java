package com.springboot.project.shelterpet.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.project.shelterpet.business.service.ShelterPetService;
import com.springboot.project.shelterpet.model.Gender;
import com.springboot.project.shelterpet.model.SheltersPet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.springboot.project.shelterpet.model.Gender.MALE;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SheltersPetController.class)
class SheltersPetControllerTest {

    public final static String URL1 = "/api/v2/pets";
    public final static String URL2 = "/api/v2/pet";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    KieSession kieSession;

    @Autowired
    private SheltersPetController sheltersPetController;

    @MockBean
    private ShelterPetService shelterPetService;

    private SheltersPet sheltersPet;
    private List<SheltersPet> sheltersPetList;

    @BeforeEach
    public void beforeEach(){
        sheltersPet = createSheltersPet(1L,"Tom", "2022-02-01",8L,"2022-06-02", "cat",
                MALE, "adolescent", "Schedule one visit to the vet per year");
        sheltersPetList = createSheltersPetList(sheltersPet);
    }

    @Test
    void getAllSheltersPetsTest() throws Exception{
        when(shelterPetService.findAllSheltersPet()).thenReturn(sheltersPetList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Tom"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].petDateOfBirth").value("2022-02-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(8L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].registrationDate").value("2022-06-02"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].type").value("cat"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("MALE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].agePhase").value("adolescent"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Schedule one visit to the vet per year"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(shelterPetService).findAllSheltersPet();
        verify(shelterPetService, times(1)).findAllSheltersPet();
    }

    @Test
    void getAllSheltersPetsEmptyDataTest() throws Exception{
        sheltersPetList.clear();

        when(shelterPetService.findAllSheltersPet()).thenReturn(sheltersPetList);
        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL1))
                .andExpect(status().isNotFound());
    }

    @Test
    void postSheltersPetCorrectParametersTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(sheltersPet))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());

        verify(shelterPetService,times(1)).saveSheltersPet(sheltersPet);
    }

    @Test
    void postSheltersPetInvalidIdTest() throws Exception{
        sheltersPet.setId(-1L);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(sheltersPet))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postSheltersPetInvalidNameTest() throws Exception{
        sheltersPet.setName("4t?");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(sheltersPet))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postSheltersPetEmptyNameTest() throws Exception{
        sheltersPet.setName("");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(sheltersPet))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postSheltersEmptyDateOfBirthTest() throws Exception{
        sheltersPet.setPetDateOfBirth("");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(sheltersPet))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postSheltersPetEmptyRegistrationDateTest() throws Exception{
        sheltersPet.setRegistrationDate("");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(sheltersPet))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postSheltersPetEmptyTypeTest() throws Exception{
        sheltersPet.setType("");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(sheltersPet))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postSheltersPetInvalidTypeTest() throws Exception{
        sheltersPet.setType("12345");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(sheltersPet))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getSheltersPetByCorrectIdTest() throws Exception {
        when(shelterPetService.findSheltersPetById(1L)).thenReturn(Optional.of(sheltersPet));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL2 + "/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Tom"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.petDateOfBirth").value("2022-02-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(8L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.registrationDate").value("2022-06-02"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("cat"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("MALE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.agePhase").value("adolescent"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Schedule one visit to the vet per year"))
                .andExpect(status().isOk()).
                andDo(MockMvcResultHandlers.print());

        verify(shelterPetService, times(1)).findSheltersPetById(1L);
    }

    @Test
    void getSheltersPetByInvalidIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL2 + "/0")
                        .content(asJsonString(sheltersPet))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(shelterPetService, times(0)).findSheltersPetById(null);
    }

    @Test
    void getSheltersPetByCorrectTypeTest() throws Exception {
        when(shelterPetService.findSheltersPetByType("cat")).thenReturn(sheltersPetList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL2 + "/list/cat"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Tom"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].petDateOfBirth").value("2022-02-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(8L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].registrationDate").value("2022-06-02"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].type").value("cat"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("MALE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].agePhase").value("adolescent"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Schedule one visit to the vet per year"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(shelterPetService, times(1)).findSheltersPetByType("cat");
    }

    @Test
    void getSheltersPetByInvalidTypeTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL2 + "/list/mouse")
                        .content(asJsonString(sheltersPet))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(shelterPetService, times(0)).findSheltersPetByType(null);
    }

    @Test
    void updateSheltersPetByIdCorrectParametersTest() throws Exception {
        when(shelterPetService.findSheltersPetById(sheltersPet.getId())).thenReturn(Optional.of(sheltersPet));

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL2 + "/1")
                        .content(asJsonString(sheltersPet))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());

        verify(shelterPetService, times(1)).updateSheltersPet(sheltersPet);
    }

    @Test
    void updateSheltersPetByIdInvalidParametersTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL2 + "/0")
                        .content(asJsonString(sheltersPet))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));

        verify(shelterPetService, times(0)).saveSheltersPet(sheltersPet);
    }

    @Test
    void deleteSheltersPetByIdCorrectParametersTest() throws Exception {
        when(shelterPetService.findSheltersPetById(anyLong())).thenReturn(Optional.of(sheltersPet));
        mockMvc.perform(delete(URL2 + "/1"))
                .andExpect(status().isNoContent());

        verify(shelterPetService, times(1)).deleteSheltersPetById(anyLong());
    }

    @Test
    void deleteSheltersPetByIdInvalidParametersTest() throws Exception {
        mockMvc.perform(delete(URL2 + "/0"))
                .andExpect(status().is(404));

        verify(shelterPetService, times(0)).deleteSheltersPetById(0L);
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

    private List<SheltersPet> createSheltersPetList(SheltersPet sheltersPet) {
        List<SheltersPet> sheltersPetList = new ArrayList<>();
        sheltersPetList.add(sheltersPet);
        sheltersPetList.add(sheltersPet);
        sheltersPetList.add(sheltersPet);
        return sheltersPetList;
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}