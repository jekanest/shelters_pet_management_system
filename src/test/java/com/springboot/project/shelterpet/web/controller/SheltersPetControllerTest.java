package com.springboot.project.shelterpet.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.project.shelterpet.business.repository.SheltersPetRepository;
import com.springboot.project.shelterpet.business.service.ShelterPetService;
import com.springboot.project.shelterpet.model.Gender;
import com.springboot.project.shelterpet.model.SheltersPet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SheltersPetController.class)
class SheltersPetControllerTest {

    public final static String URLPost = "/api/pets";
    public final static String URLGet = "/api/";


    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private SheltersPetController sheltersPetController;

    @MockBean
    private ShelterPetService shelterPetService;

    @MockBean
    private SheltersPetRepository sheltersPetRepository;

    private SheltersPet sheltersPet;
    private List<SheltersPet> sheltersPetList;

    @BeforeEach
    public void beforeEach(){
        sheltersPet = createSheltersPet(1L,"Tom", 35L, "15-08-2021", "cat", MALE);
        sheltersPetList = createSheltersPetList(sheltersPet);
    }

    @Test
    void getAllSheltersPetsTest() throws Exception{
        when(shelterPetService.findAllSheltersPet()).thenReturn(sheltersPetList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URLGet))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Tom"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(35L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].registrationDate").value("15-08-2021"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].type").value("cat"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("MALE"))
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
                        .get(URLGet))
                .andExpect(status().isNotFound());
    }

    @Test
    void postSheltersPetCorrectParametersTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .post(URLPost)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(sheltersPet))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void postSheltersPetInvalidIdTest() throws Exception{
        sheltersPet.setId(-1L);
        mockMvc.perform(MockMvcRequestBuilders
                        .post(URLPost)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(sheltersPet))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postSheltersPetInvalidNameTest() throws Exception{
        sheltersPet.setName("4t?");
        mockMvc.perform(MockMvcRequestBuilders
                        .post(URLPost)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(sheltersPet))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postSheltersPetEmptyNameTest() throws Exception{
        sheltersPet.setName("");
        mockMvc.perform(MockMvcRequestBuilders
                        .post(URLPost)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(sheltersPet))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postSheltersInvalidAgeTest() throws Exception{
        sheltersPet.setAge(-1L);
        mockMvc.perform(MockMvcRequestBuilders
                        .post(URLPost)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(sheltersPet))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postSheltersPetEmptyRegistrationDateTest() throws Exception{
        sheltersPet.setRegistrationDate("");
        mockMvc.perform(MockMvcRequestBuilders
                        .post(URLPost)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(sheltersPet))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postSheltersPetEmptyTypeTest() throws Exception{
        sheltersPet.setType("");
        mockMvc.perform(MockMvcRequestBuilders
                        .post(URLPost)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(sheltersPet))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postSheltersPetInvalidTypeTest() throws Exception{
        sheltersPet.setType("12345");
        mockMvc.perform(MockMvcRequestBuilders
                        .post(URLPost)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(sheltersPet))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

//    @Test
//    void postSheltersPetInvalidGenderTest() throws Exception{
//        sheltersPet.setGender(null);
//        mockMvc.perform(MockMvcRequestBuilders
//                        .post(URLPost)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(sheltersPet))
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }

    @Test
    void getSheltersPetByCorrectIdTest() throws Exception {
        Optional<SheltersPet> shelterPet = Optional.of(sheltersPet);
        when(shelterPetService.findSheltersPetById(anyLong())).thenReturn(shelterPet);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URLGet + "/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Tom"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(35L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.registrationDate").value("15-08-2021"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("cat"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("MALE"))
                .andExpect(status().isOk()).
                andDo(MockMvcResultHandlers.print());

        verify(shelterPetService, times(1)).findSheltersPetById(anyLong());


    }

    @Test
    void getSheltersPetByInvalidIdTest() throws Exception {
//        Optional<SheltersPet> shelterPet = Optional.of(sheltersPet);
//        shelterPet.get().setId(0L);
//
//        when(shelterPetService.findSheltersPetById(sheltersPet.getId())).thenReturn(Optional.empty());
//
//        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
//                        .get(URLGet + "/0")
//                        .content(asJsonString(shelterPet))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
//
//        verify(shelterPetService, times(0)).findSheltersPetById(0L);

        when(shelterPetService.findSheltersPetById(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(get(URLGet + anyLong()))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateSheltersPetByIdCorrectParametersTest() throws Exception {

//        when(shelterPetService.findSheltersPetById(sheltersPet.getId())).thenReturn(Optional.of(sheltersPet));
// when(shelterPetService.findSheltersPetById(anyLong())).thenReturn(Optional.of(sheltersPet));
//        mockMvc.perform(MockMvcRequestBuilders
//                        .put(URLGet + "/1")
//                        .content(asJsonString(sheltersPet))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
//                .andExpect(status().isCreated());
//
//        verify(shelterPetService, times(1)).saveSheltersPet(sheltersPet);



//        when(shelterPetService.findSheltersPetById(anyLong())).thenReturn(Optional.of(getType(1L, "test1")));
//        mockMvc.perform(MockMvcRequestBuilders
//                        .put(URLGet + "/")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(getType(1L, "test1")))
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());

        when(shelterPetService.findSheltersPetById(sheltersPet.getId())).thenReturn(Optional.of(sheltersPet));
        sheltersPet.setName("New Name");
        mockMvc.perform(MockMvcRequestBuilders
                        .put(URLGet + "/1")
                        .content(asJsonString(sheltersPet))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("New Name"))
                .andExpect(status().isCreated());

        verify(shelterPetService, times(1)).saveSheltersPet(sheltersPet);


    }

    @Test
    void updateSheltersPetByIdInvalidParametersTest() throws Exception {
        sheltersPet.setId(0L);
        when(sheltersPetRepository.existsById(anyLong())).thenReturn(false);
        ResultActions mvcRes = mockMvc.perform(MockMvcRequestBuilders
                        .put(URLGet + "/0")
                        .content(asJsonString(sheltersPet))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));

        verify(shelterPetService, times(0)).saveSheltersPet(sheltersPet);
    }

    @Test
    void deleteSheltersPetByIdCorrectParametersTest() throws Exception {
        when(shelterPetService.findSheltersPetById(anyLong())).thenReturn(Optional.of(sheltersPet));
        mockMvc.perform(delete(URLGet + "/1"))
                .andExpect(status().isNoContent());

        verify(shelterPetService, times(1)).deleteSheltersPetById(anyLong());

    }

        @Test
        void deleteSheltersPetByIdInvalidParametersTest() throws Exception {
            when(sheltersPetRepository.existsById(anyLong())).thenReturn(false);
            mockMvc.perform(delete(URLGet + "/1"))
                    .andExpect(status().is(404));

            verify(shelterPetService, times(0)).deleteSheltersPetById(anyLong());

        }

    private SheltersPet createSheltersPet (Long id, String name, Long age, String registrationDate, String type, Gender gender) {
        SheltersPet sheltersPet = new SheltersPet();
        sheltersPet.setId(id);
        sheltersPet.setName(name);
        sheltersPet.setAge(age);
        sheltersPet.setRegistrationDate(registrationDate);
        sheltersPet.setType(type);
        sheltersPet.setGender(gender);
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