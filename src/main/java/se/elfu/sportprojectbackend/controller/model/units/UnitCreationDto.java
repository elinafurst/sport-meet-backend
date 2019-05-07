package se.elfu.sportprojectbackend.controller.model.units;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnitCreationDto {

    @NotBlank(message = "Namn saknas")
    private String name;
    private String description;
}
