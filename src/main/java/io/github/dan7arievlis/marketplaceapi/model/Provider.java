package io.github.dan7arievlis.marketplaceapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Data
@Table(name="providers")
public class Provider extends Base {
    private String name;
}
