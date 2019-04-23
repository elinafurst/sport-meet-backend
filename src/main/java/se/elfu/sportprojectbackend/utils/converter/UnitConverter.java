package se.elfu.sportprojectbackend.utils.converter;

import org.springframework.data.domain.Page;
import se.elfu.sportprojectbackend.controller.model.UnitCreationDto;
import se.elfu.sportprojectbackend.controller.model.UnitDto;
import se.elfu.sportprojectbackend.controller.model.UnitPreviewDto;
import se.elfu.sportprojectbackend.repository.model.Unit;
import se.elfu.sportprojectbackend.utils.KeyValueMapper;

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

    private static UnitPreviewDto createToPreview(Unit entity) {
        return UnitPreviewDto.builder()
                .unitNumber(entity.getUnitNumber())
                .name(entity.getName())
                .description(entity.getDescription())
                .noOfMembers(entity.getMembers().size())
                .build();
    }

    public static UnitDto createFrom(Unit entity) {
        return UnitDto.builder()
                .unitNumber(entity.getUnitNumber())
                .name(entity.getName())
                .description(entity.getDescription())
                .admins(KeyValueMapper.mapUsers(entity.getAdmins()))
                .members(KeyValueMapper.mapUsers(entity.getMembers()))
                .noOfMembers(entity.getMembers().size())
                .build();
    }

    public static Set<UnitPreviewDto> createFromEntities(Page<Unit> entities) { //TODO
        return entities.stream()
                .map(UnitConverter::createToPreview)
                .collect(Collectors.toSet());

    }

}
