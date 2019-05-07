package se.elfu.sportprojectbackend.repository;

import org.springframework.data.repository.CrudRepository;
import se.elfu.sportprojectbackend.repository.model.Message;

public interface MessageRepository extends CrudRepository<Message, Long> {
}
