package com.springboot.project.shelterpet.business.service;

import com.springboot.project.shelterpet.model.SheltersPet;

import java.util.List;

public interface ShelterPetService {

    List<SheltersPet> findAllSheltersPet ();

    SheltersPet saveSheltersPet (SheltersPet sheltersPet);
}
