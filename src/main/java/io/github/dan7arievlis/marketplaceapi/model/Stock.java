package io.github.dan7arievlis.marketplaceapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Entity
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Data
@Table(name="stocks")
public class Stock extends Base {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockItem> stockItems = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;
}
