package se.elfu.sportprojectbackend.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import se.elfu.sportprojectbackend.repository.model.Event;
import se.elfu.sportprojectbackend.repository.model.Request;
import se.elfu.sportprojectbackend.repository.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RequestRepository extends CrudRepository<Request, Long> {

    List<Request> findDistinctBySenderOrReceiver(User sender, User receiver, Pageable param);

    Optional<Request> findByRequestNumber(UUID requestNumber);

    Optional<Request> findByEventAndSender(Event event, User user);

    List<Request> findByEvent(Event event);

}
