package com.example.store.repository;

import com.example.store.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderDeliveryDateBetween(LocalDate orderDeliveryDateStart, LocalDate orderDeliveryDateEnd);
    List<Order> findByOrderDeliveryDate(LocalDate orderDeliveryDate);
    List<Order> findByClient_PhoneNumber(String phoneNumber);
    List<Order> findByClient_LastNameIgnoreCase(String lastName);
    List<Order> findByClient_FirstNameIgnoreCaseAndClient_LastNameIgnoreCase(String firstName, String lastName);
    List<Order> findByOrderDateBetween(LocalDateTime orderDateStart, LocalDateTime orderDateEnd);
    List<Order> findByOrderDate(LocalDateTime orderDate);

}
