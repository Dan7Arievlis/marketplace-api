package io.github.dan7arievlis.marketplaceapi.controller.dto.mapper;

import io.github.dan7arievlis.marketplaceapi.controller.dto.base.BaseResponseDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.product.ProductCreateRequestDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.product.ProductResponseDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.product.ProductUpdateRequestDTO;
import io.github.dan7arievlis.marketplaceapi.model.Base;
import io.github.dan7arievlis.marketplaceapi.model.Product;
import io.github.dan7arievlis.marketplaceapi.model.User;
import io.github.dan7arievlis.marketplaceapi.service.CategoryService;
import java.math.BigDecimal;
import java.time.LocalDate;
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
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product requestToEntity(ProductCreateRequestDTO productCreateRequestDTO, CategoryService service) {
        if ( productCreateRequestDTO == null ) {
            return null;
        }

        Product product = new Product();

        if ( productCreateRequestDTO.vendorId() != null ) {
            product.setVendor( map( UUID.fromString( productCreateRequestDTO.vendorId() ) ) );
        }
        product.setCategories( map( productCreateRequestDTO.categories(), service ) );
        product.setName( productCreateRequestDTO.name() );
        product.setDescription( productCreateRequestDTO.description() );
        product.setPrice( productCreateRequestDTO.price() );
        product.setFabDate( productCreateRequestDTO.fabDate() );
        product.setExpDate( productCreateRequestDTO.expDate() );

        return product;
    }

    @Override
    public Product updateToEntity(ProductUpdateRequestDTO request, CategoryService service) {
        if ( request == null ) {
            return null;
        }

        Product product = new Product();

        product.setVendor( map( request.vendorId() ) );
        product.setCategories( map( request.categories(), service ) );
        product.setName( request.name() );
        product.setDescription( request.description() );
        product.setPrice( request.price() );
        product.setFabDate( request.fabDate() );
        product.setExpDate( request.expDate() );

        return product;
    }

    @Override
    public ProductResponseDTO entityToResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        UUID vendorId = null;
        List<String> categories = null;
        String name = null;
        String description = null;
        BigDecimal price = null;
        LocalDate publishDate = null;
        LocalDate fabDate = null;
        LocalDate expDate = null;
        BaseResponseDTO metaData = null;

        vendorId = productVendorId( product );
        categories = map( product.getCategories() );
        name = product.getName();
        description = product.getDescription();
        price = product.getPrice();
        publishDate = product.getPublishDate();
        fabDate = product.getFabDate();
        expDate = product.getExpDate();
        metaData = baseToBaseResponseDTO( product.getMetaData() );

        ProductResponseDTO productResponseDTO = new ProductResponseDTO( name, description, categories, price, vendorId, publishDate, fabDate, expDate, metaData );

        return productResponseDTO;
    }

    private UUID productVendorId(Product product) {
        User vendor = product.getVendor();
        if ( vendor == null ) {
            return null;
        }
        return vendor.getId();
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
}
