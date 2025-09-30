package io.github.dan7arievlis.marketplaceapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Data
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class)
public class Product extends Base {
    @Column(name = "name", length = 30, unique = true, nullable = false)
    private String name;

    @Column(name = "description", length = 180, nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "products_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    private User vendor;

    @CreatedDate
    @Column(name = "publish_date", nullable = false)
    private LocalDate publishDate;

    @Column(name = "fabrication_date")
    private LocalDate fabDate;

    @Column(name = "expiry_date")
    private LocalDate expDate;
}
