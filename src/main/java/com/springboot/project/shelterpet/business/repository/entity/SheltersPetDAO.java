package com.springboot.project.shelterpet.business.repository.entity;

import com.springboot.project.shelterpet.model.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="shelters_pets")
public class SheltersPetDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pet_id")
    private Long id;

    @Column(name="pet_name")
    private String name;

    @Column(name="pet_age_in_months")
    private Long age;

    @Column(name="pet_registration_date")
    private String registrationDate;

    @Column(name="pet_type")
    private String type;

    @Column(name="pet_gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    public SheltersPetDAO(Long id) {
        this.id = id;
    }
}
