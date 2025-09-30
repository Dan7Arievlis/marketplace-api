package io.github.dan7arievlis.marketplaceapi.service.component;

import org.springframework.data.jpa.domain.Specification;

import java.util.function.Function;

public interface OptionalSpecificationSearch {
    public default <V, T> Specification<T> optSpec(V v, Function<V, Specification<T>> f) {
        return v == null ? Specification.unrestricted() : f.apply(v);
    }
}
