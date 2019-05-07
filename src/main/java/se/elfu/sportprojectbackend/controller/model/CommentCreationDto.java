package se.elfu.sportprojectbackend.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreationDto {

    @NotBlank(message = "Kommentarsfältet kan inte va tomt")
    private String comment;
}
