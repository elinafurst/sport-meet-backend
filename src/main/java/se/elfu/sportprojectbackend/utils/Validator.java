package se.elfu.sportprojectbackend.utils;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import se.elfu.sportprojectbackend.controller.model.EventCreationDto;
import se.elfu.sportprojectbackend.exception.customException.BadRequestException;
import se.elfu.sportprojectbackend.exception.customException.ListEmptyException;
import se.elfu.sportprojectbackend.repository.*;
import se.elfu.sportprojectbackend.repository.model.*;
import se.elfu.sportprojectbackend.service.helper.EntityRepositoryHelper;

@Component
public final class Validator {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final UnitRepository unitRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;
    private final EntityRepositoryHelper entityRepositoryHelper;

    public Validator(AccountRepository accountRepository, UserRepository userRepository, UnitRepository unitRepository, LocationRepository locationRepository, RequestRepository requestRepository, EntityRepositoryHelper entityRepositoryHelper) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.unitRepository = unitRepository;
        this.locationRepository = locationRepository;
        this.requestRepository = requestRepository;
        this.entityRepositoryHelper = entityRepositoryHelper;
    }

    public static void isEventActive(Event event) {
        if(!event.isActive()){
            throw new BadRequestException("Annonsen har passerat");
        }
    }

    public static void isEventFull(Event event) {
        if(event.getMaxParticipants() <= event.getParticipants().size()){
            throw new BadRequestException("Fullt antal");
        }
    }

    public static void isCreator(Event event, User user) {
        if(!event.getCreatedBy().getId().equals(user.getId())){
            throw new BadRequestException("Kan inte radera annons, inte ägare");
        }
    }

    public static void isUserSameAsCommentator(User user, Comment comment) {
        if(!comment.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Kan inte radera andras kommenterar");
        }
    }

    public static boolean isSameUser(Long user, Long activeUser) {
        return (user.equals(activeUser)) ? true : false;
    }

    public static void isReceiver(Long activeUser, Long receiver) {
        if(!activeUser.equals(receiver)){
            throw new BadRequestException("Du är inte ägare av annonsen");
        }
    }

    public static void isCreatorOfEvent(User user, User eventOwner) {
        if(user.getId().equals(eventOwner.getId())){
            throw new BadRequestException("Kan inte skicka medverkande förfrågande på din egna annons");
        }
    }

    public static void isEmpty(int size) {
        if(size == 0){
            throw new ListEmptyException();
        }
    }

    public void isRequestAlreadySent(Event event, User user) {
        requestRepository.findByEventAndSender(event, user)
                .ifPresent(sender -> { throw new BadRequestException("Du kan inte skicka medverkande förfrågan mer än 1 gång");});
    }

    public boolean isOwnerOfComment(Comment comment, User user){
        return comment.getUser().getId().equals(user.getId());
    }

    public void isEmailOccupied(String email) {
        accountRepository.findByEmail(email)
                .ifPresent(user -> { throw new BadRequestException("E-mail upptaget");});
    }

    public void isUserNameOccupied(String alias) {
        userRepository.findByUsernameIgnoreCase(alias)
                .ifPresent(user -> { throw new BadRequestException("Användarnamn upptaget");});
    }

    public void isUnitNameOccupied(String name){
        unitRepository.findByNameIgnoreCase(name)
                .ifPresent(unit -> { throw new BadRequestException("Gruppnamn är upptaget");});
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
        if(locationRepository.findByCity(city).size() != 0) {
            throw new BadRequestException("Stad finns redan, uppdatera om du vill lägga till nytt område");
        }
    }

    public static boolean isActiveUserMemberOfUnit(Unit unit, Long id) {
        return unit.getMembers().stream().anyMatch(member -> member.getId().equals(id));
    }
}
