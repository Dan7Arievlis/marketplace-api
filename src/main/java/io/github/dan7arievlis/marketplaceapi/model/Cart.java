package io.github.dan7arievlis.marketplaceapi.model;

import io.github.dan7arievlis.marketplaceapi.model.enums.CartStatus;
import io.github.dan7arievlis.marketplaceapi.model.enums.ItemStatus;
import io.github.dan7arievlis.marketplaceapi.model.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(exclude = {"items", "payments"})
@Data
@Table(name="carts")
public class Cart extends Base {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User owner;

    @Column(name = "purchase_moment")
    private LocalDateTime purchaseMoment;

    @Column(name = "status", nullable = false)
    private CartStatus status = CartStatus.OPEN;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Payment> payments = new HashSet<>();

    public BigDecimal getTotal() {
        return items == null ? BigDecimal.ZERO :
            items.stream()
                    .filter(item -> List.of(ItemStatus.OPEN, ItemStatus.ALLOCATED, ItemStatus.SHIPPED).contains(item.getItemStatus()))
                    .filter(item -> {
                        if (purchaseMoment == null)
                            return true;
                        return purchaseMoment.isBefore(item.getUpdatedAt());
                    })
                    .map(CartItem::getSubtotal)
                    .filter(java.util.Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getPaidAmount() {
        return getPayments().stream()
                .filter(payment -> {
                    if (purchaseMoment == null)
                        return true;
                    if (payment.getConfirmedAt() == null)
                        return false;
                    return purchaseMoment.isBefore(payment.getConfirmedAt());
                })
                .filter(p -> p.getPaymentStatus().equals(PaymentStatus.CONFIRMED))
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public void addPayment(Payment payment) {
        payment.setCart(this);
        payments.add(payment);
    }

    @Transactional
    public void addItem(CartItem item) {
        item.setCart(this);
        items.add(item);
    }

    @Transactional
    public void removeItem(CartItem item) {
        items.remove(item);
    }

    public Optional<CartItem> itemByProduct(Product product) {
        return items.stream().filter(item -> item.getProduct().equals(product) && item.getItemStatus().equals(ItemStatus.OPEN)).findFirst();
    }
}
