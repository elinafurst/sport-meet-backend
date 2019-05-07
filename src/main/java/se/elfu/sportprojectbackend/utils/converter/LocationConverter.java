package se.elfu.sportprojectbackend.utils.converter;


import se.elfu.sportprojectbackend.repository.model.Area;
import se.elfu.sportprojectbackend.repository.model.Location;

public final class LocationConverter {

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

}
