package dev.felippevaz.vzp_backend_scrumban.v1.modules.project.domain;

import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.domain.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private int wipLimit;
}
