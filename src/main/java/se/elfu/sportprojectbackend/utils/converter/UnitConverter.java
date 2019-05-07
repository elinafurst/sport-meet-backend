package se.elfu.sportprojectbackend.utils.converter;

import se.elfu.sportprojectbackend.controller.model.units.UnitCreationDto;
import se.elfu.sportprojectbackend.controller.model.units.UnitDto;
import se.elfu.sportprojectbackend.controller.model.units.UnitPreviewDto;
import se.elfu.sportprojectbackend.repository.model.Unit;
import se.elfu.sportprojectbackend.utils.KeyValueMapper;

import java.util.*;

public final class UnitConverter {

    public static Unit createUnit(UnitCreationDto dto) {
        return Unit.builder()
                .unitNumber(UUID.randomUUID())
                .name(dto.getName())
                .description(dto.getName())
                .build();
    }

    public static UnitPreviewDto createUnitPreviewDto(Unit entity, Long noOfEvents) {
        return UnitPreviewDto.builder()
                .unitNumber(entity.getUnitNumber())
                .name(entity.getName())
                .description(entity.getDescription())
                .noOfMembers(entity.getMembers().size())
                .noOfEvents(noOfEvents)
                .build();
    }

    public static UnitDto createUnitDto(Unit entity, boolean isMember) {
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
