DROP TABLE IF EXISTS shelters_pets;

CREATE TABLE shelters_pets (
   pet_id BIGINT AUTO_INCREMENT NOT NULL,
   pet_name VARCHAR(255),
   pet_date_of_birth VARCHAR(255)
   pet_age_in_months BIGINT,
   pet_registration_date VARCHAR(255),
   pet_type VARCHAR(255),
   pet_gender VARCHAR(255),
   pet_age_phase VARCHAR(255),
   description VARCHAR(255),

   CONSTRAINT pk_shelters_pets PRIMARY KEY (pet_id)
);