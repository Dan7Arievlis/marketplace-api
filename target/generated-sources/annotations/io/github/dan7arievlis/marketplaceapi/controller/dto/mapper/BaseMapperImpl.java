package io.github.dan7arievlis.marketplaceapi.controller.dto.mapper;

import io.github.dan7arievlis.marketplaceapi.controller.dto.base.BaseResponseDTO;
import io.github.dan7arievlis.marketplaceapi.model.Base;
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
public class BaseMapperImpl implements BaseMapper {

    @Override
    public BaseResponseDTO entityToResponse(Base base) {
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
