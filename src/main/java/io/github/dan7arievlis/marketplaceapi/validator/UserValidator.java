package io.github.dan7arievlis.marketplaceapi.validator;

import io.github.dan7arievlis.marketplaceapi.exceptions.DuplicatedRegisterException;
import io.github.dan7arievlis.marketplaceapi.model.User;
import io.github.dan7arievlis.marketplaceapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository repository;

    public void validate(User user) {
        if(existsRegistered(user))
            throw new DuplicatedRegisterException("User already exists.");
    }

    private boolean existsRegistered(User user) {
        Optional<User> foundEntity = repository.findByUsernameIgnoreCaseOrEmailEquals(user.getUsername(), user.getEmail());
        if (user.getId() == null)
            return foundEntity.isPresent();

        return foundEntity.isPresent() && !user.getId().equals(foundEntity.get().getId());
    }
}
