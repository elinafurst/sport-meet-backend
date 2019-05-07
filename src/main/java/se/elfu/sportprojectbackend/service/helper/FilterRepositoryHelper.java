package se.elfu.sportprojectbackend.service.helper;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import se.elfu.sportprojectbackend.controller.model.PageDto;
import se.elfu.sportprojectbackend.controller.parm.Param;
import se.elfu.sportprojectbackend.repository.EventRepository;
import se.elfu.sportprojectbackend.repository.model.*;
import se.elfu.sportprojectbackend.utils.DateTimeParser;
import se.elfu.sportprojectbackend.utils.converter.EventConverter;

import java.time.LocalDateTime;
import java.util.Set;

@Component
public class FilterRepositoryHelper {

    private final EventRepository eventRepository;
    private static String FROM_TIME ="00:00";
    private static String TO_TIME = "23:59";

    public FilterRepositoryHelper(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public PageDto filterFromDateAndCity(Param p) {
        Page<Event> events = eventRepository.findByEventStartGreaterThanEqualAndLocationCityAndActiveTrue(
                DateTimeParser.parseDateTime(p.getFromDate(), FROM_TIME),
                p.getCity(),
                p.getEventPageRequest());

        return EventConverter.convertToPageDto(events);
    }

    public PageDto filterToDateAndCity(Param p) {
        Page<Event> events = eventRepository.findByEventStartBetweenAndLocationCityAndActiveTrue(
                LocalDateTime.now(),
                DateTimeParser.parseDateTime(p.getToDate(), TO_TIME),
                p.getCity(),
                p.getEventPageRequest());

        return EventConverter.convertToPageDto(events);
    }

    public PageDto filterToDate(Param p) {
        Page<Event> events = eventRepository.findByEventStartBetweenAndActiveTrue(
                LocalDateTime.now(),
                DateTimeParser.parseDateTime(p.getToDate(), TO_TIME),
                p.getEventPageRequest());

        return EventConverter.convertToPageDto(events);
    }

    public PageDto filterToDateAndType(Param p) {
        Page<Event> events = eventRepository.findByEventStartBetweenAndSportNameAndActiveTrue(
                LocalDateTime.now(),
                DateTimeParser.parseDateTime(p.getToDate(), TO_TIME),
                p.getType(),
                p.getEventPageRequest());

        return EventConverter.convertToPageDto(events);
    }

    public PageDto filterToDateAndTypeAndCity(Param p) {
        Page<Event> events = eventRepository.findByEventStartBetweenAndSportNameAndLocationCityAndActiveTrue(
                LocalDateTime.now(),
                DateTimeParser.parseDateTime(p.getToDate(), TO_TIME),
                p.getType(),
                p.getCity(),
                p.getEventPageRequest());

        return EventConverter.convertToPageDto(events);
    }

    public PageDto filterToDateAndTypeAndCityAndArea(Param p) {
        Page<Event> events = eventRepository.findByEventStartBetweenAndSportNameAndLocationCityAndLocationAreaAreaAndActiveTrue(
                LocalDateTime.now(),
                DateTimeParser.parseDateTime(p.getToDate(), TO_TIME),
                p.getType(),
                p.getCity(),
                p.getArea(),
                p.getEventPageRequest());

        return EventConverter.convertToPageDto(events);
    }

    public PageDto filterFromDate(Param p) {
        Page<Event> events = eventRepository.findByEventStartGreaterThanEqualAndActiveTrue(
                DateTimeParser.parseDateTime(p.getFromDate(), FROM_TIME),
                p.getType(),
                p.getEventPageRequest());

        return EventConverter.convertToPageDto(events);
    }

    public PageDto filterFromDateAndType(Param p) {
        Page<Event> events = eventRepository.findByEventStartGreaterThanEqualAndSportNameAndActiveTrue(
                DateTimeParser.parseDateTime(p.getFromDate(), FROM_TIME),
                p.getType(),
                p.getEventPageRequest());

        return EventConverter.convertToPageDto(events);
    }

    public PageDto filterFromDateAndTypeAndCity(Param p) {
        Page<Event> events = eventRepository.findByEventStartGreaterThanEqualAndSportNameAndLocationCityAndActiveTrue(
                DateTimeParser.parseDateTime(p.getFromDate(), FROM_TIME),
                p.getType(),
                p.getCity(),
                p.getEventPageRequest());

        return EventConverter.convertToPageDto(events);
    }

    public PageDto filterFromDateAndTypeAndCityAndArea(Param p) {
        Page<Event> events = eventRepository.findByEventStartGreaterThanEqualAndSportNameAndLocationCityAndLocationAreaAreaAndActiveTrue(
                DateTimeParser.parseDateTime(p.getFromDate(), FROM_TIME),
                p.getType(),
                p.getCity(),
                p.getArea(),
                p.getEventPageRequest());

        return EventConverter.convertToPageDto(events);
    }

    public PageDto filterBetweenDatesAndType(Param p) {
        Page<Event> events = eventRepository.findByEventStartBetweenAndSportNameAndActiveTrue(
                DateTimeParser.parseDateTime(p.getFromDate(), FROM_TIME),
                DateTimeParser.parseDateTime(p.getToDate(), TO_TIME),
                p.getType(),
                p.getEventPageRequest());

        return EventConverter.convertToPageDto(events);
    }

    public PageDto filterBetweenDatesAndTypeAndCity(Param p) {
        Page<Event> events = eventRepository.findByEventStartBetweenAndSportNameAndLocationCityAndActiveTrue(
                DateTimeParser.parseDateTime(p.getFromDate(), FROM_TIME),
                DateTimeParser.parseDateTime(p.getToDate(), TO_TIME),
                p.getType(),
                p.getCity(),
                p.getEventPageRequest());

        return EventConverter.convertToPageDto(events);
    }

    public PageDto filterBetweenDates(Param p) {
        Page<Event> events = eventRepository.findByEventStartBetweenAndActiveTrue(
                DateTimeParser.parseDateTime(p.getFromDate(), FROM_TIME),
                DateTimeParser.parseDateTime(p.getToDate(), TO_TIME),
                p.getEventPageRequest());

        return EventConverter.convertToPageDto(events);
    }

    public PageDto filterAll(Param param) {
        Page<Event> events = eventRepository.findByEventStartBetweenAndSportNameAndLocationCityAndLocationAreaAreaAndActiveTrue( //TODO active
                DateTimeParser.parseDateTime(param.getFromDate(), FROM_TIME),
                DateTimeParser.parseDateTime(param.getToDate(), TO_TIME),
                param.getType(),
                param.getCity(),
                param.getArea(),
                param.getEventPageRequest());

        return EventConverter.convertToPageDto(events);
    }

    public PageDto filterByCreatorOfActive(User user, Param p) {
        Page<Event> events = eventRepository.findByCreatedByAndActiveTrue(user, p.getEventPageRequest());

        return EventConverter.convertToPageDto(events);
    }

    public PageDto filterByCreatorOfInActive(User user, Param p) {
        Page<Event> events = eventRepository.findByCreatedByAndActiveFalse(user, p.getEventPageRequest());

        return EventConverter.convertToPageDto(events);
    }

    public PageDto filterByUnitsUserFollows(Param param, Set<Unit> memberOf) {
        Page<Event> events = eventRepository.findByByUnitIn(memberOf, param.getEventPageRequest());

        return EventConverter.convertToPageDto(events);
    }

    public PageDto findAll(Param p) {
        Page<Event> events = eventRepository.findAll(p.getEventPageRequest());

        return EventConverter.convertToPageDto(events);
    }



    public PageDto filterNotByAnyDates(Param param) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withIgnorePaths("eventNumber")
                .withIgnorePaths("name")
                .withIgnorePaths("description")
                .withIgnorePaths("eventStart")
                .withIgnorePaths("maxParticipants")
                .withIgnorePaths("participants")
                .withIgnorePaths("createdBy")
                .withIgnorePaths("byUnit")
                .withIgnoreNullValues();

        Example<Event> exampleQuery = Example.of(searchCriterias(param), matcher);
        Page<Event> events = eventRepository.findAll(exampleQuery, param.getEventPageRequest());

        return EventConverter.convertToPageDto(events);
    }

    private Event searchCriterias(Param param) {
        Location location = Location.builder()
                .city(param.getCity())
                .area(Area.builder().area(param.getArea()).build())
                .build();

        return Event.builder()
                .sport(Sport.builder().name(param.getType()).build())
                .location(location)
                .active(true)
                .build();
    }
}
