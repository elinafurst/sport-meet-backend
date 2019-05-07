package se.elfu.sportprojectbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.elfu.sportprojectbackend.controller.model.events.requests.RequestCreationDto;
import se.elfu.sportprojectbackend.controller.params.Param;
import se.elfu.sportprojectbackend.service.RequestService;

import java.util.UUID;

@RestController
@RequestMapping("requests")
@Slf4j
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping("events/{eventNumber}")
    public ResponseEntity joinEventRequest(@PathVariable("eventNumber") UUID eventNumber, @RequestBody RequestCreationDto requestCreationDto){
        log.info("Join event request {} ", eventNumber);
        return new ResponseEntity(requestService.joinEventRequest(eventNumber, requestCreationDto), HttpStatus.CREATED);
    }


    @PutMapping("{requestNumber}/messages")
    public ResponseEntity saveMessageForRequest(@PathVariable("requestNumber") UUID requestNumber, @RequestBody RequestCreationDto requestCreationDto){
        log.info("Send message {} ", requestCreationDto);
        requestService.saveMessageForRequest(requestNumber, requestCreationDto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{requestNumber}")
    public ResponseEntity answerRequest(@PathVariable("requestNumber") UUID requestNumber, @RequestBody boolean isApproved){
        log.info("Request answer {} ", isApproved);
        requestService.answerRequest(requestNumber, isApproved);
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity getEventRequest(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                          @RequestParam(name = "size", defaultValue = "6", required = false) int size) {
        Param param = Param.builder()
                .page(page)
                .size(size)
                .build();
        log.info("Get event requests {} ");
        return ResponseEntity.ok(requestService.getEventRequests(param));
    }

    @PutMapping("{requestNumber}/reader")
    public ResponseEntity readRequest(@PathVariable("requestNumber") UUID requestNumber){
        log.info("Put read request ");
        requestService.readRequest(requestNumber);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("poll")
    public ResponseEntity getNewRequestAndMessages(){
        log.info("Get undread requests {} ");
        return ResponseEntity.ok(requestService.getNewRequests());
    }

    @GetMapping("{requestNumber}")
    public ResponseEntity getEventRequest(@PathVariable("requestNumber") UUID requestNumber){
        log.info("Get event request {} ");
        return ResponseEntity.ok(requestService.getEventRequest(requestNumber));
    }
}
