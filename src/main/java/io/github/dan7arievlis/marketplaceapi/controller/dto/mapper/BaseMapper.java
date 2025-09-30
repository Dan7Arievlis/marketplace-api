package io.github.dan7arievlis.marketplaceapi.controller.dto.mapper;

import io.github.dan7arievlis.marketplaceapi.controller.dto.base.BaseResponseDTO;
import io.github.dan7arievlis.marketplaceapi.model.Base;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BaseMapper {

    BaseResponseDTO entityToResponse(Base base);
}
