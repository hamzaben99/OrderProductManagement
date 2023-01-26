package com.example.store.service;

import com.example.store.entity.Order;
import com.example.store.entity.Product;
import com.example.store.entity.ProductOrder;
import com.example.store.entity.dto.ClientDto;
import com.example.store.entity.dto.OrderDto;
import com.example.store.entity.dto.OrderDtoComplete;
import com.example.store.entity.dto.ProductQuantity;
import com.example.store.repository.OrderRepository;
import com.example.store.repository.ProductRepository;
import com.example.store.utils.ClientDtoMapper;
import com.example.store.utils.NoSuchOrderException;
import com.example.store.utils.NoSuchProductException;
import com.example.store.utils.OrderDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class OrderService {
    private static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_NUMBER_REGEX = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$";
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderDtoMapper orderDtoMapper;
    @Autowired
    private ClientDtoMapper clientDtoMapper;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private SmsSenderService smsSenderService;

    public List<OrderDtoComplete> getOrders() {
        return orderRepository
                .findAll()
                .stream()
                .map(order -> orderDtoMapper.mapEntityToDto(order))
                .toList();
    }

    public OrderDtoComplete getOrderById(Long id) {
        return orderDtoMapper
                .mapEntityToDto(orderRepository.findById(id).orElseThrow(() -> new NoSuchOrderException("Order " + id + " doesn't exist")));
    }

    public List<OrderDtoComplete> getOrdersByDate(LocalDate date) {
        return getOrdersBetweenDates(date.atStartOfDay(), date.atTime(23, 59, 59));
    }

    public List<OrderDtoComplete> getOrdersBetweenDates(LocalDateTime from, LocalDateTime to) {
        return orderRepository
                .findByOrderDateBetween(from, to)
                .stream()
                .map(order -> orderDtoMapper.mapEntityToDto(order))
                .toList();
    }

    public List<OrderDtoComplete> getOrdersByClientName(String firstName, String lastName) {
        if (firstName == null)
            return orderRepository
                    .findByClient_LastNameIgnoreCase(lastName)
                    .stream()
                    .map(order -> orderDtoMapper.mapEntityToDto(order))
                    .toList();

        return orderRepository
                .findByClient_FirstNameIgnoreCaseAndClient_LastNameIgnoreCase(firstName, lastName)
                .stream()
                .map(order -> orderDtoMapper.mapEntityToDto(order))
                .toList();
    }

    public List<OrderDtoComplete> getOrdersByClientPhoneNumber(String phoneNumber) {
        return orderRepository
                .findByClient_PhoneNumber(phoneNumber)
                .stream()
                .map(order -> orderDtoMapper.mapEntityToDto(order))
                .toList();
    }

    public List<OrderDtoComplete> getOrdersByDeliveryDate(LocalDate date) {
        return orderRepository
                .findByOrderDeliveryDate(date)
                .stream()
                .map(order -> orderDtoMapper.mapEntityToDto(order))
                .toList();
    }

    public List<OrderDtoComplete> getOrdersBetweenDeliveryDates(LocalDate from, LocalDate to) {
        return orderRepository
                .findByOrderDeliveryDateBetween(from, to)
                .stream()
                .map(order -> orderDtoMapper.mapEntityToDto(order))
                .toList();
    }

    @Transactional
    public boolean deleteOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NoSuchOrderException("Order " + id + " doesn't exist"));
        if (order == null)
            return false;
        beforeDelete(order);
        orderRepository.delete(order);
        return true;
    }

    @Transactional
    public void deleteAllOrders() {
        for (Order order: orderRepository.findAll()) {
            beforeDelete(order);
            orderRepository.delete(order);
        }
        //orderRepository.deleteAll();
    }

    @Transactional
    public OrderDtoComplete addOrder(OrderDto order, boolean isPreorder) {
        // Checking phone number and email pattern
        if (order.getClientDto().getEmailAddress() != null && !order.getClientDto().getEmailAddress().matches(EMAIL_REGEX))
            throw new IllegalArgumentException("Invalid Email Address");
        if (!order.getClientDto().getPhoneNumber().replace(" ", "").matches(PHONE_NUMBER_REGEX))
            throw new IllegalArgumentException("Invalid Phone Number");

        Order orderEntity = orderDtoMapper.mapDtoToEntity(order, isPreorder);
        Set<ProductOrder> productOrders = new HashSet<>();
        for (ProductQuantity product: order.getProducts()) {
            Product productEntity = productRepository.findById(product.getProductID()).orElseThrow(() -> new NoSuchProductException("Product " + product.getProductID() + " doesn't exist"));
            if (!isPreorder) {
                if (productEntity.getStockQuantity() >= product.getQuantity()) {
                    productEntity.setStockQuantity(productEntity.getStockQuantity() - product.getQuantity());
                    productOrders.add(new ProductOrder(orderEntity, productEntity, product.getQuantity()));
                } else
                    throw new IllegalStateException("The quantity " + product.getQuantity() + " is greater than the stock quantity");
            }
            else
                productOrders.add(new ProductOrder(orderEntity, productEntity, product.getQuantity()));
        }
        orderEntity.setProductOrders(productOrders);
        Order createdOrder = orderRepository.save(orderEntity);
        return orderDtoMapper.mapEntityToDto(createdOrder);
    }

    @Transactional
    public void deleteOrdersByDate(LocalDateTime date) {
        for (Order order: orderRepository.findByOrderDate(date)) {
            beforeDelete(order);
            orderRepository.delete(order);
        }
        //orderRepository.deleteByOrderDate(date);
    }

    @Transactional
    public void deleteOrdersBetweenDates(LocalDateTime from, LocalDateTime to) {
        for (Order order: orderRepository.findByOrderDateBetween(from, to)) {
            beforeDelete(order);
            orderRepository.delete(order);
        }
        //orderRepository.deleteByOrderDateBetween(from, to);
    }

    @Transactional
    public void deleteOrdersByClientName(String firstName, String lastName) {
        List<Order> orders;
        if (firstName == null)
            orders = orderRepository.findByClient_LastNameIgnoreCase(lastName);
        else
            orders = orderRepository.findByClient_FirstNameIgnoreCaseAndClient_LastNameIgnoreCase(firstName, lastName);
        for (Order order: orders) {
            beforeDelete(order);
            orderRepository.delete(order);
        }
        //orderRepository.deleteAll(orders);
    }

    @Transactional
    public OrderDtoComplete validateOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NoSuchOrderException("Order " + id + " doesn't exist"));
        if (order.isValidated())
            return orderDtoMapper.mapEntityToDto(order);
        order.setValidated(true);
        Order updatedOrder = orderRepository.save(order);
        // Send Mail or SMS to client
        ExecutorService executor = null;
        if (order.getClient().getEmailAddress() == null) {
            executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> smsSenderService.sendSms(order));
        }
        else {
            executor = Executors.newFixedThreadPool(2);
            executor.execute(() -> emailSenderService.sendEmail(order));
            executor.execute(() -> smsSenderService.sendSms(order));
        }
        executor.shutdown();
        return orderDtoMapper.mapEntityToDto(updatedOrder);
    }

    @Transactional
    public List<OrderDtoComplete> validateOrders(List<Long> ids) {
        List<Order> updatedOrders = new ArrayList<>();
        ExecutorService executor = Executors.newWorkStealingPool();
        for (Long id: ids) {
            Order order = orderRepository.findById(id).orElseThrow(() -> new NoSuchOrderException("Order " + id + " doesn't exist"));
            if (order.isValidated())
                continue;
            order.setValidated(true);
            updatedOrders.add(orderRepository.save(order));
            // Sending mail and sms in parallel
            executor.execute(() -> emailSenderService.sendEmail(order));
            executor.execute(() -> smsSenderService.sendSms(order));
        }
        executor.shutdown();
        return updatedOrders.stream().map(orderDtoMapper::mapEntityToDto).toList();
    }

    @Transactional
    public OrderDtoComplete updateOrderClient(Long id, ClientDto client) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NoSuchOrderException("Order " + id + " doesn't exist"));
        order.setClient(clientDtoMapper.mapDtoToEntity(client));
        Order updatedOrder = orderRepository.save(order);
        return orderDtoMapper.mapEntityToDto(updatedOrder);
    }

    @Transactional
    private void beforeDelete(Order order) {
        if (!order.isPreorder()) {
            if (!order.isValidated()) {
                for (ProductOrder po : order.getProductOrders()) {
                    po.getProduct().setStockQuantity(po.getProduct().getStockQuantity() + po.getQuantity());
                }
                orderRepository.save(order);
            }
        }
    }
}
