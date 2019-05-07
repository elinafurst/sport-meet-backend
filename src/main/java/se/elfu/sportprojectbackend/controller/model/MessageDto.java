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
public class MessageDto {

    private UUID messageNumber;
    private String message;
    private String timeStamp;
    private Map<UUID, String> author;
    private boolean isAuthorOfMessage;
    private Map<UUID, String> reader;
    private boolean isRead;
}
