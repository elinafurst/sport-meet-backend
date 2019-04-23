package se.elfu.sportprojectbackend.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class EventDto {
    private UUID eventNumber;
    private String name;
    private String description;
    private String sport;
    private String eventStartDate;
    private String eventStartTime;
    private int maxParticipants;
    private int noOfParticipants;
    private Map<UUID, String> participants;
    private Map<UUID, String> createdBy;
    private Map<UUID, String> byUnit;
    private boolean active;
    private String city;
    private String area;
    private String meetingPoint;
    private List<CommentDto> comments;

}
