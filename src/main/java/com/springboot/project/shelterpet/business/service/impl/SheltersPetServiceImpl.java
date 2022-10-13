package com.springboot.project.shelterpet.business.service.impl;

import com.springboot.project.shelterpet.business.mapper.SheltersPetMapper;
import com.springboot.project.shelterpet.business.repository.SheltersPetRepository;
import com.springboot.project.shelterpet.business.repository.entity.SheltersPetDAO;
import com.springboot.project.shelterpet.model.SheltersPet;
import com.springboot.project.shelterpet.business.service.ShelterPetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class SheltersPetServiceImpl implements ShelterPetService {

    private final SheltersPetRepository sheltersPetRepository;

    private final SheltersPetMapper sheltersPetMapper;

    @Cacheable(value = "sheltersPetList")
    @Scheduled(fixedDelay = 300000)
    @Override
    public List<SheltersPet> findAllSheltersPet() {
        List<SheltersPetDAO> shelterPetsList = sheltersPetRepository.findAll();
        log.info("Shelters pets list is received. Size  is : {}",shelterPetsList::size);
        return shelterPetsList.stream().map(sheltersPetMapper::sheltersPetDAOToSheltersPet).collect(Collectors.toList());
    }

    @CacheEvict(cacheNames = "sheltersPetList", allEntries = true)
    @Override
    public SheltersPet saveSheltersPet(SheltersPet sheltersPet) throws Exception {
        if (anyMatch(sheltersPet)) {
            log.error("Shelter pet data conflict exception is thrown: {}", HttpStatus.CONFLICT);
            throw new HttpClientErrorException(HttpStatus.CONFLICT);
        }
            checkDateOfBirth(LocalDate.parse(sheltersPet.getPetDateOfBirth()), LocalDate.now());
            sheltersPet.setAge((long) calculateAgeOfTheShelterPet(LocalDate.parse(sheltersPet.getPetDateOfBirth()),
                    LocalDate.now()));
            SheltersPetDAO sheltersPetSaved = sheltersPetRepository.save(sheltersPetMapper.sheltersPetToSheltersPetDAO(sheltersPet));

            log.info("New shelter pet is saved: {}", sheltersPetSaved);
        return sheltersPetMapper.sheltersPetDAOToSheltersPet(sheltersPetSaved);
    }

    @CacheEvict(cacheNames = "sheltersPetList", allEntries = true)
    @Override
    public SheltersPet updateSheltersPet(SheltersPet sheltersPet) throws Exception {
        checkDateOfBirth(LocalDate.parse(sheltersPet.getPetDateOfBirth()), LocalDate.now());
        sheltersPet.setAge((long) calculateAgeOfTheShelterPet(LocalDate.parse(sheltersPet.getPetDateOfBirth()),
                LocalDate.now()));
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

    @CacheEvict(cacheNames = "sheltersPetList", allEntries = true)
    @Override
    public void deleteSheltersPetById(Long id) {
        sheltersPetRepository.deleteById(id);
        log.info("Shelter pet with the id {} is deleted", id);
    }

    @Override
    public List<SheltersPet> findSheltersPetByType(String type) {
        List<SheltersPetDAO> sheltersPetByTypeList= sheltersPetRepository.findAll();
        log.info("Shelter pet by type {} is {}", type, sheltersPetByTypeList);
            return sheltersPetByTypeList.stream().filter(byType ->
                    byType.getType().equalsIgnoreCase(type)).map(sheltersPetMapper::sheltersPetDAOToSheltersPet)
                    .collect(Collectors.toList());
    }

    public static void checkDateOfBirth(LocalDate dateOfBirth, LocalDate currentDate) throws Exception{
        if(currentDate.isBefore(dateOfBirth)){
            throw new Exception("Please check date of birth, it should be before current date");
        }
    }

    public int calculateAgeOfTheShelterPet(LocalDate dateOfBirth, LocalDate currentDate) {
        Period calculateAgeOfTheShelterPet = Period.between(dateOfBirth, currentDate);
        return (calculateAgeOfTheShelterPet.getYears()*12) + (calculateAgeOfTheShelterPet.getMonths());
    }

    public boolean anyMatch (SheltersPet sheltersPet) {
        return (sheltersPetRepository.findAll().stream().
                anyMatch(p -> p.getType().equals(sheltersPet.getType())
                        && p.getPetDateOfBirth().equals(sheltersPet.getPetDateOfBirth())
                        && p.getRegistrationDate().equals(sheltersPet.getRegistrationDate())
                        && p.getName().equals(sheltersPet.getName())
                        && p.getGender().equals(sheltersPet.getGender())
                ));
    }
}
