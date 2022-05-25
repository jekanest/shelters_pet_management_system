package com.springboot.project.shelterpet.business.service.impl;

import com.springboot.project.shelterpet.business.mapper.SheltersPetMapper;
import com.springboot.project.shelterpet.business.repository.SheltersPetRepository;
import com.springboot.project.shelterpet.business.repository.entity.SheltersPetDAO;
import com.springboot.project.shelterpet.model.SheltersPet;
import com.springboot.project.shelterpet.business.service.ShelterPetService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class SheltersPetServiceImpl implements ShelterPetService {

    @Autowired
    SheltersPetRepository sheltersPetRepository;

    @Autowired
    SheltersPetMapper sheltersPetMapper;

    @Override
    public List<SheltersPet> findAllSheltersPet() {
        List<SheltersPetDAO> shelterPetsList = sheltersPetRepository.findAll();
        return shelterPetsList.stream().map(sheltersPetMapper::sheltersPetDAOToSheltersPet).collect(Collectors.toList());
    }

    @Override
    public SheltersPet saveSheltersPet(SheltersPet sheltersPet) {

        if (sheltersPetRepository.findAll().stream().anyMatch(p -> p.getName().equalsIgnoreCase(sheltersPet.getName())
                && p.getType().equalsIgnoreCase(sheltersPet.getType()))) {
            log.info("There is the pet with the same name, we advise you to chose other name");
        }
            SheltersPetDAO sheltersPetSaved = sheltersPetRepository.save(sheltersPetMapper.sheltersPetToSheltersPetDAO(sheltersPet));
            log.info("New pet is saved: {}", sheltersPetSaved);
        return sheltersPetMapper.sheltersPetDAOToSheltersPet(sheltersPetSaved);
    }
}
