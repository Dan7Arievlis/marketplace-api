package io.github.dan7arievlis.marketplaceapi.controller.dto.mapper;

import io.github.dan7arievlis.marketplaceapi.controller.dto.category.CategoryRequestDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.category.CategoryResponseDTO;
import io.github.dan7arievlis.marketplaceapi.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category requestToEntity(CategoryRequestDTO categoryRequestDTO);

    CategoryResponseDTO entityToResponse(Category category);
}
