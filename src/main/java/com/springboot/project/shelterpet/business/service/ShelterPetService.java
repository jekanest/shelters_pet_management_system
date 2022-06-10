package com.springboot.project.shelterpet.business.service;

import com.springboot.project.shelterpet.model.SheltersPet;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ShelterPetService {

    List<SheltersPet> findAllSheltersPet ();

    SheltersPet saveSheltersPet (SheltersPet sheltersPet) throws Exception;

    SheltersPet updateSheltersPet (SheltersPet sheltersPet) throws Exception;

    Optional<SheltersPet> findSheltersPetById(Long id);

    void deleteSheltersPetById(Long id);

    List<SheltersPet> findSheltersPetByType(String type);
}
