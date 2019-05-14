package se.elfu.sportprojectbackend.repository.model;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Event {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Column(columnDefinition = "BINARY(16)")
    private UUID eventNumber;
    private String name;
    @Column(length = 1000)
    private String description;
    private LocalDateTime eventStart;
    @ManyToMany(cascade = CascadeType.MERGE)
    private Set<User> participants;
    @ManyToOne()
    private User createdBy;
    @ManyToOne()
    private Unit byUnit;
    @OneToOne()
    private Sport sport;
    @OneToOne()
    private Location location;
    private boolean active;
    private String meetingPoint;

    public void addParticipant(User user) {
        this.participants.add(user);
    }

    public boolean removeParticipant(User user) {
        return this.participants.remove(user);
    }
}
