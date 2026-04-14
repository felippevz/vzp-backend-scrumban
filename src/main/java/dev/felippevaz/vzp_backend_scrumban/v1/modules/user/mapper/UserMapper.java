package dev.felippevaz.vzp_backend_scrumban.v1.modules.user.mapper;

import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.domain.User;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.dto.request.UserUpdateDTO;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.dto.response.UserResponseDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt",  ignore = true)
    @Mapping(target = "status",  ignore = true)
    @Mapping(target = "authorities" ,  ignore = true)
    @Mapping(target = "role",   ignore = true)
    @Mapping(target = "password",   ignore = true)
    void updateFromDTO(UserUpdateDTO userUpdateDTO, @MappingTarget User user);

    UserResponseDTO toResponseDTO(User user);
}
