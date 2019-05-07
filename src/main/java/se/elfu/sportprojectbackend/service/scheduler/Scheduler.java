package se.elfu.sportprojectbackend.service.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import se.elfu.sportprojectbackend.service.EventService;

import java.time.LocalDateTime;

@Component
@Slf4j
public class Scheduler {

    private final EventService eventService;

    public Scheduler(EventService eventService) {
        this.eventService = eventService;
    }

    @Scheduled(fixedRate = 60000)//300000)
    public void updateExpiredEvents() {
        log.info("Update passed events {}", LocalDateTime.now());
        eventService.updateExpiredEvents();
    }
}
