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

    public static EventDto createEventDto(Event entity, RequestStatus requestStatus, User user) {
        return EventDto.builder()
                .eventNumber(entity.getEventNumber())
                .name(entity.getName())
                .description(entity.getDescription())
                .sport(entity.getSport().getName())
                .eventStartDate(DateTimeParser.formatDate(entity.getEventStart()))
                .eventStartTime(DateTimeParser.formatTime(entity.getEventStart()))
                .eventStartDateTime(DateTimeParser.formatDateTimeSwedish(entity.getEventStart()))
                .noOfParticipants(entity.getParticipants().size())
                .participants(KeyValueMapper.mapUsers(entity.getParticipants()))
                .createdBy(KeyValueMapper.mapUser(entity.getCreatedBy()))
                .byUnit(KeyValueMapper.mapUnit(entity.getByUnit()))
                .active(entity.isActive())
                .city(entity.getLocation().getCity())
                .area(entity.getLocation().getArea().getArea())
                .meetingPoint(entity.getMeetingPoint())
                .requestStatus(requestStatus)
                .isCreator(Validator.isSameUser(entity.getCreatedBy().getId(), user.getId()))
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
        Validator.isEmpty(entities.getSize());

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
