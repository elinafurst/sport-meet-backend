package se.elfu.sportprojectbackend.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class EventCreationDto {

    private String name;
    private String description;
    private String sport;
    private String eventStartDate;
    private String eventStartTime;
    private int maxParticipants;
    private UUID byUnit;
    private String city;
    private String area;
    private String meetingPoint;
}
