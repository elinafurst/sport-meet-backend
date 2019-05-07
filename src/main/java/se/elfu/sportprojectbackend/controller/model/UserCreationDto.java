package se.elfu.sportprojectbackend.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreationDto {

    private String firstname;
    private String lastname;
    @NotBlank(message = "Användarnamn saknas")
    private String username;
    private String description;
    @NotBlank(message = "Email saknas")
    private String email;
    @NotBlank(message = "Lösenord saknas")
    private String password;
}
