package se.elfu.sportprojectbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.elfu.sportprojectbackend.controller.model.LocationDto;
import se.elfu.sportprojectbackend.repository.LocationRepository;
import se.elfu.sportprojectbackend.repository.SportRepository;
import se.elfu.sportprojectbackend.repository.model.Sport;
import se.elfu.sportprojectbackend.utils.Validator;
import se.elfu.sportprojectbackend.utils.converter.LocationConverter;

@Service
public class AdminService {

    @Autowired
    private Validator validator;
    private final SportRepository sportRepository;
    private final LocationRepository locationRepository;

    public AdminService(SportRepository sportRepository, LocationRepository locationRepository) {
        this.sportRepository = sportRepository;
        this.locationRepository = locationRepository;
    }

    public Sport createSport(String name) {
        Sport sport = Sport.builder()
                .name(name)
                .build();
        return sportRepository.save(sport);
    }

    public void createLocation(LocationDto locationDto) {
        validator.isCityInDatabase(locationDto.getCity());

        locationDto.getAreas().forEach(area ->
                locationRepository.save(LocationConverter.createFrom(locationDto.getCity(), area)));
    }
}
