package io.github.dan7arievlis.marketplaceapi.model;

import io.github.dan7arievlis.marketplaceapi.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Data
@Table(name="payments")
public class Payment extends Base {
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false, updatable = false)
    private Cart cart;

    @Column(name = "paid_at", nullable = false, updatable = false)
    private LocalDateTime paidAt = LocalDateTime.now();

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Provider provider;

    @Column(name = "external_id", nullable = false, unique = true)
    private String externalId;

    @Column(name = "authorization_code", nullable = false)
    private String authorizationCode;

    @Column(name = "status", nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
}
