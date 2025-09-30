package io.github.dan7arievlis.marketplaceapi.repository;

import io.github.dan7arievlis.marketplaceapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    Optional<User> findByUsernameLikeIgnoreCase(String username);

    Optional<User> findByUsernameIgnoreCaseOrEmailEquals(String username, String email);

    Optional<User> findByEmail(String email);
}
