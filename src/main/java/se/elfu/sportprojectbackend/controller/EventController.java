package se.elfu.sportprojectbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.elfu.sportprojectbackend.controller.model.CommentCreationDto;
import se.elfu.sportprojectbackend.controller.model.EventCreationDto;
import se.elfu.sportprojectbackend.controller.parm.Param;
import se.elfu.sportprojectbackend.service.EventService;

import java.util.UUID;

@RestController
@RequestMapping("events")
@Slf4j
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody EventCreationDto eventCreationDto){
        log.info("CREATE event {} ", eventCreationDto);
        return new ResponseEntity(eventService.createEvent(eventCreationDto), HttpStatus.CREATED);
    }

    @PostMapping("sports")
    //TODO autorize admin
    public ResponseEntity createSport(@RequestBody String sport){
        log.info("CREATE sport {} ", sport);
        return new ResponseEntity(eventService.createSport(sport), HttpStatus.CREATED);
    }

    @GetMapping("sports")
    //TODO autorize user
    public ResponseEntity getSports(){
        log.info("GET sports {} ");
        return ResponseEntity.ok(eventService.getSports());
    }

    @GetMapping("{eventNumber}")
    public ResponseEntity getEvent(@PathVariable("eventNumber") UUID eventNumber){
        log.info("GET event {} ", eventNumber);
        return ResponseEntity.ok(eventService.getEvent(eventNumber));
    }

    @PostMapping("{eventNumber}/comments")
    public ResponseEntity createCommentEvent(@PathVariable("eventNumber") UUID eventNumber, @RequestBody CommentCreationDto commentCreationDto){
        log.info("POST Comment event {} ", eventNumber);
        return new ResponseEntity(eventService.createCommentEvent(eventNumber, commentCreationDto), HttpStatus.CREATED);
    }

    @GetMapping("{eventNumber}/comments")
    public ResponseEntity getCommentsForEvent(@PathVariable("eventNumber") UUID eventNumber){
        log.info("GET comments for event {} ", eventNumber);
        Param param = Param.builder()
                .page(0)
                .size(10)
                .build();
        return ResponseEntity.ok(eventService.getCommentsForEvent(eventNumber, param));
    }

    @DeleteMapping("comments/{commentNumber}")
    public ResponseEntity deleteCommentForActiveUser(@PathVariable("commentNumber") UUID commentNumber){
        log.info("DELETE comment {} ", commentNumber);
        eventService.deleteCommentForActiveUser(commentNumber);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{eventNumber}/users")
    public ResponseEntity joinEvent(@PathVariable("eventNumber")UUID eventNumber){
        log.info("Join event {} ", eventNumber);
        eventService.joinEvent(eventNumber);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{eventNumber}/creator") // TODO better api path
    public ResponseEntity cancelEvent(@PathVariable("eventNumber")UUID eventNumber){
        log.info("Cancel event {} ", eventNumber);
        eventService.cancelEvent(eventNumber);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{eventNumber}/users")
    public ResponseEntity leaveEvent(@PathVariable("eventNumber")UUID eventNumber){
        log.info("Leave event {} ", eventNumber);
        eventService.leaveEvent(eventNumber);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity getEvents(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                    @RequestParam(name = "size", defaultValue = "18", required = false) int size,
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
        log.info("GET events");
        return ResponseEntity.ok(eventService.getEvents(param));
    }
}
