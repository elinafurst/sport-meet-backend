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

    @NotBlank(message = "Name missing")
    private String name;
    private String description;
    @NotBlank(message = "Sport missing")
    private String sport;
    @NotBlank(message = "Startdate missing")
    private String eventStartDate;
    @NotBlank(message = "Starttime missing")
    private String eventStartTime;
    private UUID byUnit;
    @NotBlank(message = "City missing")
    private String city;
    @NotBlank(message = "Area missing")
    private String area;
    @NotBlank(message = "Meetingpoint missing")
    private String meetingPoint;
}
