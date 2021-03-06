package se.elfu.sportprojectbackend.controller.model.units;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnitPreviewDto {
    private UUID unitNumber;
    private String name;
    private String description;
    private int noOfMembers;
    private Long noOfEvents;
}
