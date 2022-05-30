package com.springboot.project.shelterpet.web.controller;

import com.springboot.project.shelterpet.model.SheltersPet;
import com.springboot.project.shelterpet.business.service.ShelterPetService;
import com.springboot.project.shelterpet.swagger.DescriptionVariables;
import com.springboot.project.shelterpet.swagger.HTMLResponseMessages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.HTML;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Api(tags = DescriptionVariables.SHELTERSPET)
@Log4j2
@RestController
@RequestMapping("/api")
public class SheltersPetController {

    @Autowired
    ShelterPetService shelterPetService;

    @GetMapping("/")
    @ApiOperation(value = "Gets all shelters pets",
            notes = "Get a list of all shelters pets",
            response = SheltersPet.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    public ResponseEntity<List<SheltersPet>> getAllSheltersPets() {
        List<SheltersPet> sheltersPets = shelterPetService.findAllSheltersPet();
        if (sheltersPets.isEmpty()) {
            log.warn("Shelters pets list is empty! {}", sheltersPets);
            return ResponseEntity.notFound().build();
        }
        log.info("Shelters pets list is found. Size: {}", sheltersPets::size);
        return ResponseEntity.ok(sheltersPets);
    }

    @PostMapping("/pets")
    @ApiOperation(value = "Saves shelters pets data in database",
            notes = "If provided valid shelters pets data, saves it",
            response = SheltersPet.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = HTMLResponseMessages.HTTP_201),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 409, message = HTMLResponseMessages.HTTP_409),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SheltersPet> postSheltersPet(@Valid @RequestBody SheltersPet sheltersPet, BindingResult bindingResult) throws Exception{
        if (bindingResult.hasErrors()) {
        log.error("New shelters pets data has an error: {}", bindingResult);
        return ResponseEntity.badRequest().build();
    }
        SheltersPet sheltersPetSaved= shelterPetService.saveSheltersPet(sheltersPet);
        log.info("New shelters pet is created: {}", sheltersPetSaved);
        return new ResponseEntity<>(sheltersPetSaved, HttpStatus.CREATED);
}

    @GetMapping("/{id}")
    @ApiOperation(value = "Find shelters pet by provided id",
            notes = "Provide an id to search specific pet in the database",
            response = SheltersPet.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500),
    })
    public ResponseEntity<SheltersPet> getSheltersPetById (@ApiParam(value = "id of the pet", required = true)
                                                           @NonNull @PathVariable Long id){
        log.info("Find shelters pet by passing id, where pets id is : {}", id);
        Optional<SheltersPet> sheltersPet = (shelterPetService.findSheltersPetById(id));
        if(!sheltersPet.isPresent()){
            log.warn("Shelters pet with id {} is not found.", id);
        }else{
            log.info("Shelters pet with id {} is found: {}", id, sheltersPet);
        }
        return sheltersPet.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete shelters pet by provided id",
            notes = "Deletes the shelters pet iif provided id exists",
            response = SheltersPet.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = HTMLResponseMessages.HTTP_204_WITHOUT_DATA),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteSheltersPetById(@ApiParam(value = "Required non-null value", required = true)
                                                      @NonNull @PathVariable Long id){
        Optional<SheltersPet> sheltersPet = shelterPetService.findSheltersPetById(id);
        if (!sheltersPet.isPresent()) {
            log.warn("Shelters pet data with id {} for delete in not found", id);
            return ResponseEntity.notFound().build();
        }
        shelterPetService.deleteSheltersPetById(id);
        log.info("Shelters pet data record with id {} is deleted", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Updates the shelters pets data by id",
            notes = "Updates the shelters pets data if provided id exists",
            response = SheltersPet.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = HTMLResponseMessages.HTTP_201),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500) })
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<SheltersPet> putSheltersPetById(@ApiParam(value = "id of the shelters pet", required = true)
                                                          @NonNull  @PathVariable Long id,
                                                          @Valid @RequestBody SheltersPet sheltersPet, BindingResult bindingResult) {
        log.info("Update existing shelters pet with id: {} and new body: {}", id, sheltersPet);
        if (bindingResult.hasErrors() || id <= 0 || !id.equals(sheltersPet.getId())) {
            log.warn("Shelters pet with id {} for update is not found", id);
            return ResponseEntity.notFound().build();
        }
        shelterPetService.saveSheltersPet(sheltersPet);
        log.info("Shelters pet with id {} is updated: {}", id, sheltersPet);
        return new ResponseEntity<>(sheltersPet, HttpStatus.CREATED);
    }
}
