package com.springboot.project.shelterpet.business.repository;

import com.springboot.project.shelterpet.business.repository.entity.SheltersPetDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SheltersPetRepository extends JpaRepository<SheltersPetDAO, Long> {
}
