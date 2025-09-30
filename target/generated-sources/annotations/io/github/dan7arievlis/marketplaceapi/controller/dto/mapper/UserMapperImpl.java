package io.github.dan7arievlis.marketplaceapi.controller.dto.mapper;

import io.github.dan7arievlis.marketplaceapi.controller.dto.base.BaseResponseDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.user.UserCreateRequestDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.user.UserResponseDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.user.UserUpdateRequestDTO;
import io.github.dan7arievlis.marketplaceapi.model.Base;
import io.github.dan7arievlis.marketplaceapi.model.User;
import io.github.dan7arievlis.marketplaceapi.model.enums.UserRoles;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-25T15:49:33-0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 21.0.7 (Microsoft)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User requestToEntity(UserCreateRequestDTO userRequestDTO) {
        if ( userRequestDTO == null ) {
            return null;
        }

        User user = new User();

        user.setUsername( userRequestDTO.username() );
        user.setEmail( userRequestDTO.email() );
        user.setPassword( userRequestDTO.password() );
        user.setFirstName( userRequestDTO.firstName() );
        user.setLastName( userRequestDTO.lastName() );
        user.setBirthDate( userRequestDTO.birthDate() );
        user.setBalance( userRequestDTO.balance() );
        user.setAddress( userRequestDTO.address() );
        user.setPhone( userRequestDTO.phone() );
        if ( userRequestDTO.role() != null ) {
            user.setRole( Enum.valueOf( UserRoles.class, userRequestDTO.role() ) );
        }

        return user;
    }

    @Override
    public User updateToEntity(UserUpdateRequestDTO userUpdateRequestDTO) {
        if ( userUpdateRequestDTO == null ) {
            return null;
        }

        User user = new User();

        user.setUsername( userUpdateRequestDTO.username() );
        user.setEmail( userUpdateRequestDTO.email() );
        user.setFirstName( userUpdateRequestDTO.firstName() );
        user.setLastName( userUpdateRequestDTO.lastName() );
        user.setBirthDate( userUpdateRequestDTO.birthDate() );
        user.setBalance( userUpdateRequestDTO.balance() );
        user.setAddress( userUpdateRequestDTO.address() );
        user.setPhone( userUpdateRequestDTO.phone() );
        if ( userUpdateRequestDTO.role() != null ) {
            user.setRole( Enum.valueOf( UserRoles.class, userUpdateRequestDTO.role() ) );
        }

        return user;
    }

    @Override
    public UserResponseDTO entityToResponse(User user) {
        if ( user == null ) {
            return null;
        }

        String username = null;
        String email = null;
        String firstName = null;
        String lastName = null;
        String fullName = null;
        LocalDate birthDate = null;
        Integer age = null;
        BigDecimal balance = null;
        String address = null;
        String phone = null;
        String role = null;
        BaseResponseDTO metaData = null;

        username = user.getUsername();
        email = user.getEmail();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        fullName = user.getFullName();
        birthDate = user.getBirthDate();
        age = user.getAge();
        balance = user.getBalance();
        address = user.getAddress();
        phone = user.getPhone();
        if ( user.getRole() != null ) {
            role = user.getRole().name();
        }
        metaData = baseToBaseResponseDTO( user.getMetaData() );

        UserResponseDTO userResponseDTO = new UserResponseDTO( username, email, firstName, lastName, fullName, birthDate, age, balance, address, phone, role, metaData );

        return userResponseDTO;
    }

    protected BaseResponseDTO baseToBaseResponseDTO(Base base) {
        if ( base == null ) {
            return null;
        }

        UUID id = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;
        LocalDateTime deletedAt = null;
        Boolean active = null;
        UUID lastUpdatedByUser = null;

        id = base.getId();
        createdAt = base.getCreatedAt();
        updatedAt = base.getUpdatedAt();
        deletedAt = base.getDeletedAt();
        active = base.getActive();
        lastUpdatedByUser = base.getLastUpdatedByUser();

        BaseResponseDTO baseResponseDTO = new BaseResponseDTO( id, createdAt, updatedAt, deletedAt, active, lastUpdatedByUser );

        return baseResponseDTO;
    }
}
