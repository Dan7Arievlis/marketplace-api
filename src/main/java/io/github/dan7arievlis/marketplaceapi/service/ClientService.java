package io.github.dan7arievlis.marketplaceapi.service;

import io.github.dan7arievlis.marketplaceapi.model.Client;
import io.github.dan7arievlis.marketplaceapi.repository.ClientRepository;
import io.github.dan7arievlis.marketplaceapi.validator.ClientValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository repository;
    private final ClientValidator validator;
    private final PasswordEncoder encoder;

    @Transactional
    public Client create(Client client) {
        client.setClientSecret(encoder.encode(client.getClientSecret()));
        validator.validate(client);
        return repository.save(client);
    }

    public Client findByClientId(String clientId) {
        return repository.findByClientId(clientId).orElseGet(null);
    }
}
