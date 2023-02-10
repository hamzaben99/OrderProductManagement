package com.example.store.service;

import com.example.store.entity.Order;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class NoOpSmsSender implements SmsSender {

    @Override
    public void sendSms(Order order){
      log.warn("no sms sent, sms is disabled");
    }
}
