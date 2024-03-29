package com.springboot.project.shelterpet.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@ApiModel(description= "Model of SheltersPet data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SheltersPet implements Serializable {

    @ApiModelProperty(notes = "Unique shelter pet id")
    @Min(value = 1)
    @Max(value = Long.MAX_VALUE)
    @NonNull
    private Long id;

    @ApiModelProperty(notes = "Shelter pet name")
    @NotBlank
    @NonNull
    @Pattern(regexp = "^[a-zA-Z0-9\\s]+$", message ="This field may contain only letters, numbers and spaces")
    private String name;

    @ApiModelProperty(notes = "Date of birth of the pet")
    @NonNull
    @NotBlank
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonFormat(pattern = "Yyyy-MM-dd")
    private String petDateOfBirth;

    @ApiModelProperty(notes = "Shelter pet age")
    @Min(value = 0)
    private Long age;

    @ApiModelProperty(notes = "Pet registration date in a shelter")
    @NotBlank
    @NonNull
    private String registrationDate;

    @ApiModelProperty(notes = "Shelter pet type")
    @NotBlank
    @NonNull
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message ="This field may contain only letters and spaces")
    private String type;

    @ApiModelProperty(notes = "Shelter pet gender")
    @NonNull
    private Gender gender;

    @ApiModelProperty(notes = "Shelter pet living phase by type and age")
    private String agePhase;

    @ApiModelProperty(notes = "Notes for shelter pet care")
    private String description;

}