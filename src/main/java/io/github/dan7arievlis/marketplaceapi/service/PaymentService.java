package io.github.dan7arievlis.marketplaceapi.service;

import io.github.dan7arievlis.marketplaceapi.controller.dto.cart.PaymentUpdateRequestDTO;
import io.github.dan7arievlis.marketplaceapi.exceptions.OperationNotAllowedException;
import io.github.dan7arievlis.marketplaceapi.model.*;
import io.github.dan7arievlis.marketplaceapi.model.enums.PaymentStatus;
import io.github.dan7arievlis.marketplaceapi.repository.PaymentRepository;
import io.github.dan7arievlis.marketplaceapi.security.SecurityService;
import io.github.dan7arievlis.marketplaceapi.service.component.OptionalSpecificationSearch;
import io.github.dan7arievlis.marketplaceapi.validator.PaymentValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService implements OptionalSpecificationSearch {
    private final PaymentRepository repository;
    private final PaymentValidator validator;
    private final SecurityService securityService;
    private final UserService userService;
    public final PasswordEncoder encoder;

    public Optional<Payment> findById(UUID id) {
        return repository.findById(id);
    }

    @Transactional
    public void save(Payment payment) {
        validator.validate(payment);
        payment.setUser(securityService.getLoggedUser());
        repository.save(payment);
    }

    @Transactional
    public void create(User user, Payment payment) {
        if (!user.getId().equals(securityService.getLoggedUser().getId()))
            throw new OperationNotAllowedException("Is necessary to be logged in to pay");

        restore(payment);
        payment.setAuthorizationCode(encoder.encode(payment.getAuthorizationCode()));
        save(payment);
    }

    @Transactional
    public void update(Payment payment) {
        if (payment.getId() == null)
            throw new IllegalArgumentException("Is necessary to have a saved payment in db to update it");

        save(payment);
    }

    @Transactional
    public void updateStatus(Payment payment, PaymentUpdateRequestDTO request, User user) {
        PaymentStatus status = PaymentStatus.valueOf(request.status());
        var cart = user.getCart();
        PaymentStatus newStatus = getNewStatus(payment, cart, status);

        payment.setPaymentStatus(newStatus);
        if (newStatus == PaymentStatus.CONFIRMED) {
            payment.setConfirmedAt(LocalDateTime.now());
        }
        if (newStatus == PaymentStatus.REFUNDED) {
            user.refund(payment.getAmount());
            payment.setConfirmedAt(null);
            userService.update(user);
            throw new OperationNotAllowedException("Payment refunded in total amount: " + payment.getAmount());
        }
        if (newStatus == PaymentStatus.FAILED) {
            throw new OperationNotAllowedException("Payment failed in total amount: " + payment.getAmount());
        }

        update(payment);
    }

    @Transactional
    protected PaymentStatus getNewStatus(Payment payment, Cart cart, PaymentStatus status) {
        var totalPaid = cart.getPaidAmount();

        PaymentStatus exceeded = totalPaid.add(payment.getAmount()).compareTo(cart.getTotal()) <= 0
                ? PaymentStatus.CONFIRMED
                : PaymentStatus.FAILED;

        return switch (payment.getPaymentStatus()) {
            case PENDING -> (status == PaymentStatus.AUTHORIZED)
                    ? exceeded
                    : PaymentStatus.REFUNDED;

            case AUTHORIZED -> (status == PaymentStatus.CANCELED)
                    ? PaymentStatus.REFUNDED
                    : exceeded;

            case CANCELED, FAILED, CONFIRMED, REFUNDED ->
                    throw new OperationNotAllowedException("Cannot update from " + payment.getPaymentStatus());
        };
    }

    @Transactional
    public void delete(Payment payment) {
        payment.delete();
        save(payment);
    }

    @Transactional
    public void restore(Payment payment) {
        payment.restore();
        save(payment);
    }

    public Page<Payment> findAll(Integer page, Integer pageSize) {
        return repository.findAll(PageRequest.of(page, pageSize));
    }
}
