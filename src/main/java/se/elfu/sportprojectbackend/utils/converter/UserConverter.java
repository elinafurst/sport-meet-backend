package se.elfu.sportprojectbackend.utils.converter;

import se.elfu.sportprojectbackend.controller.model.UserCreationDto;
import se.elfu.sportprojectbackend.controller.model.UserDto;
import se.elfu.sportprojectbackend.repository.model.Account;
import se.elfu.sportprojectbackend.repository.model.Authority;
import se.elfu.sportprojectbackend.repository.model.User;

import java.util.UUID;

public final class UserConverter {

    public static User createFrom(UserCreationDto dto, Account account) {
        return User.builder()
                .userNumber(UUID.randomUUID())
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .username(dto.getUsername())
                .description(dto.getDescription())
                .account(account)
                .build();
    }

    public static UserDto createFrom(User entity) {
        return UserDto.builder()
                .userNumber(entity.getUserNumber())
                .firstname(entity.getFirstname())
                .lastname(entity.getLastname())
                .username(entity.getUsername())
                .description(entity.getDescription())
                .build();
    }

    public static User updateEntity(User entity, UserDto dto){
        return entity.toBuilder()
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .username(dto.getUsername())
                .description(dto.getDescription())
                .build();
    }

    public static User createFrom(UserCreationDto userCreationDto, Authority authority, String encoded) {
        Account account = Account.builder()
                .email(userCreationDto.getEmail().toLowerCase())
                .password(encoded)
                .authority(authority)
                .build();
        return createFrom(userCreationDto, account);
    }

}
