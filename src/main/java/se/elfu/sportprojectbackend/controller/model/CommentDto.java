package se.elfu.sportprojectbackend.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {

    private UUID commentNumber;
    private String comment;
    private String date;
    private String time;
    private Map<UUID, String> by;
    private boolean owner;

}
