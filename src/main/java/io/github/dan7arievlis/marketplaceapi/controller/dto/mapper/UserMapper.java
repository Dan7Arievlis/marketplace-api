package io.github.dan7arievlis.marketplaceapi.controller.dto.mapper;

import io.github.dan7arievlis.marketplaceapi.controller.dto.user.UserCreateRequestDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.user.UserResponseDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.user.UserUpdateRequestDTO;
import io.github.dan7arievlis.marketplaceapi.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User requestToEntity(UserCreateRequestDTO userRequestDTO);

    User updateToEntity(UserUpdateRequestDTO userUpdateRequestDTO);

    UserResponseDTO entityToResponse(User user);
}
