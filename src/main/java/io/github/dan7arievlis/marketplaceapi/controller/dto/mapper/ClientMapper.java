package io.github.dan7arievlis.marketplaceapi.controller.dto.mapper;

import io.github.dan7arievlis.marketplaceapi.controller.dto.client.ClientRequestDTO;
import io.github.dan7arievlis.marketplaceapi.model.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    Client requestToEntity(ClientRequestDTO clientRequestDTO);

}
