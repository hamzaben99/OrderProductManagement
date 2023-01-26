package com.example.store.entity;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table (name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (nullable = false)
    private LocalDateTime orderDate;
    private LocalDate orderDeliveryDate;
    @ManyToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private Client client;
    private boolean validated = false;
    private boolean preorder;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<ProductOrder> productOrders = new HashSet<>();

    public Order() {}

    public Order(LocalDateTime orderDate, LocalDate orderDeliveryDate, Client client, boolean preorder) {
        this.orderDate = orderDate;
        this.client = client;
        this.preorder = preorder;
        this.orderDeliveryDate = orderDeliveryDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDate getOrderDeliveryDate() {
        return orderDeliveryDate;
    }

    public void setOrderDeliveryDate(LocalDate orderDeliveryDate) {
        this.orderDeliveryDate = orderDeliveryDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<ProductOrder> getProductOrders() {
        return productOrders;
    }

    public void setProductOrders(Set<ProductOrder> productOrders) {
        this.productOrders = productOrders;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public boolean isPreorder() {
        return preorder;
    }

    public void setPreorder(boolean preorder) {
        this.preorder = preorder;
    }
}
