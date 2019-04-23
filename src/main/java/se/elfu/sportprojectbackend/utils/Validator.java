package se.elfu.sportprojectbackend.utils;

import org.springframework.stereotype.Component;
import se.elfu.sportprojectbackend.controller.model.EventCreationDto;
import se.elfu.sportprojectbackend.exception.customException.BadRequestException;
import se.elfu.sportprojectbackend.repository.AccountRepository;
import se.elfu.sportprojectbackend.repository.UnitRepository;
import se.elfu.sportprojectbackend.repository.UserRepository;
import se.elfu.sportprojectbackend.repository.model.Comment;
import se.elfu.sportprojectbackend.repository.model.Event;
import se.elfu.sportprojectbackend.repository.model.Unit;
import se.elfu.sportprojectbackend.repository.model.User;
import se.elfu.sportprojectbackend.service.helper.EntityRepositoryHelper;

@Component
public final class Validator {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final UnitRepository unitRepository;
    private final EntityRepositoryHelper entityRepositoryHelper;

    public Validator(AccountRepository accountRepository, UserRepository userRepository, UnitRepository unitRepository, EntityRepositoryHelper entityRepositoryHelper) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.unitRepository = unitRepository;
        this.entityRepositoryHelper = entityRepositoryHelper;
    }

    public static void isEventActive(Event event) {
        if(!event.isActive()){
            throw new BadRequestException("Event is due date"); //TODO better exception
        }
    }

    public static void isEventFull(Event event) {
        if(event.getMaxParticipants() <= event.getParticipants().size()){
            throw new BadRequestException("Event is full");
        }
    }

    public static void isCreator(Event event, User user) {
        if(!event.getCreatedBy().getId().equals(user.getId())){
            throw new BadRequestException("Not owner of event, can' delete it");
        }
    }

    public static void isUserSameAsCommentator(User user, Comment comment) {
        if(!comment.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Can't delete others comment");

        }
    }

    public boolean isOwnerOfComment(Comment comment, User user){
        return comment.getUser().getId().equals(user.getId());
    }

    public void isEmailOccupied(String email) {
        accountRepository.findByEmail(email)
                .ifPresent(user -> { throw new BadRequestException("E-mail already in use");});
    }

    public void isUserNameOccupied(String alias) {
        userRepository.findByUsernameIgnoreCase(alias)
                .ifPresent(user -> { throw new BadRequestException("Alias already in use");});
    }

    public void isUnitNameOccupied(String name){
        unitRepository.findByNameIgnoreCase(name)
                .ifPresent(unit -> { throw new BadRequestException("Name already in use");});
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
}
