package se.elfu.sportprojectbackend.utils.converter;

import se.elfu.sportprojectbackend.controller.model.users.UserCreationDto;
import se.elfu.sportprojectbackend.controller.model.users.UserDto;
import se.elfu.sportprojectbackend.repository.model.Account;
import se.elfu.sportprojectbackend.repository.model.Authority;
import se.elfu.sportprojectbackend.repository.model.User;

import java.util.UUID;

public final class UserConverter {

    public static UserDto createUserDto(User entity) {
        return UserDto.builder()
                .userNumber(entity.getUserNumber())
                .firstname(entity.getFirstname())
                .lastname(entity.getLastname())
                .username(entity.getUsername())
                .description(entity.getDescription())
                .build();
    }

    public static User updateEntity(User entity, UserDto dto) {
        return entity.toBuilder()
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .username(dto.getUsername())
                .description(dto.getDescription())
                .build();
    }

    public static User createUser(UserCreationDto userCreationDto, Authority authority, String encoded) {
        return User.builder()
                .userNumber(UUID.randomUUID())
                .firstname(userCreationDto.getFirstname())
                .lastname(userCreationDto.getLastname())
                .username(userCreationDto.getUsername())
                .description(userCreationDto.getDescription())
                .account(createAccount(userCreationDto, authority, encoded))
                .build();
    }

    private static Account createAccount(UserCreationDto userCreationDto, Authority authority, String encoded) {
        return Account.builder()
                .email(userCreationDto.getEmail().toLowerCase())
                .password(encoded)
                .authority(authority)
                .build();

    }
}
