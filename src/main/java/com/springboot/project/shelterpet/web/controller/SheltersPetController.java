package com.springboot.project.shelterpet.web.controller;

import com.springboot.project.shelterpet.model.SheltersPet;
import com.springboot.project.shelterpet.business.service.ShelterPetService;
import com.springboot.project.shelterpet.swagger.DescriptionVariables;
import com.springboot.project.shelterpet.swagger.HTMLResponseMessages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api(tags = DescriptionVariables.SHELTERSPET)
@Log4j2
@RestController
@RequestMapping("/api")
public class SheltersPetController {

    @Autowired
    ShelterPetService shelterPetService;

    @ApiOperation(value = "Gets all shelters pets",
            notes = "Get a list of all shelters pets",
            response = SheltersPet.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })

    @Cacheable(cacheNames = "shelters_pets")
    @GetMapping()
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
        log.error("Training type has error: {}", bindingResult);
        return ResponseEntity.badRequest().build();
    }
        SheltersPet sheltersPetSaved= shelterPetService.saveSheltersPet(sheltersPet);
        log.info("New training type is created: {}", sheltersPetSaved);
        return new ResponseEntity<>(sheltersPetSaved, HttpStatus.CREATED);
}

}
