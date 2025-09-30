package io.github.dan7arievlis.marketplaceapi.controller.dto.mapper;

import io.github.dan7arievlis.marketplaceapi.controller.dto.base.BaseResponseDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.category.CategoryRequestDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.category.CategoryResponseDTO;
import io.github.dan7arievlis.marketplaceapi.model.Base;
import io.github.dan7arievlis.marketplaceapi.model.Category;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-25T15:49:33-0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 21.0.7 (Microsoft)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public Category requestToEntity(CategoryRequestDTO categoryRequestDTO) {
        if ( categoryRequestDTO == null ) {
            return null;
        }

        Category category = new Category();

        category.setName( categoryRequestDTO.name() );

        return category;
    }

    @Override
    public CategoryResponseDTO entityToResponse(Category category) {
        if ( category == null ) {
            return null;
        }

        String name = null;
        BaseResponseDTO metaData = null;

        name = category.getName();
        metaData = baseToBaseResponseDTO( category.getMetaData() );

        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO( name, metaData );

        return categoryResponseDTO;
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
