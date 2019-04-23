package se.elfu.sportprojectbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.elfu.sportprojectbackend.controller.model.EventDto;
import se.elfu.sportprojectbackend.controller.model.UnitCreationDto;
import se.elfu.sportprojectbackend.controller.model.UnitDto;
import se.elfu.sportprojectbackend.controller.model.UnitPreviewDto;
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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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
        Unit unit = entityRepositoryHelper.getUnit(unitNumber);
        return UnitConverter.createFrom(unit);
    }

    public Map<UUID, String> getUnitsActiveUserIsAdminOf(){
        User user = entityRepositoryHelper.getActiveUser();
        return KeyValueMapper.mapUnits(user.getAdminOf());
    }

    public Set<UnitPreviewDto> getUnits(Param param) {
        Page<Unit> units = unitRepository.findAll(param.getUnitPageRequest());
        return UnitConverter.createFromEntities(units);
    }

    private void makeGroupAdmin(Unit unit) {
        User user = entityRepositoryHelper.getActiveUser();
        user.addAdminOf(unit);
        userRepository.save(user);
    }

    public List<EventDto> getEventsForUnit(UUID unitNumber, Param param) {
        Unit unit = entityRepositoryHelper.getUnit(unitNumber);
        Page<Event> events = eventRepository.findByByUnit(unit, param.getEventPageRequest());

        return EventConverter.createFromEntities(events);
    }
}