package se.elfu.sportprojectbackend.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Unit {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Column(columnDefinition = "BINARY(16)")
    private UUID unitNumber;
    private String name;
    private String description;
    @ManyToMany(mappedBy = "adminOf")
    private Set<User> admins;
    @ManyToMany(mappedBy = "memberOf")
    private Set<User> members;

}
