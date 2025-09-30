package io.github.dan7arievlis.marketplaceapi.validator;

import io.github.dan7arievlis.marketplaceapi.exceptions.DuplicatedRegisterException;
import io.github.dan7arievlis.marketplaceapi.model.Client;
import io.github.dan7arievlis.marketplaceapi.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClientValidator {
    private final ClientRepository repository;

    public void validate(Client client) {
        if (existsRegistered(client)) {
            throw new DuplicatedRegisterException("Client already exists");
        }
    }

    private boolean existsRegistered(Client client) {
        Optional<Client> foundEntity = repository.findByClientId(client.getClientId());

        if(client.getId() == null)
            return foundEntity.isPresent();

        return foundEntity.isPresent() && !client.getId().equals(foundEntity.get().getId());
    }
}
