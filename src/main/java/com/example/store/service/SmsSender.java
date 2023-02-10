package com.example.store.service;

import com.example.store.entity.Order;

public interface SmsSender {
    void sendSms(Order order);
}

