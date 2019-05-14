package se.elfu.sportprojectbackend.repository.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Request {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Column(columnDefinition = "BINARY(16)")
    private UUID requestNumber;
    private RequestStatus requestStatus;
    @OneToOne()
    private Event event;
    @OneToOne()
    private User sender;
    @OneToOne()
    private User receiver;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Message> messages;

    public void addMessage(Message message) {
        this.messages.add(message);
    }

}
