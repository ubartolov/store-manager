package com.example.storemanager.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PRODUCT")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "product_price")
    private Integer productPrice;
    @OneToMany(mappedBy = "product")
    private List<StoreStock> storeStock = new ArrayList<>();

    public Product addStoreStock(StoreStock storeStock) {
        this.storeStock.add(storeStock);
        storeStock.setProduct(this);
        return this;
    }

    public Product removeStoreStock(StoreStock storeStock) {
        this.storeStock.remove(storeStock);
        storeStock.setProduct(null);
        return this;
    }
}
