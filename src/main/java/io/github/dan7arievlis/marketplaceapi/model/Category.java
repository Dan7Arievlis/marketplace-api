package io.github.dan7arievlis.marketplaceapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Data
@Table(name="categories")
public class Category extends Base {
    @Column(name = "name", length = 20, unique = true, nullable = false)
    private String name;
}
