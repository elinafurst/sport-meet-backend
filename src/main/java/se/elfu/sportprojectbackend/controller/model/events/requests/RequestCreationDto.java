package se.elfu.sportprojectbackend.controller.model.events.requests;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestCreationDto {

    private String message;
}
