package se.elfu.sportprojectbackend.controller.model.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class EventCreationDto {

    @NotBlank(message = "Namn saknas")
    private String name;
    private String description;
    @NotBlank(message = "Typ av sport saknas")
    private String sport;
    @NotBlank(message = "Startdatum saknas")
    private String eventStartDate;
    @NotBlank(message = "Starttid saknas")
    private String eventStartTime;
    private int maxParticipants;
    private UUID byUnit;
    @NotBlank(message = "Stad saknas")
    private String city;
    @NotBlank(message = "Område saknas")
    private String area;
    @NotBlank(message = "Mötesplats saknas")
    private String meetingPoint;
}
