package se.elfu.sportprojectbackend.controller.model.events.comments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreationDto {

    @NotBlank(message = "Comment cant be empty")
    private String comment;
}
