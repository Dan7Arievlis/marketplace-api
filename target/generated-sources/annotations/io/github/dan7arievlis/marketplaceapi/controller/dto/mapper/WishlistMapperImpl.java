package io.github.dan7arievlis.marketplaceapi.controller.dto.mapper;

import io.github.dan7arievlis.marketplaceapi.controller.dto.base.BaseResponseDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.product.ProductNestedResponseDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.wishlist.WishlistResponseDTO;
import io.github.dan7arievlis.marketplaceapi.model.Base;
import io.github.dan7arievlis.marketplaceapi.model.Product;
import io.github.dan7arievlis.marketplaceapi.model.User;
import io.github.dan7arievlis.marketplaceapi.model.Wishlist;
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
public class WishlistMapperImpl implements WishlistMapper {

    @Override
    public WishlistResponseDTO entityToResponse(Wishlist wishlist) {
        if ( wishlist == null ) {
            return null;
        }

        UUID owner = null;
        BaseResponseDTO metaData = null;

        owner = wishlistOwnerId( wishlist );
        metaData = baseToBaseResponseDTO( wishlist.getMetaData() );

        WishlistResponseDTO wishlistResponseDTO = new WishlistResponseDTO( owner, metaData );

        return wishlistResponseDTO;
    }

    @Override
    public ProductNestedResponseDTO toProductNested(Product product) {
        if ( product == null ) {
            return null;
        }

        UUID vendorId = null;
        List<String> categories = null;
        String name = null;
        BigDecimal price = null;

        vendorId = productVendorId( product );
        categories = map( product.getCategories() );
        name = product.getName();
        price = product.getPrice();

        ProductNestedResponseDTO productNestedResponseDTO = new ProductNestedResponseDTO( name, categories, price, vendorId );

        return productNestedResponseDTO;
    }

    private UUID wishlistOwnerId(Wishlist wishlist) {
        User owner = wishlist.getOwner();
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
}
