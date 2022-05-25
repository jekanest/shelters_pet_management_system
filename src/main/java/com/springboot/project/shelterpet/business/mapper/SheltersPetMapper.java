package com.springboot.project.shelterpet.business.mapper;

import com.springboot.project.shelterpet.business.repository.entity.SheltersPetDAO;
import com.springboot.project.shelterpet.model.SheltersPet;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface SheltersPetMapper {

    SheltersPetDAO sheltersPetToSheltersPetDAO (SheltersPet sheltersPet);

    SheltersPet sheltersPetDAOToSheltersPet (SheltersPetDAO shelterPetDAO);



}
