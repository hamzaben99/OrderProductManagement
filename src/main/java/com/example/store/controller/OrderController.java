package com.example.store.controller;

import com.example.store.entity.dto.ClientDto;
import com.example.store.entity.dto.OrderDto;
import com.example.store.entity.dto.OrderDtoComplete;
import com.example.store.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping(value = "api/orders/")
    public List<OrderDtoComplete> getOrders() {
        return orderService.getOrders();
    }

    @GetMapping(value = "api/orders/{id}")
    public OrderDtoComplete getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @GetMapping(value = "api/orders/by/date")
    public List<OrderDtoComplete> getOrdersByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return orderService.getOrdersByDate(date);
    }

    @GetMapping(value = "api/orders/between/dates")
    public List<OrderDtoComplete> getOrdersBetweenDates(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return orderService.getOrdersBetweenDates(from, to);
    }

    @GetMapping(value = "api/orders/by/client/name")
    public List<OrderDtoComplete> getOrdersByClientName(@RequestParam(required = false) String firstName, @RequestParam String lastName) {
        return orderService.getOrdersByClientName(firstName, lastName);
    }

    @GetMapping(value = "api/orders/by/client/phone")
    public List<OrderDtoComplete> getOrdersByClientPhoneNumber(@RequestParam String phoneNumber) {
        return orderService.getOrdersByClientPhoneNumber(phoneNumber);
    }

    @GetMapping(value = "api/orders/by/deliveryDate")
    public List<OrderDtoComplete> getOrdersByDeliveryDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return orderService.getOrdersByDeliveryDate(date);
    }

    @GetMapping(value = "api/orders/between/deliveryDates")
    public  List<OrderDtoComplete> getOrdersBetweenDeliveryDates(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return orderService.getOrdersBetweenDeliveryDates(from, to);
    }

    @DeleteMapping(value = "api/orders/{id}")
    public ResponseEntity<?> deleteOrderById(@PathVariable Long id) {
        boolean deleted = orderService.deleteOrderById(id);
        if (!deleted)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<Long>(id, HttpStatus.OK);
    }

    @DeleteMapping(value = "api/orders")
    public ResponseEntity<?> deleteAllOrders() {
        orderService.deleteAllOrders();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "api/orders/by/date")
    public ResponseEntity<?> deleteOrdersByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        orderService.deleteOrdersByDate(date);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "api/orders/between/dates")
    public ResponseEntity<?> deleteOrdersBetweenDates(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        orderService.deleteOrdersBetweenDates(from, to);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "api/orders/by/client/name")
    public ResponseEntity<?> deleteOrdersByClientName(@RequestParam(required = false) String firstName, @RequestParam String lastName) {
        orderService.deleteOrdersByClientName(firstName, lastName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "api/orders")
    public ResponseEntity<?> addOrder(@RequestBody OrderDto order, @RequestParam boolean isPreorder) {
        OrderDtoComplete orderDto = orderService.addOrder(order, isPreorder);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(orderDto.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "api/orders/validate/{id}")
    public ResponseEntity<?> validateOrder(@PathVariable Long id) {
        OrderDtoComplete orderDto = orderService.validateOrder(id);
        return new ResponseEntity<OrderDtoComplete>(orderDto, HttpStatus.OK);
    }

    @PutMapping(value = "api/orders/validate/list/{ids}")
    public ResponseEntity<?> validateOrders(@PathVariable List<Long> ids) {
        List<OrderDtoComplete> orderDtos = orderService.validateOrders(ids);
        return new ResponseEntity<List<OrderDtoComplete>>(orderDtos, HttpStatus.OK);
    }

    @PutMapping(value = "api/orders/{id}/client")
    public ResponseEntity<?> updateOrderClient(@PathVariable Long id, ClientDto client) {
        OrderDtoComplete orderDto = orderService.updateOrderClient(id, client);
        return new ResponseEntity<OrderDtoComplete>(orderDto, HttpStatus.OK);
    }
}
