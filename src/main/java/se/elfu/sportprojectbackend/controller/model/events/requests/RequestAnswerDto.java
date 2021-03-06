package se.elfu.sportprojectbackend.controller.model.events.requests;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestAnswerDto {
    private boolean isApproved;
}
