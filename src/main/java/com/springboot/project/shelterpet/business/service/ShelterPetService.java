package com.springboot.project.shelterpet.business.service;

import com.springboot.project.shelterpet.model.SheltersPet;

import java.util.List;
import java.util.Optional;

public interface ShelterPetService {

    List<SheltersPet> findAllSheltersPet ();

    SheltersPet saveSheltersPet (SheltersPet sheltersPet);

    SheltersPet updateSheltersPet (SheltersPet sheltersPet);

    Optional<SheltersPet> findSheltersPetById(Long id);

    void deleteSheltersPetById(Long id);






}
