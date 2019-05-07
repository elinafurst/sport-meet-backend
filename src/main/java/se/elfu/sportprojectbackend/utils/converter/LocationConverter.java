package se.elfu.sportprojectbackend.utils.converter;


import se.elfu.sportprojectbackend.controller.model.LocationDto;
import se.elfu.sportprojectbackend.repository.model.Area;
import se.elfu.sportprojectbackend.repository.model.Location;

import java.util.Set;
import java.util.stream.Collectors;

public final class LocationConverter {

    public static Location createFrom(String cityName, String areaName){
        Area area = Area.builder().area(areaName).build();
        return Location.builder()
                .city(cityName)
                .area(area)
                .build();
    }

    public static LocationDto convert(String city, Set<String> areas) {
        return LocationDto.builder()
                .city(city)
                .areas(areas)
                .build();
    }


}
