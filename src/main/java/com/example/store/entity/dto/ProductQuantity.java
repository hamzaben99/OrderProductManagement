package com.example.store.entity.dto;

public class ProductQuantity {
    private Long productID;
    private Long quantity;

    public ProductQuantity(Long productID, Long quantity) {
        this.productID = productID;
        this.quantity = quantity;
    }

    public Long getProductID() {
        return productID;
    }

    public void setProductID(Long productID) {
        this.productID = productID;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
