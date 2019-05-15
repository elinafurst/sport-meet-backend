package se.elfu.sportprojectbackend.utils.converter;

import org.springframework.data.domain.Page;
import se.elfu.sportprojectbackend.controller.model.*;
import se.elfu.sportprojectbackend.controller.model.events.EventCreationDto;
import se.elfu.sportprojectbackend.controller.model.events.EventDto;
import se.elfu.sportprojectbackend.repository.model.*;
import se.elfu.sportprojectbackend.utils.DateTimeParser;
import se.elfu.sportprojectbackend.utils.KeyValueMapper;
import se.elfu.sportprojectbackend.utils.Validator;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public final class EventConverter {

    public static Event createEvent(EventCreationDto dto, Sport sport, User user, Unit unit, LocalDateTime eventStart, Location location){
        return Event.builder()
                .eventNumber(UUID.randomUUID())
                .name(dto.getName())
                .description(dto.getDescription())
                .sport(sport)
                .eventStart(eventStart)
                .createdBy(user)
                .byUnit(unit)
                .active(true)
                .meetingPoint(dto.getMeetingPoint())
                .location(location)
                .build();
    }

    /**
     * Example of toBuilder = true. Updates only som fields. Keeps eventNumber from entity
     */
    public static Event updateFrom(Event event, EventCreationDto dto, Sport sport, User user, Unit unit, LocalDateTime eventStart, Location location, boolean active) {
        return event.toBuilder()
                .name(dto.getName())
                .description(dto.getDescription())
                .sport(sport)
                .eventStart(eventStart)
                .createdBy(user)
                .byUnit(unit)
                .active(active)
                .meetingPoint(dto.getMeetingPoint())
                .location(location)
                .build();
    }

    /**
     * Example of converting to eventDto for detailed view
     */
    public static EventDto createEventDto(Event event, RequestStatus requestStatus, User user) {
        return EventDto.builder()
                .eventNumber(event.getEventNumber())
                .name(event.getName())
                .description(event.getDescription())
                .sport(event.getSport().getName())
                .eventStartDate(DateTimeParser.formatDate(event.getEventStart()))
                .eventStartTime(DateTimeParser.formatTime(event.getEventStart()))
                .eventStartDateTime(DateTimeParser.formatDateTimeSwedish(event.getEventStart()))
                .noOfParticipants(event.getParticipants().size())
                .participants(KeyValueMapper.mapUsers(event.getParticipants()))
                .createdBy(KeyValueMapper.mapUser(event.getCreatedBy()))
                .byUnit(KeyValueMapper.mapUnit(event.getByUnit()))
                .active(event.isActive())
                .city(event.getLocation().getCity())
                .area(event.getLocation().getArea().getArea())
                .meetingPoint(event.getMeetingPoint())
                .requestStatus(requestStatus)
                .isCreator(Validator.isSameUser(event.getCreatedBy().getId(), user.getId()))
                .build();
    }

    private static EventDto createEventDto(Event entity) {
        return EventDto.builder()
                .eventNumber(entity.getEventNumber())
                .name(entity.getName())
                .description(entity.getDescription())
                .sport(entity.getSport().getName())
                .eventStartDate(DateTimeParser.formatDate(entity.getEventStart()))
                .eventStartTime(DateTimeParser.formatTime(entity.getEventStart()))
                .eventStartDateTime(DateTimeParser.formatDateTimeSwedish(entity.getEventStart()))
                .noOfParticipants(entity.getParticipants().size())
                .active(entity.isActive())
                .city(entity.getLocation().getCity())
                .area(entity.getLocation().getArea().getArea())
                .meetingPoint(entity.getMeetingPoint())
                .build();
    }

    /**
     * Converts to PageDto. To get total hits of query and pages available and a list of objects from current page.
     */
    public static PageDto convertToPageDto(Page<Event> entities){
        Validator.isEmpty(entities.getSize()); // throws exception, one way to show webb request has no results could also use totalelements = 0

        return PageDto.builder()
                .totalElements(entities.getTotalElements())
                .totalPages(entities.getTotalPages() -1)
                .dtos(createEventDtoList(entities))
                .build();
    }

    private static List<Object> createEventDtoList(Page<Event> entities){
        return entities.stream()
                .map(EventConverter::createEventDto)
                .collect(Collectors.toList());
    }

    public static Location createLocation(String cityName, String areaName){
        return Location.builder()
                .city(cityName)
                .area(createArea(areaName))
                .build();
    }

    public static Area createArea(String areaName){
        return Area.builder()
                .area(areaName)
                .build();
    }

    public static Sport createSport(String name){
        return Sport.builder()
                .name(name)
                .build();
    }


}
