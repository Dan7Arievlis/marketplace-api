package io.github.dan7arievlis.marketplaceapi.controller.dto.mapper;

import io.github.dan7arievlis.marketplaceapi.controller.dto.client.ClientRequestDTO;
import io.github.dan7arievlis.marketplaceapi.model.Client;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-25T15:49:32-0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 21.0.7 (Microsoft)"
)
@Component
public class ClientMapperImpl implements ClientMapper {

    @Override
    public Client requestToEntity(ClientRequestDTO clientRequestDTO) {
        if ( clientRequestDTO == null ) {
            return null;
        }

        Client client = new Client();

        client.setClientId( clientRequestDTO.clientId() );
        client.setClientSecret( clientRequestDTO.clientSecret() );
        client.setRedirectURI( clientRequestDTO.redirectURI() );
        client.setScope( clientRequestDTO.scope() );

        return client;
    }
}
