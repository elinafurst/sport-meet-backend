package se.elfu.sportprojectbackend.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationDto {

    @NotBlank(message = "Stad saknas")
    private String city;
    private Set<@NotBlank(message = "Områden saknas") String> areas;
}
