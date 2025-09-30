package io.github.dan7arievlis.marketplaceapi.controller.dto.mapper;

import io.github.dan7arievlis.marketplaceapi.controller.dto.base.BaseResponseDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.provider.ProviderNestedResponseDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.provider.ProviderRequestDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.provider.ProviderResponseDTO;
import io.github.dan7arievlis.marketplaceapi.model.Base;
import io.github.dan7arievlis.marketplaceapi.model.Provider;
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
public class ProviderMapperImpl implements ProviderMapper {

    @Override
    public Provider requestToEntity(ProviderRequestDTO request) {
        if ( request == null ) {
            return null;
        }

        Provider provider = new Provider();

        provider.setName( request.name() );

        return provider;
    }

    @Override
    public ProviderNestedResponseDTO toProviderNestedResponseDTO(Provider provider) {
        if ( provider == null ) {
            return null;
        }

        String name = null;
        UUID id = null;

        name = provider.getName();
        id = provider.getId();

        ProviderNestedResponseDTO providerNestedResponseDTO = new ProviderNestedResponseDTO( name, id );

        return providerNestedResponseDTO;
    }

    @Override
    public ProviderResponseDTO entityToResponse(Provider provider) {
        if ( provider == null ) {
            return null;
        }

        String name = null;
        BaseResponseDTO metaData = null;

        name = provider.getName();
        metaData = baseToBaseResponseDTO( provider.getMetaData() );

        ProviderResponseDTO providerResponseDTO = new ProviderResponseDTO( name, metaData );

        return providerResponseDTO;
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
