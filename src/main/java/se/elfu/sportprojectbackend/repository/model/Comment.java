package se.elfu.sportprojectbackend.repository.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Comment {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Column(columnDefinition = "BINARY(16)")
    private UUID commentNumber;
    @Column(length = 1000)
    private String comment;
    private LocalDateTime timeStamp;
    @ManyToOne()
    private User user;
    @ManyToOne()
    private Event event;
}
