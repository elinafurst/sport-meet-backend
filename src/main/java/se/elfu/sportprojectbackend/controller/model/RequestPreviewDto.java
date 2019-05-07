package se.elfu.sportprojectbackend.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.elfu.sportprojectbackend.repository.model.RequestStatus;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestPreviewDto {

    private UUID requestNumber;
    private RequestStatus requestStatus;
    private Map<UUID, String> event;
    private Map<UUID, String> sender;
    private Map<UUID, String> receiver;
    private MessageDto message;
    private boolean isRequester;
    private boolean isRead;
}
