package io.github.dan7arievlis.marketplaceapi.controller;

import io.github.dan7arievlis.marketplaceapi.controller.dto.cart.*;
import io.github.dan7arievlis.marketplaceapi.controller.dto.mapper.CartMapper;
import io.github.dan7arievlis.marketplaceapi.model.Cart;
import io.github.dan7arievlis.marketplaceapi.model.CartItem;
import io.github.dan7arievlis.marketplaceapi.model.Payment;
import io.github.dan7arievlis.marketplaceapi.model.User;
import io.github.dan7arievlis.marketplaceapi.service.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("users/{id}/cart")
@RequiredArgsConstructor
public class CartController implements GenericController {
    private final CartService service;
    private final UserService userService;
    private final CartMapper mapper;
    private final CategoryService categoryService;
    private final CartItemService cartItemService;
    private final PaymentService paymentService;

    // details GET /
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CartResponseDTO> findById(@PathVariable String id) {
        return userService.findById(UUID.fromString(id))
                .map(user -> {
                    var optional = service.findById(user.getCart().getId());
                    return optional.map(cart ->
                            ResponseEntity.ok(mapper.entityToResponse(cart))
                    ).orElse(null);
                })
                .orElseThrow(() -> new EntityNotFoundException("Cart not found with id: " + id));
    }

    // update cart PATCH /
    @PatchMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody @Valid CartUpdateRequestDTO request) {
        return userService.findById(UUID.fromString(id))
                .map(user -> {
                    service.updateStatus(user.getCart(), request);
                    return ResponseEntity.noContent().build();
                })
                .orElseThrow( () -> new EntityNotFoundException("User not found."));
    }

    // filter items GET /items
    @GetMapping("items")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getItems(
           @PathVariable String id,
           @RequestParam(name = "name", required = false)
           String name,
           @RequestParam(name = "category", required = false)
           String category,
           @RequestParam(value = "page", defaultValue = "0")
           Integer page,
           @RequestParam(value = "page-size", defaultValue = "10")
           Integer pageSize
    ) {
        return userService.findById(UUID.fromString(id))
                .map(user -> {
                    var optional = service.findById(user.getCart().getId());
                    var categoryEntity = (category == null || category.isBlank())
                            ? null
                            : categoryService.findByName(category).orElse(null);

                    Page<CartItemResponseDTO> items = optional
                            .map(cart ->
                                service.filterItems(name, categoryEntity, page, pageSize, cart)
                                    .map(mapper::toCartItemResponse)
                    ).orElse(null);
                    return ResponseEntity.ok(items);
                })
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    // update cart items PATCH /items
    @PatchMapping("items")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> updateProducts(@PathVariable String id, @RequestBody @Valid CartItemUpdateRequestDTO request) {
        return userService.findById(UUID.fromString(id))
                .map(user -> {
                    service.updateItems(user, request);
                    return ResponseEntity.noContent().build();
                })
                .orElseThrow( () -> new EntityNotFoundException("User not found."));
    }

    // update item PATCH /items/{ITEM_ID}
    @PatchMapping("items/{itemId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> update(@PathVariable String id, @PathVariable String itemId, @RequestBody @Valid CartItemUpdateStatusDTO request) {
        return userService.findById(UUID.fromString(id))
                .map(user -> {
                    CartItem aux = mapper.updateToEntity(request);
                    cartItemService.fullUpdate(aux, itemId);
                    return ResponseEntity.noContent().build();
                })
                .orElseThrow( () -> new EntityNotFoundException("User not found with id: " + id));
    }

    // create payment POST /payments
    @PostMapping("payments")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> create(@PathVariable String id, @RequestBody @Valid PaymentCreateRequestDTO request) {
        return userService.findById(UUID.fromString(id))
                .map(user -> {
                    Payment payment = mapper.createRequestToPayment(request);
                    service.addPayment(user, payment, request);

                    URI location = generateHeaderLocation(payment.getId());
                    return ResponseEntity.created(location).build();
                })
                .orElseThrow(() -> new EntityNotFoundException("User not found."));
    }

    // details payment GET /payments/PAYMENT_ID
    @GetMapping("payments/{paymentId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PaymentResponseDTO> findById(@PathVariable String id, @PathVariable String paymentId) {
        return userService.findById(UUID.fromString(id))
                .map(user -> {
                    var optional = paymentService.findById(UUID.fromString(paymentId));
                    return optional.map(payment ->
                            ResponseEntity.ok(mapper.paymentToResponse(payment))
                    ).orElse(null);
                })
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + id));
    }

    // list payments GET /payments
    @GetMapping("payments")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> getPayments(@PathVariable String id,
            @RequestParam(value = "page", defaultValue = "0")
            Integer page,
            @RequestParam(value = "page-size", defaultValue = "10")
            Integer pageSize
    ) {
        return userService.findById(UUID.fromString(id))
                .map(user -> {
                    Page<PaymentResponseDTO> result = paymentService.findAll(page, pageSize)
                            .map(mapper::paymentToResponse);

                    return ResponseEntity.ok(result);
                })
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + id));
    }

    // update payment status PATCH /payments/PAYMENT_ID
    @PatchMapping("payments/{paymentId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> updatePayment(@PathVariable String id, @PathVariable String paymentId, @RequestBody @Valid PaymentUpdateRequestDTO request) {
        return userService.findById(UUID.fromString(id))
                .map(user -> {
                    Optional<Payment> payment = paymentService.findById(UUID.fromString(paymentId));
                    payment.ifPresent(p -> paymentService.updateStatus(p, request, user));
                    service.update(user.getCart());

                    return ResponseEntity.noContent().build();
                })
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + id));
    }
}
