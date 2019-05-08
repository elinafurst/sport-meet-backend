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
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Column(columnDefinition = "BINARY(16)")
    private UUID userNumber;
    private String firstname;
    private String lastname;
    private String username;
    @Column(length = 200)

    private String description;
    @OneToOne(cascade = CascadeType.ALL)
    private Account account;
    @ManyToMany
    private Set<Unit> adminOf;
    @ManyToMany
    private Set<Unit> memberOf;
    @ManyToMany(mappedBy = "participants")
    private Set<Event> events;
    private boolean active;

    public void addAdminOf(Unit unit) {
        this.adminOf.add(unit);
    }

    public void addMemberOf(Unit unit) {this.memberOf.add(unit);}

    public void removeMemberOf(Unit unit){
        this.memberOf.remove(unit);
    }
}
