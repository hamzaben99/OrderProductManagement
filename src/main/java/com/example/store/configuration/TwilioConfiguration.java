package com.example.store.configuration;

import com.example.store.service.NoOpSmsSender;
import com.example.store.service.SmsSender;
import com.example.store.service.TwilioSmsSender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sms.twilio")
public class TwilioConfiguration {
    private String sid;
    private String token;
    private String number;

    public TwilioConfiguration(){}

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
