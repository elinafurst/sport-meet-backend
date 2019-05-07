package se.elfu.sportprojectbackend.utils.converter;

import org.springframework.data.domain.Page;
import se.elfu.sportprojectbackend.controller.model.PageDto;
import se.elfu.sportprojectbackend.controller.model.UnitCreationDto;
import se.elfu.sportprojectbackend.controller.model.UnitDto;
import se.elfu.sportprojectbackend.controller.model.UnitPreviewDto;
import se.elfu.sportprojectbackend.repository.model.Event;
import se.elfu.sportprojectbackend.repository.model.Unit;
import se.elfu.sportprojectbackend.repository.model.User;
import se.elfu.sportprojectbackend.utils.KeyValueMapper;
import se.elfu.sportprojectbackend.utils.Validator;

import java.util.*;
import java.util.stream.Collectors;

public final class UnitConverter {

    public static Unit createFrom(UnitCreationDto dto) {
        return Unit.builder()
                .unitNumber(UUID.randomUUID())
                .name(dto.getName())
                .description(dto.getName())
                .build();
    }

    public static UnitPreviewDto createToPreview(Unit entity, Long noOfEvents) {
        return UnitPreviewDto.builder()
                .unitNumber(entity.getUnitNumber())
                .name(entity.getName())
                .description(entity.getDescription())
                .noOfMembers(entity.getMembers().size())
                .noOfEvents(noOfEvents)
                .build();
    }

    public static UnitDto createFrom(Unit entity, boolean isMember) {
        return UnitDto.builder()
                .unitNumber(entity.getUnitNumber())
                .name(entity.getName())
                .description(entity.getDescription())
                .admins(KeyValueMapper.mapUsers(entity.getAdmins()))
                .members(KeyValueMapper.mapUsers(entity.getMembers()))
                .noOfMembers(entity.getMembers().size())
                .isMember(isMember)
                .build();
    }


}
