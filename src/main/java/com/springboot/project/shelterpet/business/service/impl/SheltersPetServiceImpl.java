package com.springboot.project.shelterpet.business.service.impl;

import com.springboot.project.shelterpet.business.mapper.SheltersPetMapper;
import com.springboot.project.shelterpet.business.repository.SheltersPetRepository;
import com.springboot.project.shelterpet.business.repository.entity.SheltersPetDAO;
import com.springboot.project.shelterpet.model.SheltersPet;
import com.springboot.project.shelterpet.business.service.ShelterPetService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class SheltersPetServiceImpl implements ShelterPetService {

    @Autowired
    SheltersPetRepository sheltersPetRepository;

    @Autowired
    SheltersPetMapper sheltersPetMapper;

    @Cacheable(value = "sheltersPetList")
    @Override
    public List<SheltersPet> findAllSheltersPet() {
        List<SheltersPetDAO> shelterPetsList = sheltersPetRepository.findAll();
        log.info("Shelters pets list is received (size  is : {}",shelterPetsList::size);
        return shelterPetsList.stream().map(sheltersPetMapper::sheltersPetDAOToSheltersPet).collect(Collectors.toList());
    }

    @CacheEvict(cacheNames = "sheltersPetList", allEntries = true)
    @Override
    public SheltersPet saveSheltersPet(SheltersPet sheltersPet) {

        if (sheltersPetRepository.findAll().stream().anyMatch(p -> p.getName().equals(sheltersPet.getName())
                && p.getAge().equals(sheltersPet.getAge())
                && p.getRegistrationDate().equals(sheltersPet.getRegistrationDate())
                && p.getType().equals(sheltersPet.getType())
                && p.getGender().equals(sheltersPet.getGender())
        )) {
            log.error("Shelter pet data conflict exception is thrown: {}", HttpStatus.CONFLICT);
            throw new HttpClientErrorException(HttpStatus.CONFLICT);
        }
            sheltersPet.setAge(Long.valueOf(sheltersPet.calculateAgeOfTheShelterPet(LocalDate.parse(sheltersPet.getPetDateOfBirth()),
                LocalDate.now())));
            SheltersPetDAO sheltersPetSaved = sheltersPetRepository.save(sheltersPetMapper.sheltersPetToSheltersPetDAO(sheltersPet));
            log.info("New shelter pet is saved: {}", sheltersPetSaved);
        return sheltersPetMapper.sheltersPetDAOToSheltersPet(sheltersPetSaved);
    }

    @Override
    public SheltersPet updateSheltersPet(SheltersPet sheltersPet){
        sheltersPet.setAge(Long.valueOf(sheltersPet.calculateAgeOfTheShelterPet(LocalDate.parse(sheltersPet.getPetDateOfBirth()),
                LocalDate.now())));
        SheltersPetDAO sheltersPetDAOSaved = sheltersPetRepository.save(sheltersPetMapper.sheltersPetToSheltersPetDAO(sheltersPet));
        log.info("Shelter pet data is updated: {}", () -> sheltersPetDAOSaved);
        return sheltersPetMapper.sheltersPetDAOToSheltersPet(sheltersPetDAOSaved);
    }

    @Override
    public Optional<SheltersPet> findSheltersPetById(Long id) {
        Optional<SheltersPet> sheltersPetById = sheltersPetRepository.findById(id).
                flatMap(sheltersPet -> Optional.ofNullable(sheltersPetMapper.sheltersPetDAOToSheltersPet(sheltersPet)));
        log.info("Shelter pet with the id {} is {}", id, sheltersPetById);
        return sheltersPetById;
    }

    @Override
    public void deleteSheltersPetById(Long id) {
        sheltersPetRepository.deleteById(id);
        log.info("Shelter pet with the id {} is deleted", id);
    }
}
