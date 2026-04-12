package dev.felippevaz.vzp_backend_scrumban.v1.modules.user.mapper;

import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.domain.User;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.dto.request.UserRequestDTO;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.dto.response.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt",  ignore = true)
    @Mapping(target = "status",  ignore = true)
    @Mapping(target = "authorities" ,  ignore = true)
    @Mapping(target = "role",   ignore = true)
    User toEntity(UserRequestDTO userRequestDTO);

    UserResponseDTO toResponseDTO(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt",  ignore = true)
    @Mapping(target = "status",  ignore = true)
    @Mapping(target = "password",  ignore = true)
    @Mapping(target = "authorities" ,  ignore = true)
    @Mapping(target = "role",   ignore = true)
    void updateEntityFromDTO(UserRequestDTO userRequestDTO, @MappingTarget User user);
}
