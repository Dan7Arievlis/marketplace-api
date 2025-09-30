package io.github.dan7arievlis.marketplaceapi.controller.dto.mapper;

import io.github.dan7arievlis.marketplaceapi.controller.dto.base.BaseResponseDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.cart.CartItemResponseDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.cart.CartItemUpdateStatusDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.cart.CartResponseDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.cart.PaymentCreateRequestDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.cart.PaymentResponseDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.product.ProductNestedResponseDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.provider.ProviderNestedResponseDTO;
import io.github.dan7arievlis.marketplaceapi.model.Base;
import io.github.dan7arievlis.marketplaceapi.model.Cart;
import io.github.dan7arievlis.marketplaceapi.model.CartItem;
import io.github.dan7arievlis.marketplaceapi.model.Payment;
import io.github.dan7arievlis.marketplaceapi.model.Product;
import io.github.dan7arievlis.marketplaceapi.model.User;
import io.github.dan7arievlis.marketplaceapi.model.enums.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-25T15:49:33-0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 21.0.7 (Microsoft)"
)
@Component
public class CartMapperImpl implements CartMapper {

    @Override
    public CartResponseDTO entityToResponse(Cart cart) {
        if ( cart == null ) {
            return null;
        }

        UUID owner = null;
        String status = null;
        BigDecimal total = null;
        BigDecimal paidAmount = null;
        LocalDateTime purchaseMoment = null;
        BaseResponseDTO metaData = null;

        owner = cartOwnerId( cart );
        status = map( cart.getStatus() );
        total = cart.getTotal();
        paidAmount = cart.getPaidAmount();
        purchaseMoment = cart.getPurchaseMoment();
        metaData = baseToBaseResponseDTO( cart.getMetaData() );

        CartResponseDTO cartResponseDTO = new CartResponseDTO( owner, total, paidAmount, purchaseMoment, status, metaData );

        return cartResponseDTO;
    }

    @Override
    public CartItemResponseDTO toCartItemResponse(CartItem cartItem) {
        if ( cartItem == null ) {
            return null;
        }

        ProductNestedResponseDTO product = null;
        String status = null;
        Integer quantity = null;
        BigDecimal subtotal = null;

        product = productToProductNestedResponseDTO( cartItem.getProduct() );
        status = map( cartItem.getItemStatus() );
        quantity = cartItem.getQuantity();
        subtotal = cartItem.getSubtotal();

        CartItemResponseDTO cartItemResponseDTO = new CartItemResponseDTO( quantity, status, subtotal, product );

        return cartItemResponseDTO;
    }

    @Override
    public PaymentResponseDTO paymentToResponse(Payment payment) {
        if ( payment == null ) {
            return null;
        }

        UUID cart = null;
        ProviderNestedResponseDTO provider = null;
        BigDecimal amount = null;
        LocalDateTime paidAt = null;
        LocalDateTime confirmedAt = null;
        PaymentStatus paymentStatus = null;
        BaseResponseDTO metaData = null;

        cart = paymentCartId( payment );
        provider = map( payment.getProvider() );
        amount = payment.getAmount();
        paidAt = payment.getPaidAt();
        confirmedAt = payment.getConfirmedAt();
        paymentStatus = payment.getPaymentStatus();
        metaData = baseToBaseResponseDTO( payment.getMetaData() );

        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO( cart, amount, paidAt, confirmedAt, paymentStatus, provider, metaData );

        return paymentResponseDTO;
    }

    @Override
    public Payment createRequestToPayment(PaymentCreateRequestDTO request) {
        if ( request == null ) {
            return null;
        }

        Payment payment = new Payment();

        payment.setAmount( request.amount() );
        payment.setExternalId( request.externalId() );
        payment.setAuthorizationCode( request.authorizationCode() );

        return payment;
    }

    @Override
    public CartItem updateToEntity(CartItemUpdateStatusDTO request) {
        if ( request == null ) {
            return null;
        }

        CartItem cartItem = new CartItem();

        return cartItem;
    }

    private UUID cartOwnerId(Cart cart) {
        User owner = cart.getOwner();
        if ( owner == null ) {
            return null;
        }
        return owner.getId();
    }

    protected BaseResponseDTO baseToBaseResponseDTO(Base base) {
        if ( base == null ) {
            return null;
        }

        UUID id = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;
        LocalDateTime deletedAt = null;
        Boolean active = null;
        UUID lastUpdatedByUser = null;

        id = base.getId();
        createdAt = base.getCreatedAt();
        updatedAt = base.getUpdatedAt();
        deletedAt = base.getDeletedAt();
        active = base.getActive();
        lastUpdatedByUser = base.getLastUpdatedByUser();

        BaseResponseDTO baseResponseDTO = new BaseResponseDTO( id, createdAt, updatedAt, deletedAt, active, lastUpdatedByUser );

        return baseResponseDTO;
    }

    private UUID productVendorId(Product product) {
        User vendor = product.getVendor();
        if ( vendor == null ) {
            return null;
        }
        return vendor.getId();
    }

    protected ProductNestedResponseDTO productToProductNestedResponseDTO(Product product) {
        if ( product == null ) {
            return null;
        }

        List<String> categories = null;
        UUID vendorId = null;
        String name = null;
        BigDecimal price = null;

        categories = map( product.getCategories() );
        vendorId = productVendorId( product );
        name = product.getName();
        price = product.getPrice();

        ProductNestedResponseDTO productNestedResponseDTO = new ProductNestedResponseDTO( name, categories, price, vendorId );

        return productNestedResponseDTO;
    }

    private UUID paymentCartId(Payment payment) {
        Cart cart = payment.getCart();
        if ( cart == null ) {
            return null;
        }
        return cart.getId();
    }
}
