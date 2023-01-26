package com.example.store.entity;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (nullable = false, unique = true)
    private String name;
    @Column (nullable = false)
    private Integer unitPrice;
    @Column (nullable = false)
    private Long StockQuantity;
    //@OneToMany(cascade = CascadeType.ALL)
    //private List<Order> orders = new ArrayList<>();
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<ProductOrder> productOrders = new HashSet<>();

    public Product() {}

    public Product(String name, Integer unitPrice, Long StockQuantity) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.StockQuantity = StockQuantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Long getStockQuantity() {
        return StockQuantity;
    }

    public void setStockQuantity(Long stockQuantity) {
        StockQuantity = stockQuantity;
    }

    public Set<ProductOrder> getProductOrders() {
        return productOrders;
    }

    public void setProductOrders(Set<ProductOrder> productOrders) {
        this.productOrders = productOrders;
    }
}
