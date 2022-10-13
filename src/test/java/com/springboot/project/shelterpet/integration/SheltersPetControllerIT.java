package com.springboot.project.shelterpet.integration;

import com.springboot.project.shelterpet.SheltersPetApplication;
import com.springboot.project.shelterpet.business.service.ShelterPetService;
import com.springboot.project.shelterpet.model.SheltersPet;
import org.json.JSONException;
import org.junit.jupiter.api.*;
import org.kie.api.runtime.KieSession;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static com.springboot.project.shelterpet.model.Gender.MALE;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = SheltersPetApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SheltersPetControllerIT {

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Test
    @Order(1)
    void getAllPetsIT() throws JSONException {
        String expected = "[\n" +
                "    {\n" +
                "        \"id\": 1,\n" +
                "        \"name\": \"Lucky\",\n" +
                "        \"petDateOfBirth\": \"2022-09-01\",\n" +
                "        \"age\": 1,\n" +
                "        \"registrationDate\": \"2022-09-22\",\n" +
                "        \"type\": \"dog\",\n" +
                "        \"gender\": \"FEMALE\",\n" +
                "        \"agePhase\": \"pup\",\n" +
                "        \"description\": \"Follow the list of required vaccination\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": 2,\n" +
                "        \"name\": \"Pirate\",\n" +
                "        \"petDateOfBirth\": \"2020-08-01\",\n" +
                "        \"age\": 26,\n" +
                "        \"registrationDate\": \"2021-09-15\",\n" +
                "        \"type\": \"dog\",\n" +
                "        \"gender\": \"MALE\",\n" +
                "        \"agePhase\": \"productive years\",\n" +
                "        \"description\": \"Schedule one visit to the vet per year\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": 3,\n" +
                "        \"name\": \"Minka\",\n" +
                "        \"petDateOfBirth\": \"2018-03-01\",\n" +
                "        \"age\": 55,\n" +
                "        \"registrationDate\": \"2020-05-06\",\n" +
                "        \"type\": \"cat\",\n" +
                "        \"gender\": \"FEMALE\",\n" +
                "        \"agePhase\": \"productive years\",\n" +
                "        \"description\": \"Schedule one visit to the vet per year\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": 4,\n" +
                "        \"name\": \"Brave\",\n" +
                "        \"petDateOfBirth\": \"2013-10-01\",\n" +
                "        \"age\": 108,\n" +
                "        \"registrationDate\": \"2019-03-09\",\n" +
                "        \"type\": \"dog\",\n" +
                "        \"gender\": \"FEMALE\",\n" +
                "        \"agePhase\": \"productive years\",\n" +
                "        \"description\": \"Schedule one visit to the vet per year\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": 5,\n" +
                "        \"name\": \"Zara\",\n" +
                "        \"petDateOfBirth\": \"2022-01-01\",\n" +
                "        \"age\": 9,\n" +
                "        \"registrationDate\": \"2022-06-02\",\n" +
                "        \"type\": \"dog\",\n" +
                "        \"gender\": \"FEMALE\",\n" +
                "        \"agePhase\": \"pup\",\n" +
                "        \"description\": \"Follow the list of required vaccination\"\n" +
                "    }\n" +
                "]";

        ResponseEntity<String> response = restTemplate.getForEntity(
                createURLWithPort("/api/v2/pets"),
                String.class);

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    @Order(2)
    void getPetByIdIT() throws JSONException {
        String expected = "{\"id\":1,\"name\":\"Lucky\",\"petDateOfBirth\":\"2022-09-01\",\"age\":1," +
                "\"registrationDate\":\"2022-09-22\",\"type\":\"dog\",\"gender\":\"FEMALE\",\"agePhase\":\"pup\"," +
                "\"description\":\"Follow the list of required vaccination\"}";

        ResponseEntity<String> response = restTemplate.getForEntity(
                createURLWithPort("/api/v2/pet/1"),
                String.class);

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    @Order(3)
    void getPetByTypeIT() throws JSONException {
        String expected = "[{\"id\":3,\"name\":\"Minka\",\"petDateOfBirth\":\"2018-03-01\"," +
                "\"age\":55,\"registrationDate\":\"2020-05-06\",\"type\":\"cat\",\"gender\":\"FEMALE\"," +
                "\"agePhase\":\"productive years\",\"description\":\"Schedule one visit to the vet per year\"}]";

        ResponseEntity<String> response = restTemplate.getForEntity(
                createURLWithPort("/api/v2/pet/list/cat"),
                String.class);

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    @Order(4)
    void postPetIT() throws JSONException {
        SheltersPet sheltersPet = new SheltersPet(6L, "Tom", "2015-08-01", 86L, "2021-05-15", "cat",
                MALE, "productive years", "Schedule one visit to the vet per year");

        String expected = "{\"id\":6,\"name\":\"Tom\",\"petDateOfBirth\":\"2015-08-01\",\"age\":86," +
                "\"registrationDate\":\"2021-05-15\",\"type\":\"cat\",\"gender\":\"MALE\",\"agePhase\":\"productive years\"," +
                "\"description\":\"Schedule one visit to the vet per year\"}";

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SheltersPet> entity = new HttpEntity<>(sheltersPet, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                createURLWithPort("/api/v2/pet"),
                entity, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    @Order(5)
    void updateSheltersPetIT() throws JSONException {
        SheltersPet sheltersPet = new SheltersPet(6L, "Sam", "2022-04-18", 5L, "2022-09-14", "dog",
                MALE, "pup", "Follow the list of required vaccination");

        String expected = "{\"id\":6,\"name\":\"Sam\",\"petDateOfBirth\":\"2022-04-18\",\"age\":5," +
                "\"registrationDate\":\"2022-09-14\",\"type\":\"dog\",\"gender\":\"MALE\",\"agePhase\":\"pup\"," +
                "\"description\":\"Follow the list of required vaccination\"}";

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SheltersPet> request = new HttpEntity<>(sheltersPet, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/v2/pet/6"),
                HttpMethod.PUT,
                request, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    @Order(6)
    void deleteSheltersPetIT() throws JSONException {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<String> responseEntity = this.restTemplate.exchange(
                createURLWithPort("/api/v2/pet/6"),
                HttpMethod.DELETE,
                entity, String.class);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
