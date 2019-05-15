package se.elfu.sportprojectbackend.service.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import se.elfu.sportprojectbackend.repository.model.Event;
import se.elfu.sportprojectbackend.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class Scheduler {

    private final EventService eventService;

    public Scheduler(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Background job that runs every x minutes, calls method that inactive events that are about to "expire"
     */
    @Scheduled(fixedRate = 60000)
    public void updateExpiredEvents() {
        log.info("Update passed events {}", LocalDateTime.now());
        long updatedEvents = eventService.checkIfEventIsAboutToExpire();
        log.info("Updated events {}", updatedEvents);

    }
}
