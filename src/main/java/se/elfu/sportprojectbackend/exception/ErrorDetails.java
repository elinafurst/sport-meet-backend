package se.elfu.sportprojectbackend.exception;

import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ErrorDetails {
    private LocalDateTime timestamp;
    private String message;
    private List<String> details;

    public ErrorDetails(LocalDateTime timestamp, String message, List<String> details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

}
