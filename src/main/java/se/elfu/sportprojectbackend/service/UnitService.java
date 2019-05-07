package se.elfu.sportprojectbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.elfu.sportprojectbackend.controller.model.*;
import se.elfu.sportprojectbackend.controller.parm.Param;
import se.elfu.sportprojectbackend.repository.EventRepository;
import se.elfu.sportprojectbackend.repository.UnitRepository;
import se.elfu.sportprojectbackend.repository.UserRepository;
import se.elfu.sportprojectbackend.repository.model.Event;
import se.elfu.sportprojectbackend.repository.model.Unit;
import se.elfu.sportprojectbackend.repository.model.User;
import se.elfu.sportprojectbackend.service.helper.EntityRepositoryHelper;
import se.elfu.sportprojectbackend.utils.KeyValueMapper;
import se.elfu.sportprojectbackend.utils.Validator;
import se.elfu.sportprojectbackend.utils.converter.EventConverter;
import se.elfu.sportprojectbackend.utils.converter.UnitConverter;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UnitService {

    @Autowired
    private Validator validator;
    private final UnitRepository unitRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EntityRepositoryHelper entityRepositoryHelper;

    public UnitService(UnitRepository unitRepository, UserRepository userRepository, EventRepository eventRepository, EntityRepositoryHelper entityRepositoryHelper) {
        this.unitRepository = unitRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.entityRepositoryHelper = entityRepositoryHelper;
    }

    @Transactional(rollbackFor = Exception.class)
    public UUID createUnit(UnitCreationDto unitCreationDto) {
        validator.isUnitNameOccupied(unitCreationDto.getName());
        Unit unit = unitRepository.save(UnitConverter.createFrom(unitCreationDto));
        makeGroupAdmin(unit);

        return unit.getUnitNumber();
    }

    public UnitDto getUnit(UUID unitNumber) {
        User user = entityRepositoryHelper.getActiveUser();
        Unit unit = entityRepositoryHelper.getUnit(unitNumber);
        boolean isMember = Validator.isActiveUserMemberOfUnit(unit, user.getId());

        return UnitConverter.createFrom(unit, isMember);
    }

    public PageDto getUnitsActiveUserIsAdminOf(Param param){
        User user = entityRepositoryHelper.getActiveUser();
        Page<Unit> units = unitRepository.findByAdminsIn(user, param.getUnitPageRequest());

        return convertToPageDto(units);
    }

    public Object getUnitsActiveUserIsAdminOfKeyPairs() {
        User user = entityRepositoryHelper.getActiveUser();
        Set<Unit> units = unitRepository.findByAdminsIn(user);

        return KeyValueMapper.mapUnits(units);
    }

    public PageDto getUnits(Param param) {
        Page<Unit> units = unitRepository.findAll(param.getUnitPageRequest());

        return convertToPageDto(units);
    }

    private void makeGroupAdmin(Unit unit) {
        User user = entityRepositoryHelper.getActiveUser();
        user.addAdminOf(unit);

        userRepository.save(user);
    }

    public PageDto getEventsForUnit(UUID unitNumber, Param param) {
        Unit unit = entityRepositoryHelper.getUnit(unitNumber);
        Page<Event> events = eventRepository.findByByUnit(unit, param.getEventPageRequest());

        return EventConverter.convertToPageDto(events);
    }

    public void joinGroup(UUID unitNumber) {
        Unit unit = entityRepositoryHelper.getUnit(unitNumber);
        User user = entityRepositoryHelper.getActiveUser();
        user.addMemberOf(unit);

        userRepository.save(user);
    }

    public void leaveGroup(UUID unitNumber) {
        Unit unit = entityRepositoryHelper.getUnit(unitNumber);
        User user = entityRepositoryHelper.getActiveUser();
        user.removeMemberOf(unit);

        userRepository.save(user);
    }

    private PageDto convertToPageDto(Page<Unit> units){
        Validator.isEmpty(units.getSize());

        return PageDto.builder()
                .totalElements(units.getTotalElements())
                .totalPages(units.getTotalPages() -1)
                .dtos(sortUnits(units))
                .build();
    }

    private List<Object> sortUnits(Page<Unit> units) {
        return units.stream()
                .map(unit -> UnitConverter.createToPreview(unit, eventRepository.countByByUnit(unit)))
                .sorted(Comparator.comparing(UnitPreviewDto::getNoOfEvents))
                .collect(Collectors.toList());
    }
}