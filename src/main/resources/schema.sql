DROP TABLE IF EXISTS shelters_pets;
CREATE TABLE shelters_pets (
   pet_id BIGINT AUTO_INCREMENT NOT NULL,
   pet_name VARCHAR(50),
   pet_age_in_months BIGINT,
   pet_registration_date VARCHAR(20),
   pet_type VARCHAR(50),
   pet_gender VARCHAR(50),
   PRIMARY KEY (pet_id)
);