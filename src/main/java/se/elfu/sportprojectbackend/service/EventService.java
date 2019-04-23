package se.elfu.sportprojectbackend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import se.elfu.sportprojectbackend.controller.model.CommentCreationDto;
import se.elfu.sportprojectbackend.controller.model.CommentDto;
import se.elfu.sportprojectbackend.controller.model.EventDto;
import se.elfu.sportprojectbackend.controller.model.EventCreationDto;
import se.elfu.sportprojectbackend.controller.parm.Param;
import se.elfu.sportprojectbackend.repository.CommentRepository;
import se.elfu.sportprojectbackend.repository.EventRepository;
import se.elfu.sportprojectbackend.repository.LocationRepository;
import se.elfu.sportprojectbackend.repository.SportRepository;
import se.elfu.sportprojectbackend.repository.model.*;
import se.elfu.sportprojectbackend.service.helper.EntityRepositoryHelper;
import se.elfu.sportprojectbackend.service.helper.FilterRepositoryHelper;
import se.elfu.sportprojectbackend.utils.Validator;
import se.elfu.sportprojectbackend.utils.converter.CommentConverter;
import se.elfu.sportprojectbackend.utils.converter.EventConverter;
import se.elfu.sportprojectbackend.utils.DateTimeParser;

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
    private final FilterRepositoryHelper filterRepositoryHelper;
    private final EntityRepositoryHelper entityRepositoryHelper;

    public EventService(EventRepository eventRepository, LocationRepository locationRepository, CommentRepository commentRepository, SportRepository sportRepository, FilterRepositoryHelper filterRepositoryHelper, EntityRepositoryHelper entityRepositoryHelper) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
        this.commentRepository = commentRepository;
        this.sportRepository = sportRepository;
        this.filterRepositoryHelper = filterRepositoryHelper;
        this.entityRepositoryHelper = entityRepositoryHelper;
    }

    public UUID createEvent(EventCreationDto eventCreationDto) {
        User user = entityRepositoryHelper.getActiveUser();
        Sport sport = entityRepositoryHelper.getSport(eventCreationDto.getSport());
        Unit unit = validator.isEventRelatedToUnit(eventCreationDto);
        LocalDateTime eventStart = DateTimeParser.parseDateTime(eventCreationDto.getEventStartDate(), eventCreationDto.getEventStartTime());
        Location location = isLocationInDataBase(eventCreationDto);

        return eventRepository.save(EventConverter.createFrom(eventCreationDto, sport, user, unit, eventStart, location)).getEventNumber();
    }

    // TODO admin stuff
    public Sport createSport(String name) {
        Sport sport = Sport.builder()
                .name(name)
                .build();
        sportRepository.save(sport);
        return null;
    }

    public Set<String> getSports() {
        return sportRepository.findAll()
                .stream()
                .map(Sport::getName)
                .collect(Collectors.toSet());
    }

    public List<EventDto> getEvents(Param p) {

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

    public void joinEvent(UUID eventNumber) {
        User user = entityRepositoryHelper.getActiveUser();
        Event event = entityRepositoryHelper.getEvent(eventNumber);
        Validator.isEventActive(event);
        Validator.isEventFull(event);
        event.addParticipant(user);
        eventRepository.save(event);
    }

    public void leaveEvent(UUID eventNumber) {
        User user = entityRepositoryHelper.getActiveUser();
        Event event = entityRepositoryHelper.getEvent(eventNumber);
        event.removeParticipant(user);
        eventRepository.save(event);
    }

    public EventDto getEvent(UUID eventNumber) {
        Event event = entityRepositoryHelper.getEvent(eventNumber);
        return EventConverter.createFrom(event);
    }

    public void updateExpiredEvents() {
        LocalDateTime expirationDateTime = DateTimeParser.expirationDateTime();

        List<Event> events = eventRepository.findByEventStartLessThanAndActiveTrue(expirationDateTime)
                .stream()
                .map(this::inActivateEvent)
                .collect(Collectors.toList());

        log.info("Found {} events", events.size());
    }

    public void cancelEvent(UUID eventNumber) {
        User user = entityRepositoryHelper.getActiveUser();
        Event event = entityRepositoryHelper.getEvent(eventNumber);
        Validator.isCreator(event, user);
        inActivateEvent(event);
    }

    //TODO null and check make prettyyyy

    private Location isLocationInDataBase(EventCreationDto eventCreationDto) {
        Location location = locationRepository.findByCity(eventCreationDto.getCity())
                .orElse(new Location(null, eventCreationDto.getCity(), null));

        if(location.getArea() == null && location.getId() == null) {
            return location.toBuilder().area(Area.builder().area(eventCreationDto.getArea()).build()).build();
        }
        if(!location.getArea().getArea().equalsIgnoreCase(eventCreationDto.getArea())){
            return Location.builder().city(location.getCity()).area(Area.builder().area(eventCreationDto.getArea()).build()).build();
        }
        return location;
    }
    private Event inActivateEvent(Event event){
        event.setActive(false);
        return eventRepository.save(event);
    }

    public Comment createCommentEvent(UUID eventNumber, CommentCreationDto commentCreationDto) {
        User user = entityRepositoryHelper.getActiveUser();
        Event event = entityRepositoryHelper.getEvent(eventNumber);
        Comment comment = commentRepository.save(CommentConverter.createFrom(commentCreationDto, event, user));
        return comment;
    }

    public void deleteCommentForActiveUser(UUID commentNumber){
        User user = entityRepositoryHelper.getActiveUser();
        Comment comment = entityRepositoryHelper.getComment(commentNumber);
        Validator.isUserSameAsCommentator(user, comment);
        commentRepository.delete(comment);

    }

    public List<CommentDto> getCommentsForEvent(UUID eventNumber, Param param) {
        Page<Comment> comments = commentRepository.findByEventEventNumber(eventNumber, param.getCommentPageRequest());
        return sortComments(comments);
    }

    private List<CommentDto> sortComments(Page<Comment> comments) {
        User user = entityRepositoryHelper.getActiveUser();

        return comments.stream()
                .map(comment -> CommentConverter.createFrom(comment, validator.isOwnerOfComment(comment, user)))
                .collect(Collectors.toList());
    }
}
