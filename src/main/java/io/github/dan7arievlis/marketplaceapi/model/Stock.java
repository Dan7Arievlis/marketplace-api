package io.github.dan7arievlis.marketplaceapi.model;

import io.github.dan7arievlis.marketplaceapi.model.enums.ItemStatus;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private List<StockItem> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @Transactional
    public void addItem(StockItem item) {
        item.setStock(this);
        items.add(item);
    }

    @Transactional
    public void removeItem(StockItem item) {
        items.remove(item);
    }

    public Optional<StockItem> itemByProduct(Product product) {
        return items.stream().filter(item -> item.getProduct().equals(product) && item.getItemStatus().equals(ItemStatus.OPEN)).findFirst();
    }
}
