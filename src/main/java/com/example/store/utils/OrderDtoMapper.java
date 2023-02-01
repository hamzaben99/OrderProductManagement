package com.example.store.utils;

import com.example.store.entity.Order;
import com.example.store.entity.ProductOrder;
import com.example.store.entity.dto.OrderDto;
import com.example.store.entity.dto.OrderDtoComplete;
import com.example.store.entity.dto.ProductQuantity;
import com.example.store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class OrderDtoMapper {
    @Autowired
    private ClientDtoMapper clientDtoMapper;
    @Autowired
    private ProductDtoMapper productDtoMapper;
    @Autowired
    private ProductRepository productRepository;

    public OrderDtoComplete mapEntityToDto(Order order) {
        OrderDtoComplete dto = new OrderDtoComplete();
        dto.setId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setOrderDeliveryDate(order.getOrderDeliveryDate());
        dto.setClientDto(clientDtoMapper.mapEntityToDto(order.getClient()));
        dto.setValidated(order.isValidated());
        dto.setPreorder(order.isPreorder());
        List<ProductQuantity> productsDto = new ArrayList<>();
        for (ProductOrder productOrder: order.getProductOrders()) {
            ProductQuantity product = new ProductQuantity(productOrder.getProduct().getId(), productOrder.getQuantity());
            productsDto.add(product);
        }
        dto.setProducts(productsDto);
        return dto;
    }

    public Order mapDtoToEntity(OrderDto dto, boolean isPreorder) {
        // Idk if i need to set productorders here, cuz i set it in add order
        Order order = new Order(LocalDateTime.now(), dto.getOrderDeliveryDate(), clientDtoMapper.mapDtoToEntity(dto.getClientDto()), isPreorder);
        //order.setProductOrders(new HashSet<>());
        return order;
    }
}
