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
@Builder
public class UnitDto {
    private UUID unitNumber;
    private String name;
    private String description;
    private Map<UUID, String> admins; //TODO MAP
    private Map<UUID, String> members;
    private long noOfMembers;
    private boolean isMember;
}
