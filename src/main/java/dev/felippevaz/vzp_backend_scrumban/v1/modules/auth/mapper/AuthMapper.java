package dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.mapper;

import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.dto.response.RegisterResponseDTO;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AuthMapper {

    RegisterResponseDTO toRegisterResponseDTO(User user);
}
