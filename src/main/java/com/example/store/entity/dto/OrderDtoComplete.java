package com.example.store.entity.dto;

import java.time.LocalDateTime;

public class OrderDtoComplete extends OrderDto {
    private Long id;
    private LocalDateTime orderDate;
    private boolean validated;
    private boolean preorder;

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
