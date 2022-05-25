package com.springboot.project.shelterpet.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@ApiModel(description= "Model of SheltersPet data")
@Data
@NoArgsConstructor
@Component
public class SheltersPet {

    @ApiModelProperty(notes = "Unique shelters pets id")
    @Min(value = 1)
    @Max(value = Long.MAX_VALUE)
    @NonNull
    private Long id;

    @ApiModelProperty(notes = "Shelters pets name")
    @NonNull
    @Pattern(regexp = "^[a-zA-Z0-9\\s]+$", message ="This field may contain only letters, numbers and spaces")
    private String name;

    @ApiModelProperty(notes = "Shelters pets age")
    @NonNull
    private Long age;

    @ApiModelProperty(notes = "Pets registration date in a shelter")
    @NonNull
    private String registrationDate;

    @ApiModelProperty(notes = "Shelters pets type")
    @NonNull
    private String type;

    @ApiModelProperty(notes = "Shelters pets gender")
    @NonNull
    private String gender;
}
