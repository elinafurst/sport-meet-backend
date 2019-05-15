package se.elfu.sportprojectbackend.controller.model.users;

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
    @NotBlank(message = "Username missing")
    private String username;
    private String description;
    @NotBlank(message = "Email missing")
    private String email;
    @NotBlank(message = "Password missing")
    private String password;
}
