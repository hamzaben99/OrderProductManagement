package com.example.store.utils;

import com.example.store.entity.Product;
import com.example.store.entity.dto.ProductDto;
import com.example.store.entity.dto.ProductDtoComplete;
import org.springframework.stereotype.Component;

@Component
public class ProductDtoMapper {
    public ProductDtoComplete mapEntityToDto(Product product) {
        ProductDtoComplete dto = new ProductDtoComplete();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setUnitPrice(product.getUnitPrice());
        return dto;
    }

    public Product mapDtoToEntity(ProductDto dto) {
        return new Product(dto.getName(), dto.getUnitPrice(), dto.getStockQuantity());
    }
}
