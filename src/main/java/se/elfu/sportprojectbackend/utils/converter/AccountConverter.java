package se.elfu.sportprojectbackend.utils.converter;

import se.elfu.sportprojectbackend.controller.model.users.UserCreationDto;
import se.elfu.sportprojectbackend.controller.model.users.UserDto;
import se.elfu.sportprojectbackend.repository.model.Account;
import se.elfu.sportprojectbackend.repository.model.Authority;
import se.elfu.sportprojectbackend.repository.model.PasswordResetToken;
import se.elfu.sportprojectbackend.repository.model.User;

import java.time.LocalDateTime;
import java.util.UUID;

public final class AccountConverter {

    public static UserDto createUserDto(User entity) {
        return UserDto.builder()
                .userNumber(entity.getUserNumber())
                .firstname(entity.getFirstname())
                .lastname(entity.getLastname())
                .username(entity.getUsername())
                .description(entity.getDescription())
                .build();
    }

    public static User updateUser(User entity, UserDto dto) {
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

    public static PasswordResetToken createPasswordResetToken(Account account) {
        return PasswordResetToken.builder()
                .token(UUID.randomUUID())
                .account(account)
                .expiration(LocalDateTime.now().plusHours(24))
                .build();

    }

    public static Account updateFrom(PasswordResetToken passwordResetToken, String encoded) {
        return passwordResetToken.getAccount().toBuilder()
                .password(encoded)
                .build();
    }
}
