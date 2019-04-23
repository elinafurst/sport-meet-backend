package se.elfu.sportprojectbackend.service.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import se.elfu.sportprojectbackend.exception.customException.BadRequestException;
import se.elfu.sportprojectbackend.exception.customException.NotFoundException;
import se.elfu.sportprojectbackend.repository.*;
import se.elfu.sportprojectbackend.repository.model.*;

import java.util.UUID;

@Component
@Slf4j
public class EntityRepositoryHelper {

    private static String ROLE_USER = "ROLE_USER";

    private final EventRepository eventRepository;
    private final SportRepository sportRepository;
    private final UnitRepository unitRepository;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final AccountRepository accountRepository;
    private final CommentRepository commentRepository;
    private final LocationRepository locationRepository;

    public EntityRepositoryHelper(EventRepository eventRepository, SportRepository sportRepository, UnitRepository unitRepository, UserRepository userRepository, AuthorityRepository authorityRepository, AccountRepository accountRepository, CommentRepository commentRepository, LocationRepository locationRepository) {
        this.eventRepository = eventRepository;
        this.authorityRepository = authorityRepository;
        this.sportRepository = sportRepository;
        this.unitRepository = unitRepository;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.commentRepository = commentRepository;
        this.locationRepository = locationRepository;
    }

    public String getCurrentEmail(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null){
            throw new RuntimeException("Can't make request, invalid token"); //TODO exception
        }
        String email = auth.getName().toLowerCase();
        log.info("Active user is: {}", email);
        return email;
    }

    public User getActiveUser() {
        return getUser(getCurrentEmail());
    }

    public User getUser(UUID userNumber) {
        return userRepository.findByUserNumber(userNumber)
                .orElseThrow(() -> new NotFoundException("User", userNumber.toString()));
    }

    public User getUser(String userName) {
        return userRepository.findByAccountEmail(userName)
                .orElseThrow(() -> new NotFoundException("User", userName));
    }

    public Authority getAuthority(){
        return authorityRepository.findByAuthority(ROLE_USER);
    }

    public Sport getSport(String sport) {
        return sportRepository.findByNameIgnoreCase(sport)
                .orElseThrow(() -> new BadRequestException("Sport don't exists")); //TODO
    }

    public Account getAccount(String username){
        return accountRepository.findByEmail(username)
                .orElseThrow(() -> new NotFoundException("User", username));
    }

    public Unit getUnit(UUID unitNumber) {
        return unitRepository.findByUnitNumber(unitNumber)
                .orElseThrow(() -> new NotFoundException("Unit", unitNumber.toString()));
    }

    public Event getEvent(UUID eventNumber) {
        return eventRepository.findByEventNumber(eventNumber)
                .orElseThrow(() -> new NotFoundException("Event", eventNumber.toString()));
    }

    public Comment getComment(UUID commentNumber) {
        return commentRepository.findByCommentNumber(commentNumber)
                .orElseThrow(() -> new NotFoundException("Comment", commentNumber.toString()));
    }
}
