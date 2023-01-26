package com.example.store.entity.dto;

public class ProductDto {
    private String name;
    private Integer unitPrice;
    private Long StockQuantity;

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
}
