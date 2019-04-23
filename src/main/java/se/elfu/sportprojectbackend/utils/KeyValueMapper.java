package se.elfu.sportprojectbackend.utils;

import se.elfu.sportprojectbackend.repository.model.Unit;
import se.elfu.sportprojectbackend.repository.model.User;

import java.util.*;
import java.util.stream.Collectors;

public final class KeyValueMapper {

    public static Map<UUID, String> mapUser(User user){
        return Optional.ofNullable(user)
                .map(u -> Collections.singletonMap(u.getUserNumber(), u.getUsername()))
                .orElse(null);
    }

    public static Map<UUID, String> mapUsers(Set<User> users){
        return users.stream()
                .collect(Collectors.toMap(User::getUserNumber, User::getUsername));
    }

    public static Map<UUID, String> mapUnit(Unit unit){
        return Optional.ofNullable(unit)
                .map(u -> Collections.singletonMap(u.getUnitNumber(), u.getName()))
                .orElse(null);
    }

    public static Map<UUID, String> mapUnits(Set<Unit> units){
        return units.stream()
                .collect(Collectors.toMap(Unit::getUnitNumber, Unit::getName));
    }
}
