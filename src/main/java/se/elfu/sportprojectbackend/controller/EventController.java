package se.elfu.sportprojectbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.elfu.sportprojectbackend.controller.model.events.comments.CommentCreationDto;
import se.elfu.sportprojectbackend.controller.model.events.EventCreationDto;
import se.elfu.sportprojectbackend.controller.params.Param;
import se.elfu.sportprojectbackend.service.EventService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping()
@Slf4j
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("events")
    public ResponseEntity createEvent(@Valid @RequestBody EventCreationDto eventCreationDto){
        log.info("Create event {} ", eventCreationDto);
        return new ResponseEntity(eventService.createEvent(eventCreationDto), HttpStatus.CREATED);
    }

    @PutMapping("events/{eventNumber}")
    public ResponseEntity updateEvent(@Valid @RequestBody EventCreationDto eventCreationDto,@PathVariable("eventNumber")  UUID eventNumber){
        log.info("Update event {} ", eventCreationDto);
        return ResponseEntity.ok(eventService.updateEvent(eventNumber, eventCreationDto));
    }

    @GetMapping("events/sports")
    public ResponseEntity getSports(){
        log.info("Get sports");
        return ResponseEntity.ok(eventService.getSports());
    }

    @GetMapping("events/locations/{city}")
    public ResponseEntity getAreasForCity(@PathVariable("city") String city){
        log.info("Get locations for city {} ", city);
        return ResponseEntity.ok(eventService.getAreasForCity(city));
    }

    @GetMapping("events/{eventNumber}")
    public ResponseEntity getEvent(@PathVariable("eventNumber") UUID eventNumber){
        log.info("Get event {} ", eventNumber);
        return ResponseEntity.ok(eventService.getEvent(eventNumber));
    }

    @PostMapping("events/{eventNumber}/comments")
    public ResponseEntity createCommentEvent(@PathVariable("eventNumber") UUID eventNumber, @Valid @RequestBody CommentCreationDto commentCreationDto){
        log.info("Create comment {} for event {} ", commentCreationDto, eventNumber);
        return new ResponseEntity(eventService.createCommentEvent(eventNumber, commentCreationDto), HttpStatus.CREATED);
    }

    @GetMapping("events/{eventNumber}/comments")
    public ResponseEntity getCommentsForEvent(@PathVariable("eventNumber") UUID eventNumber,
                                              @RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                              @RequestParam(name = "size", defaultValue = "10", required = false) int size){
        Param param = Param.builder()
                .page(page)
                .size(size)
                .build();
        log.info("GET comments for event {}, params: {} ", eventNumber, param);

        return ResponseEntity.ok(eventService.getCommentsForEvent(eventNumber, param));
    }

    @DeleteMapping("events/comments/{commentNumber}")
    public ResponseEntity deleteCommentForActiveUser(@PathVariable("commentNumber") UUID commentNumber){
        log.info("Delete comment {} ", commentNumber);
        eventService.deleteCommentForActiveUser(commentNumber);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("events/{eventNumber}/creator")
    public ResponseEntity cancelEvent(@PathVariable("eventNumber") UUID eventNumber){
        log.info("Cancel event {} ", eventNumber);
        eventService.cancelEvent(eventNumber);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("events/{eventNumber}/requests")
    public ResponseEntity cancelRequest(@PathVariable("eventNumber")UUID eventNumber){
        log.info("Cancel request {} ", eventNumber);
        eventService.cancelRequest(eventNumber);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("open/events")
    public ResponseEntity getEvents(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                    @RequestParam(name = "size", defaultValue = "6", required = false) int size,
                                    @RequestParam(name = "fromDate", required = false) String fromDate,
                                    @RequestParam(name = "toDate", required = false) String toDate,
                                    @RequestParam(name = "type", required = false) String type,
                                    @RequestParam(name = "city", required = false) String city,
                                    @RequestParam(name = "area", required = false) String area) {
        Param param = Param.builder()
                .page(page)
                .size(size)
                .fromDate(fromDate)
                .toDate(toDate)
                .type(type)
                .city(city)
                .area(area)
                .build();

        log.info("GET events, param {}", param);
        return ResponseEntity.ok(eventService.getEvents(param));
    }

    @GetMapping("events/units")
    public ResponseEntity getEventsForUnitsUserFollows(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                    @RequestParam(name = "size", defaultValue = "18", required = false) int size) {
        Param param = Param.builder()
                .page(page)
                .size(size)
                .build();
        log.info("GET events for units user follows, param {}", param);

        return ResponseEntity.ok(eventService.getEventsForUnitsUserFollows(param));
    }

    @GetMapping("events/creator")
    public ResponseEntity getEventsCreatorOf(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                             @RequestParam(name = "size", defaultValue = "6", required = false) int size,
                                             @RequestParam(name = "active", defaultValue = "1", required = false) boolean active) {
        Param param = Param.builder()
                .page(page)
                .size(size)
                .active(active)
                .build();
        log.info("GET events creator of active: {}", active);

        return ResponseEntity.ok(eventService.getEventsCreatorOf(param));
    }

}
