package io.github.dan7arievlis.marketplaceapi.repository;

import io.github.dan7arievlis.marketplaceapi.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID>, JpaSpecificationExecutor<Payment> {
    Optional<Payment> findByAuthorizationCodeEquals(String authorizationCode);
}
