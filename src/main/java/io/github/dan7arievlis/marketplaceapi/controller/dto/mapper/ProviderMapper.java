package io.github.dan7arievlis.marketplaceapi.controller.dto.mapper;

import io.github.dan7arievlis.marketplaceapi.controller.dto.provider.ProviderNestedResponseDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.provider.ProviderRequestDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.provider.ProviderResponseDTO;
import io.github.dan7arievlis.marketplaceapi.model.Provider;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProviderMapper {

    Provider requestToEntity(ProviderRequestDTO request);

    ProviderNestedResponseDTO toProviderNestedResponseDTO(Provider provider);

    ProviderResponseDTO entityToResponse(Provider provider);
}
