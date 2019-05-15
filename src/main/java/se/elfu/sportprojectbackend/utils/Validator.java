package se.elfu.sportprojectbackend.utils;

import org.springframework.stereotype.Component;
import se.elfu.sportprojectbackend.controller.model.events.EventCreationDto;
import se.elfu.sportprojectbackend.exception.customException.BadRequestException;
import se.elfu.sportprojectbackend.exception.customException.ListEmptyException;
import se.elfu.sportprojectbackend.repository.*;
import se.elfu.sportprojectbackend.repository.model.*;
import se.elfu.sportprojectbackend.service.helper.EntityRepositoryHelper;

import java.time.LocalDateTime;
import java.util.Set;

@Component
public final class Validator {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final UnitRepository unitRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;
    private final SportRepository sportRepository;
    private final EntityRepositoryHelper entityRepositoryHelper;

    public Validator(AccountRepository accountRepository, UserRepository userRepository, UnitRepository unitRepository, LocationRepository locationRepository, SportRepository sportRepository, RequestRepository requestRepository, EntityRepositoryHelper entityRepositoryHelper) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.unitRepository = unitRepository;
        this.locationRepository = locationRepository;
        this.requestRepository = requestRepository;
        this.sportRepository = sportRepository;
        this.entityRepositoryHelper = entityRepositoryHelper;
    }

    public static void isEventActive(Event event) {
        if(!event.isActive()){
            throw new BadRequestException("Event is due date");
        }
    }

    public static void isCreator(Event event, User user) {
        if(!event.getCreatedBy().getId().equals(user.getId())){
            throw new BadRequestException("Not creator of event");
        }
    }

    public static void isUserSameAsCommentator(User user, Comment comment) {
        if(!comment.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Can't delete other users comments");
        }
    }

    public static boolean isSameUser(Long user, Long activeUser) {
        return (user.equals(activeUser)) ? true : false;
    }

    public static void isReceiver(Long activeUser, Long receiver) {
        if(!activeUser.equals(receiver)){
            throw new BadRequestException("Not creator of event");
        }
    }

    public static void isCreatorOfEvent(User user, User eventOwner) {
        if(user.getId().equals(eventOwner.getId())){
            throw new BadRequestException("Cant make request on your own event");
        }
    }

    public static void isEmpty(int size) {
        if(size == 0){
            throw new ListEmptyException();
        }
    }

    public static void isEventStartInFuture(LocalDateTime eventStart) {
        if(eventStart.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Invalid startdate");
        }
    }

    public static void isTokenExpired(LocalDateTime expiration) {
        if(expiration.isBefore(LocalDateTime.now())){
            throw new BadRequestException("Reset token expired");
        }
    }

    public static boolean isActiveInFuture(LocalDateTime eventStart) {
        return (eventStart.isBefore(LocalDateTime.now())) ? false : true;
    }

    public void isRequestAlreadySent(Event event, User user) {
        requestRepository.findByEventAndSender(event, user)
                .ifPresent(sender -> { throw new BadRequestException("Cant make same request");});
    }

    public boolean isOwnerOfComment(Comment comment, User user){
        return comment.getUser().getId().equals(user.getId());
    }

    public void isEmailOccupied(String email) {
        accountRepository.findByEmail(email)
                .ifPresent(user -> { throw new BadRequestException("E-mail occupied");});
    }

    public void isUserNameOccupied(String alias) {
        userRepository.findByUsernameIgnoreCase(alias)
                .ifPresent(user -> { throw new BadRequestException("Username occupied");});
    }

    public void isUnitNameOccupied(String name){
        unitRepository.findByNameIgnoreCase(name)
                .ifPresent(unit -> { throw new BadRequestException("Groupname occupied");});
    }

    public Unit isEventRelatedToUnit(EventCreationDto eventCreationDto) {
        if(eventCreationDto.getByUnit() != null) {
            return entityRepositoryHelper.getUnit(eventCreationDto.getByUnit());
        }
        return null;
    }

    public void isUsernameChanged(String originalUsername, String incomingUsername) {
        if(!originalUsername.equalsIgnoreCase(incomingUsername)){ //If user changed cases don't validate if it occupied
            isUserNameOccupied(incomingUsername);
        }
    }

    public void isCityInDatabase(String city) {
        if(locationRepository.countByCity(city) != 0) {
            throw new BadRequestException("Ciy already exists, update if you want to add new area");
        }
    }
    public void isSportInDatabase(String name){
        sportRepository.findByNameIgnoreCase(name)
                .ifPresent(sport -> { throw new BadRequestException("Sport allredy exists"); });
    }

    public static boolean isActiveUserMemberOfUnit(Unit unit, Long id) {
        return unit.getMembers().stream().anyMatch(member -> member.getId().equals(id));
    }

    public boolean isSingleAdmin(Set<Unit> units) {
        return units.size() > 1 ? false : true;
    }
}
