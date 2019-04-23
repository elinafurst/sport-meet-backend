package se.elfu.sportprojectbackend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.elfu.sportprojectbackend.controller.model.UnitDto;
import se.elfu.sportprojectbackend.controller.model.UserCreationDto;
import se.elfu.sportprojectbackend.controller.model.UserDto;
import se.elfu.sportprojectbackend.repository.AccountRepository;
import se.elfu.sportprojectbackend.repository.UserRepository;
import se.elfu.sportprojectbackend.repository.model.Account;
import se.elfu.sportprojectbackend.repository.model.Authority;
import se.elfu.sportprojectbackend.repository.model.Unit;
import se.elfu.sportprojectbackend.repository.model.User;
import se.elfu.sportprojectbackend.service.helper.EntityRepositoryHelper;
import se.elfu.sportprojectbackend.utils.Validator;
import se.elfu.sportprojectbackend.utils.converter.UnitConverter;
import se.elfu.sportprojectbackend.utils.converter.UserConverter;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;
    @Autowired
    Validator validator;

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final EntityRepositoryHelper entityRepositoryHelper;

    public UserService(UserRepository userRepository, AccountRepository accountRepository, EntityRepositoryHelper entityRepositoryHelper) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.entityRepositoryHelper = entityRepositoryHelper;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { //TODO error hantering
        Account account = entityRepositoryHelper.getAccount(email);

        log.info("Email:{}, password: {}, role: {}", account.getEmail(), account.getPassword(), account.getAuthority().getAuthority());

        return new org.springframework.security.core.userdetails.User(
                account.getEmail(), account.getPassword(), Collections.singleton(new SimpleGrantedAuthority(account.getAuthority().getAuthority())));
    }

    public User registerUser(UserCreationDto userCreationDto) {
        validator.isEmailOccupied(userCreationDto.getEmail().toLowerCase());
        validator.isUserNameOccupied(userCreationDto.getUsername());
        Authority authority = entityRepositoryHelper.getAuthority();

        return userRepository.save(UserConverter.createFrom(userCreationDto, authority, bCryptPasswordEncoder.encode(userCreationDto.getPassword())));
    }

    public UserDto getActiveUser() {
        User user = entityRepositoryHelper.getActiveUser();
        return UserConverter.createFrom(user);
    }

    public UserDto getUser(UUID userNumber) {
        User user = entityRepositoryHelper.getUser(userNumber);
        return UserConverter.createFrom(user);
    }

    public void updateUser(UserDto userDto) {
        User user = entityRepositoryHelper.getActiveUser();
        validator.isUsernameChanged(user.getUsername(), userDto.getUsername());

        userRepository.save(UserConverter.updateEntity(user, userDto));
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

    public Set<UnitDto> getGroupsForActiveUser() {
        return entityRepositoryHelper.getActiveUser()
                .getMemberOf()
                .stream()
                .map(UnitConverter::createFrom)
                .collect(Collectors.toSet());
    }

}
