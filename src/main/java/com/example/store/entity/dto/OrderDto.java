package com.example.store.entity.dto;

import java.time.LocalDate;
import java.util.List;

public class OrderDto {
    private ClientDto clientDto;
    private LocalDate orderDeliveryDate;
    private List<ProductQuantity> products;

    public ClientDto getClientDto() {
        return clientDto;
    }

    public void setClientDto(ClientDto clientDto) {
        this.clientDto = clientDto;
    }

    public LocalDate getOrderDeliveryDate() {
        return orderDeliveryDate;
    }

    public void setOrderDeliveryDate(LocalDate orderDeliveryDate) {
        this.orderDeliveryDate = orderDeliveryDate;
    }

    public List<ProductQuantity> getProducts() {
        return products;
    }

    public void setProducts(List<ProductQuantity> products) {
        this.products = products;
    }
}
