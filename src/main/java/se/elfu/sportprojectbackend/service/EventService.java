package se.elfu.sportprojectbackend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.elfu.sportprojectbackend.controller.model.*;
import se.elfu.sportprojectbackend.controller.model.events.EventCreationDto;
import se.elfu.sportprojectbackend.controller.model.events.EventDto;
import se.elfu.sportprojectbackend.controller.model.events.comments.CommentCreationDto;
import se.elfu.sportprojectbackend.controller.model.events.comments.CommentDto;
import se.elfu.sportprojectbackend.controller.params.Param;
import se.elfu.sportprojectbackend.exception.customException.BadRequestException;
import se.elfu.sportprojectbackend.repository.*;
import se.elfu.sportprojectbackend.repository.model.*;
import se.elfu.sportprojectbackend.service.helper.EntityRepositoryHelper;
import se.elfu.sportprojectbackend.service.helper.FilterRepositoryHelper;
import se.elfu.sportprojectbackend.utils.Validator;
import se.elfu.sportprojectbackend.utils.converter.CommentConverter;
import se.elfu.sportprojectbackend.utils.converter.EventConverter;
import se.elfu.sportprojectbackend.utils.DateTimeParser;
import se.elfu.sportprojectbackend.utils.converter.RequestConverter;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class EventService {

    @Autowired
    private Validator validator;

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final CommentRepository commentRepository;
    private final SportRepository sportRepository;
    private final RequestRepository requestRepository;
    private final MessageRepository messageRepository;
    private final FilterRepositoryHelper filterRepositoryHelper;
    private final EntityRepositoryHelper entityRepositoryHelper;

    public EventService(EventRepository eventRepository, LocationRepository locationRepository, CommentRepository commentRepository, SportRepository sportRepository, RequestRepository requestRepository, MessageRepository messageRepository, FilterRepositoryHelper filterRepositoryHelper, EntityRepositoryHelper entityRepositoryHelper) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
        this.commentRepository = commentRepository;
        this.sportRepository = sportRepository;
        this.requestRepository = requestRepository;
        this.messageRepository = messageRepository;
        this.filterRepositoryHelper = filterRepositoryHelper;
        this.entityRepositoryHelper = entityRepositoryHelper;
    }

    public UUID createEvent(EventCreationDto eventCreationDto) {
        User user = entityRepositoryHelper.getActiveUser();
        Sport sport = entityRepositoryHelper.getSport(eventCreationDto.getSport());
        LocalDateTime eventStart = DateTimeParser.parseDateTime(eventCreationDto.getEventStartDate(), eventCreationDto.getEventStartTime());
        Location location = entityRepositoryHelper.getLocation(eventCreationDto.getCity(), eventCreationDto.getArea());

        Validator.isEventStartInFuture(eventStart);
        Unit unit = validator.isEventRelatedToUnit(eventCreationDto);

        return eventRepository.save(EventConverter.createEvent(eventCreationDto, sport, user, unit, eventStart, location)).getEventNumber();
    }

    public UUID updateEvent(UUID eventNumber, EventCreationDto eventCreationDto){
        User user = entityRepositoryHelper.getActiveUser();
        Event event = entityRepositoryHelper.getEvent(eventNumber);
        Sport sport = entityRepositoryHelper.getSport(eventCreationDto.getSport());
        LocalDateTime eventStart = DateTimeParser.parseDateTime(eventCreationDto.getEventStartDate(), eventCreationDto.getEventStartTime());
        Location location = entityRepositoryHelper.getLocation(eventCreationDto.getCity(), eventCreationDto.getArea());

        Validator.isCreator(event, user);
        boolean active = Validator.isActiveInFuture(eventStart);
        Unit unit = validator.isEventRelatedToUnit(eventCreationDto);

        return eventRepository.save(EventConverter.updateFrom(event, eventCreationDto, sport, user, unit, eventStart, location, active)).getEventNumber();
    }

    public Set<String> getSports() {
        return sportRepository.findAll()
                .stream()
                .map(Sport::getName)
                .collect(Collectors.toSet());
    }

    public PageDto getEvents(Param p) {
        if(Stream.of(p.getFromDate(), p.getToDate()).allMatch(x -> x == null)){
            log.info(p.toString());
            return filterRepositoryHelper.filterNotByAnyDates(p);
        }

        /**
         * Between dates statements
         */
        if(Stream.of(p.getFromDate(), p.getToDate(), p.getType(), p.getCity(), p.getArea()).allMatch(x -> x != null)){
            log.info(p.toString());
            return filterRepositoryHelper.filterAll(p);
        }

        if(Stream.of(p.getFromDate(), p.getToDate(), p.getType(), p.getCity()).allMatch(x -> x != null)){
            log.info(p.toString());
            return filterRepositoryHelper.filterBetweenDatesAndTypeAndCity(p);
            //area missing

            //fromDate an toDate present
        }
        if(Stream.of(p.getFromDate(), p.getToDate(), p.getType()).allMatch(x -> x != null)) {
            log.info(p.toString());
            return filterRepositoryHelper.filterBetweenDatesAndType(p);
            //Area and city missing

        }
        if(Stream.of(p.getFromDate(), p.getToDate()).allMatch(x -> x != null)) {
            log.info(p.toString());
            return filterRepositoryHelper.filterBetweenDates(p);
            // filter only dates
        }

        /**
         * From date statements
         */
        if(Stream.of(p.getFromDate()).allMatch(x -> x != null)) {
            if(Stream.of(p.getType(), p.getCity(), p.getArea()).allMatch(x -> x != null)) {
                log.info(p.toString());
                return filterRepositoryHelper.filterFromDateAndTypeAndCityAndArea(p);
                //toDate missing
            } else if (Stream.of(p.getType(), p.getCity()).allMatch(x -> x != null)) {
                log.info(p.toString());
                return filterRepositoryHelper.filterFromDateAndTypeAndCity(p);

                //toDate missing area missing
            } else if(Stream.of(p.getType()).allMatch(x -> x != null)) {
                log.info(p.toString());
                return filterRepositoryHelper.filterFromDateAndType(p);
                //toDate missing area and
            } else if(Stream.of(p.getCity()).allMatch(x -> x != null)){
                return filterRepositoryHelper.filterFromDateAndCity(p);
            }
            log.info(p.toString());
            return filterRepositoryHelper.filterFromDate(p);
            // filter only fromDate
        }

        /**
         * To date statements
         */
        if(Stream.of(p.getToDate()).allMatch(x -> x != null)) {
            if(Stream.of(p.getType(), p.getCity(), p.getArea()).allMatch(x -> x != null)) {
                log.info(p.toString());
                return filterRepositoryHelper.filterToDateAndTypeAndCityAndArea(p);
                //fromDate missing
            } else if ( Stream.of(p.getType(), p.getCity()).allMatch(x -> x != null)) {
                log.info(p.toString());
                return filterRepositoryHelper.filterToDateAndTypeAndCity(p);
                //fromDate missing area missing
            } else if(Stream.of(p.getType()).allMatch(x -> x != null)) {
                log.info(p.toString());
                return filterRepositoryHelper.filterToDateAndType(p);
                //fromDate missing area and
            } else if(Stream.of(p.getCity()).allMatch(x -> x != null)){
                return filterRepositoryHelper.filterToDateAndCity(p);
            }
            return filterRepositoryHelper.filterToDate(p);
            // filter only toDate
        }

        log.info(p.toString());
        return filterRepositoryHelper.findAll(p);
    }

    public EventDto getEvent(UUID eventNumber) {
        User user = entityRepositoryHelper.getActiveUser();
        Event event = entityRepositoryHelper.getEvent(eventNumber);

        RequestStatus request = requestRepository.findByEventAndSender(event, user)
                .map(Request::getRequestStatus)
                .orElse(null);

        return EventConverter.createEventDto(event, request, user);
    }

    public long checkIfEventIsAboutToExpire() {
        LocalDateTime expirationDateTime = DateTimeParser.expirationDateTime();

        return eventRepository.findByEventStartLessThanAndActiveTrue(expirationDateTime)
                .stream()
                .map(this::inactivateEvent)
                .collect(Collectors.toList()).size();
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelEvent(UUID eventNumber) {
        User user = entityRepositoryHelper.getActiveUser();
        Event event = entityRepositoryHelper.getEvent(eventNumber);
        Validator.isCreator(event, user);

        inactivateEvent(event);
        markAsCancelledOnRequest(event);
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelRequest(UUID eventNumber) {
        User user = entityRepositoryHelper.getActiveUser();
        Event event = entityRepositoryHelper.getEvent(eventNumber);
        markAsLeftOnRequest(event, user);

        if(event.removeParticipant(user)) {
            eventRepository.save(event);
        }
    }

    private void markAsLeftOnRequest(Event event, User user) {
        requestRepository.findByEventAndSender(event, user)
                .map(request -> RequestConverter.setRequestStatusUpdateFrom(request, RequestStatus.LEFT))
                .map(requestRepository::save)
                .orElseThrow(() -> new BadRequestException("Can't leave event you're not part of"));
    }

    private void markAsCancelledOnRequest(Event event) {
        requestRepository.findByEvent(event)
                .forEach(request -> requestRepository.save(RequestConverter.setRequestStatusUpdateFrom(request, RequestStatus.CANCELLED)));
    }

    public UUID createCommentEvent(UUID eventNumber, CommentCreationDto commentCreationDto) {
        User user = entityRepositoryHelper.getActiveUser();
        Event event = entityRepositoryHelper.getEvent(eventNumber);

        return commentRepository.save(CommentConverter.createComment(commentCreationDto, event, user)).getCommentNumber();
    }

    public void deleteCommentForActiveUser(UUID commentNumber){
        User user = entityRepositoryHelper.getActiveUser();
        Comment comment = entityRepositoryHelper.getComment(commentNumber);
        Validator.isUserSameAsCommentator(user, comment);

        commentRepository.delete(comment);
    }

    public List<CommentDto> getCommentsForEvent(UUID eventNumber, Param param) {
        User user = entityRepositoryHelper.getActiveUser();
        Event event = entityRepositoryHelper.getEvent(eventNumber);

        return commentRepository.findByEvent(event, param.getCommentPageRequest())
                .stream()
                .map(comment -> CommentConverter.createCommentDto(comment, validator.isOwnerOfComment(comment, user)))
                .collect(Collectors.toList());
    }

    public Set<String> getAreasForCity(String city) {
        return locationRepository.findByCity(city)
                .stream()
                .map(Location::getArea)
                .map(Area::getArea)
                .collect(Collectors.toSet());
    }

    private Event inactivateEvent(Event event){
        event.setActive(false);
        return eventRepository.save(event);
    }

    public PageDto getEventsCreatorOf(Param param) {
        User user = entityRepositoryHelper.getActiveUser();

        if(param.isActive()){
            return filterRepositoryHelper.filterByCreatorOfActive(user, param);
        }
        return filterRepositoryHelper.filterByCreatorOfInActive(user, param);
    }

    public PageDto getEventsForUnitsUserFollows(Param param) {
        User user = entityRepositoryHelper.getActiveUser();

        return filterRepositoryHelper.filterByUnitsUserFollows(param, user.getMemberOf());
    }
}
