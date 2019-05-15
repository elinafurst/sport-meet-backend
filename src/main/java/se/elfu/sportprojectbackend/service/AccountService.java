package se.elfu.sportprojectbackend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.elfu.sportprojectbackend.controller.model.units.UnitPreviewDto;
import se.elfu.sportprojectbackend.controller.model.users.UserCreationDto;
import se.elfu.sportprojectbackend.controller.model.users.UserDto;
import se.elfu.sportprojectbackend.repository.*;
import se.elfu.sportprojectbackend.repository.model.*;
import se.elfu.sportprojectbackend.service.helper.EntityRepositoryHelper;
import se.elfu.sportprojectbackend.service.helper.EmailSender;
import se.elfu.sportprojectbackend.utils.Validator;
import se.elfu.sportprojectbackend.utils.converter.UnitConverter;
import se.elfu.sportprojectbackend.utils.converter.AccountConverter;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AccountService implements UserDetailsService {

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private Validator validator;
    @Autowired
    private EmailSender emailSender;

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;
    private final RequestRepository requestRepository;
    private final UnitRepository unitRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EntityRepositoryHelper entityRepositoryHelper;

    public AccountService(UserRepository userRepository, AccountRepository accountRepository, EventRepository eventRepository, CommentRepository commentRepository, RequestRepository requestRepository, UnitRepository unitRepository, PasswordResetTokenRepository passwordResetTokenRepository, EntityRepositoryHelper entityRepositoryHelper) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.eventRepository = eventRepository;
        this.commentRepository = commentRepository;
        this.requestRepository = requestRepository;
        this.unitRepository = unitRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.entityRepositoryHelper = entityRepositoryHelper;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = entityRepositoryHelper.getAccount(email);

        return new org.springframework.security.core.userdetails.User(
                account.getEmail(), account.getPassword(), Collections.singleton(new SimpleGrantedAuthority(account.getAuthority().getAuthority())));
    }

    public UUID registerUser(UserCreationDto userCreationDto) {
        validator.isEmailOccupied(userCreationDto.getEmail().toLowerCase());
        validator.isUserNameOccupied(userCreationDto.getUsername());
        Authority authority = entityRepositoryHelper.getAuthority();

        return userRepository.save(AccountConverter.createUser(userCreationDto, authority, bCryptPasswordEncoder.encode(userCreationDto.getPassword()))).getUserNumber();
    }

    public UserDto getActiveUser() {
        User user = entityRepositoryHelper.getActiveUser();
        return AccountConverter.createUserDto(user);
    }

    public UserDto getUser(UUID userNumber) {
        User user = entityRepositoryHelper.getUser(userNumber);
        return AccountConverter.createUserDto(user);
    }

    public void updateUser(UserDto userDto) {
        User user = entityRepositoryHelper.getActiveUser();
        validator.isUsernameChanged(user.getUsername(), userDto.getUsername());

        userRepository.save(AccountConverter.updateUser(user, userDto));
    }

    public List<UnitPreviewDto> getUnitssForActiveUser() {
        return entityRepositoryHelper.getActiveUser()
                .getMemberOf()
                .stream()
                .map(unit -> UnitConverter.createUnitPreviewDto(unit, eventRepository.countByByUnit(unit)))
                .collect(Collectors.toList());
    }

    /**
     * Saves information for reset password and email link to reset password
     */
    public void resetPassword(String email) {
        Account account = entityRepositoryHelper.getAccount(email);

        PasswordResetToken passwordResetToken = passwordResetTokenRepository.save(AccountConverter.createPasswordResetToken(account));

        emailSender.sendResetPasswordMail(passwordResetToken);
    }

    /**
     *   Validate reset password request has been made. Check if link has not been expired
     **/

    public PasswordResetToken validatePasswordToken(UUID token) {
        PasswordResetToken passwordResetToken = entityRepositoryHelper.getPasswordResetToken(token);
        Validator.isTokenExpired(passwordResetToken.getExpiration());

        return passwordResetToken;
    }

    /**
     * Saves new password
     */

    public void saveNewPassword(String password, UUID token) {
        PasswordResetToken passwordResetToken = validatePasswordToken(token);

        accountRepository.save(AccountConverter.updateFrom(passwordResetToken, bCryptPasswordEncoder.encode(password)));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteAccount() {
        User user = entityRepositoryHelper.getActiveUser();
        removeAsParticipantInEvents(user);
        removeUnits(user);
        removeEvents(user);
        userRepository.delete(user);
    }

    private void removeUnits(User user) {
        Set<Unit> units = unitRepository.findByAdminsIn(user);
        if(validator.isSingleAdmin(units)) {
            units.forEach(unit -> {
                removeMemberRelationship(unit);
                unitRepository.delete(unit);
            });
        }
    }

    private void removeMemberRelationship(Unit unit) {
        userRepository.findByMemberOfIn(unit).forEach(u -> {
            u.removeMemberOf(unit);
            userRepository.save(u);
        });
    }

    private void removeAsParticipantInEvents(User user) {
        eventRepository.findByParticipantsIn(user).forEach(event -> {
                event.removeParticipant(user);
                eventRepository.save(event);
        });
    }

    private void removeEvents(User user) {
        List<Event> events = eventRepository.findByCreatedBy(user);
        events.forEach(event -> {
            removeComments(event);
            removeRequests(event);
        });
        eventRepository.deleteAll(events);
    }

    private void removeRequests(Event event) {
        requestRepository.findByEvent(event)
                .forEach(request -> requestRepository.delete(request));
    }

    private void removeComments(Event event) {
        commentRepository.findByEvent(event)
                .forEach(comment -> commentRepository.delete(comment));
    }
}
