package se.elfu.sportprojectbackend.repository.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Location {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String city;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Area area;
}
