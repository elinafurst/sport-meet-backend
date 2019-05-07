package se.elfu.sportprojectbackend.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private UUID userNumber;
    private String firstname;
    private String lastname;
    @NotBlank(message = "Användarnamn saknas")
    private String username;
    private String description;
}
