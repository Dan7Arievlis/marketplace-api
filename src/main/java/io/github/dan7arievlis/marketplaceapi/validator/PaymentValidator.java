package io.github.dan7arievlis.marketplaceapi.validator;

import io.github.dan7arievlis.marketplaceapi.exceptions.DuplicatedRegisterException;
import io.github.dan7arievlis.marketplaceapi.model.CartItem;
import io.github.dan7arievlis.marketplaceapi.model.Payment;
import io.github.dan7arievlis.marketplaceapi.repository.CartItemRepository;
import io.github.dan7arievlis.marketplaceapi.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PaymentValidator {
    private final PaymentRepository repository;

    public void validate(Payment payment) {
        if(existsRegistered(payment))
            throw new DuplicatedRegisterException("Payment already exists.");
    }

    private boolean existsRegistered(Payment payment) {
        Optional<Payment> foundEntity = repository.findByAuthorizationCodeEquals(payment.getAuthorizationCode());
        if (payment.getId() == null)
            return foundEntity.isPresent();

        return foundEntity.isPresent() && !payment.getId().equals(foundEntity.get().getId());
    }
}