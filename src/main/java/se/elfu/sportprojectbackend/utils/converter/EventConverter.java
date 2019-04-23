package se.elfu.sportprojectbackend.utils.converter;

import org.springframework.data.domain.Page;
import se.elfu.sportprojectbackend.controller.model.*;
import se.elfu.sportprojectbackend.repository.model.*;
import se.elfu.sportprojectbackend.utils.DateTimeParser;
import se.elfu.sportprojectbackend.utils.KeyValueMapper;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public final class EventConverter {


    public static Event createFrom(EventCreationDto dto, Sport sport, User user, Unit unit, LocalDateTime eventStart, Location location){
        return Event.builder()
                .eventNumber(UUID.randomUUID())
                .name(dto.getName())
                .description(dto.getDescription())
                .sport(sport)
                .eventStart(eventStart)
                .maxParticipants(dto.getMaxParticipants())
                .createdBy(user)
                .byUnit(unit)
                .active(true)
                .meetingPoint(dto.getMeetingPoint())
                .location(location)
                .build();
    }

    public static EventDto createFrom(Event entity) {
        return EventDto.builder()
                .eventNumber(entity.getEventNumber())
                .name(entity.getName())
                .description(entity.getDescription())
                .sport(entity.getSport().getName())
                .eventStartDate(DateTimeParser.formatDate(entity.getEventStart()))
                .eventStartTime(DateTimeParser.formatTime(entity.getEventStart()))
                .noOfParticipants(entity.getParticipants().size())
                .maxParticipants(entity.getMaxParticipants())
                .participants(KeyValueMapper.mapUsers(entity.getParticipants()))
                .createdBy(KeyValueMapper.mapUser(entity.getCreatedBy()))
                .byUnit(KeyValueMapper.mapUnit(entity.getByUnit()))
                .active(entity.isActive())
                .city(entity.getLocation().getCity())
                .area(entity.getLocation().getArea().getArea())
                .meetingPoint(entity.getMeetingPoint())
                .build();
    }

    public static EventDto createFromList(Event entity) {
        return EventDto.builder()
                .eventNumber(entity.getEventNumber())
                .name(entity.getName())
                .description(entity.getDescription())
                .sport(entity.getSport().getName())
                .eventStartDate(DateTimeParser.formatDate(entity.getEventStart()))
                .eventStartTime(DateTimeParser.formatTime(entity.getEventStart()))
                .noOfParticipants(entity.getParticipants().size())
                .maxParticipants(entity.getMaxParticipants())
                .active(entity.isActive())
                .city(entity.getLocation().getCity())
                .area(entity.getLocation().getArea().getArea())
                .meetingPoint(entity.getMeetingPoint())
                .build();
    }

    public static List<EventDto> createFromEntities(Page<Event> entities){
        return entities.stream()
                .map(EventConverter::createFromList)
                .collect(Collectors.toList());
    }

}
