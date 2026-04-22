package dev.felippevaz.vzp_backend_scrumban.v1.modules.project.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRoleId implements Serializable {

    private Long projectId;
    private Long userId;
}
