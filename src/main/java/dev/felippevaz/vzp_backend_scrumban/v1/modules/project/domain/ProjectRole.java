package dev.felippevaz.vzp_backend_scrumban.v1.modules.project.domain;

import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.domain.User;
import jakarta.persistence.*;

@Entity
public class ProjectRole {

    @EmbeddedId
    private ProjectRoleId projectRoleId;

    @ManyToOne
    @MapsId("projectId")
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ProjectRoleType projectRoleType;
}
