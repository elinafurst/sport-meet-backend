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
    @OneToOne
    private Event event;
    @OneToOne
    private User sender;
    @OneToOne
    private User receiver;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Message> messages;
    private boolean isRead;

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public void setRequestStatus(RequestStatus requestStatus){
        this.requestStatus = requestStatus;
    }
}
